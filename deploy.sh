#!/bin/bash

set -e

pip install --user awscli
export PATH=$PATH:$HOME/.local/bin

./build.sh
