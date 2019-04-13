#!/bin/bash

cd "$( dirname "${BASH_SOURCE[0]}" )/.."

[ -d dist ] || ./cmds/compile
cd dist
java -jar pma-helper.jar "$@"
