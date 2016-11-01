#!/bin/bash
# this script uses google protocol buffers compiler to create 
# java classes from users.proto file
# install protoc using 'sudo apt install protobuf-compiler' command
#

protoc --proto_path=. --java_out=../java users.proto
