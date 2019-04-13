#!/bin/bash

cd "$( dirname "${BASH_SOURCE[0]}" )/.."

mvn clean install -Dmaven.javadoc.skip=true -Dgpg.skip=true
[ -d dist ] || mkdir dist
cp ./target/pma-helper-*-jar-with-dependencies.jar dist/pma-helper.jar

