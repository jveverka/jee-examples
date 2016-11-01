#!/bin/bash
# this script uses google protocol buffers compiler to create 
# java classes from users.proto file
# install protoc using 'sudo apt install protobuf-compiler' command
#

rm -rf ../java/itx/hybridapp/common/protocols/*
protoc --proto_path=. --java_out=../java common.proto
protoc --proto_path=. --java_out=../java user-access.proto
protoc --proto_path=. --java_out=../java data-service.proto
protoc --proto_path=. --java_out=../java device-service.proto
protoc --proto_path=. --java_out=../java chat-service.proto
protoc --proto_path=. --java_out=../java test-service.proto

