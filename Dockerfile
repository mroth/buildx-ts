FROM openjdk:8u212-alpine
WORKDIR /src
COPY *.java .
RUN javac *.java
COPY *.sh .

RUN sh script-buildtime.sh
