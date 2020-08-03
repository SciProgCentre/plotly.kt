#!/usr/bin/python3
import sys
import subprocess as sp

argc = len(sys.argv)
if argc != 5:
    sys.exit("ERROR: Invalid arguments number: "+ ", argc = " + str(argc))
json_string = sys.argv[1]
file_name = sys.argv[2]
directory = sys.argv[3]
format = sys.argv[4]
sp.run(["orca", "graph", json_string, "-o", file_name, "-d", directory, "-f", format], stdin=sp.PIPE, stdout=sp.PIPE)