#!/bin/sh
# echo output from buildtime for comparison
echo "OUTPUT FROM BUILD TIME:"
cat buildlog-writer.txt
cat buildlog-reader.txt

# see how things look now at runtime
echo "--------------------"
echo "OUTPUT FROM RUNTIME:"
java TSReader
