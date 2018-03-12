#!/bin/bash

#Parameters:
#   1) file path
#   2) property key
#   3) property value

grep "^$2=" "$1" &> /dev/null
if [ $? -ne 0 ] ; then
    # append
    echo "$2=$3" >> "$1"
else
    # replace
    sed -i "s@^$2=.*@$2=$3@" "$1"
fi