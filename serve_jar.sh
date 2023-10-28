#!/usr/bin/env bash

# This script is used to rebuild and serve the web UI

./gradlew shadowJar && \
  clear && \
  java -jar build/libs/codestats.jar --mode=serve
