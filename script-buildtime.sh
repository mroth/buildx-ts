#!/bin/sh -x
# write the files, and read their timestamps. also write the output to a file in
# the image so we can easily echo it later from the runtime container for
# comparison with the results we get there.
java TSWriter buildtime.ts | tee buildlog-writer.txt
java TSReader buildtime.ts | tee buildlog-reader.txt
