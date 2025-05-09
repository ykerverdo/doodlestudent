#!/bin/bash
set -eu
clang -g -O1 -fsanitize=address,fuzzer -o fuzz_target_1 fuzz_target_1.c
