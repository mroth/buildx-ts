This is the companion repository hosting the files used to generate the sample
data for **TODO: ISSUE HERE ONCE FILED?**.

## Background

In the Java JVM, `File.lastModified` returns a _"A `long` value representing the
time the file was last modified, measured in milliseconds since the epoch"._

On many JDKs and operating systems (such a Linux OpenJDK8), this actually
returns a number rounded to the whole second, losing the millisecond
part.[[*]](https://bugs.openjdk.java.net/browse/JDK-8177809)

Something is different in the OS/environment for buildx builders using the
"docker-container" driver, such that for files with a timestamp above the
millisecond midpoint (e.g. subsecond millis >=500), the rounded up value is
read. In contrast, in the default ("docker") driver, the truncated/rounded down
is would be read (which matches the behavior of `docker run`.) 

_Because of this, if using a `docker-container` driver in buildx, it is possible
that a file timestamp for the same file will appear differently during the
buildtime (managed by buildx), and a later `docker run` invocation of a
container based on the image (managed by the docker driver)._

## Sample Data

Generated via the sample scripts in this repository.

Running with image built using **default driver**, always gets same file
timestamp between buildtime and runtime:

    docker run --rm -it buildx-ts:default sh script-runtime.sh
    OUTPUT FROM BUILD TIME:
    WRITER: The current OS system time appears to me as: 1566945971365
    WRITER: -> Creating new files: /tmp/file1.ts /tmp/file2.ts
    WRITER: -> Fudging modification timestamp of /tmp/file1.ts to 1566945971111
    WRITER: -> Fudging modification timestamp of /tmp/file2.ts to 1566945971999
    READER: Reading file lastModified timestamp of /tmp/file1.ts: 1566945971000
    READER: Reading file lastModified timestamp of /tmp/file2.ts: 1566945971000
    --------------------
    OUTPUT FROM RUNTIME:
    READER: Reading file lastModified timestamp of /tmp/file1.ts: 1566945971000
    READER: Reading file lastModified timestamp of /tmp/file2.ts: 1566945971000

Running with image built using **docker-container driver**, that *gets different
file timestamp* (note `file2.ts`) between buildtime and runtime:

    docker run --rm -it buildx-ts:container sh script-runtime.sh
    OUTPUT FROM BUILDTIME:
    WRITER: The current OS system time appears to me as: 1566945706942
    WRITER: -> Creating new files: /tmp/file1.ts /tmp/file2.ts
    WRITER: -> Fudging modification timestamp of /tmp/file1.ts to 1566945706111
    WRITER: -> Fudging modification timestamp of /tmp/file2.ts to 1566945706999
    READER: Reading file lastModified timestamp of /tmp/file1.ts: 1566945706000
    READER: Reading file lastModified timestamp of /tmp/file2.ts: 1566945706000
    --------------------
    OUTPUT FROM RUNTIME:
    READER: Reading file lastModified timestamp of /tmp/file1.ts: 1566945706000
    READER: Reading file lastModified timestamp of /tmp/file2.ts: 1566945707000

## Proposed Resolution

Technically I believe the docker-container driver behavior here to be more
"proper", but the difference in behavior between the build environment and the
run environment can potentially be considered problematic. In particular, it
appears to cause issues with improper cache invalidation in some build systems
(such as Scala's SBT), which is how I encountered it originally.

(In an ideal world, JVM based tooling would use a more reliable methodology of
checking file changes, such as checksumming the actual contents. However, in the
shorter term, and considering buildx is in active development, I'm hoping the
behavior can potentially be addressed in buildx to be consistent?)