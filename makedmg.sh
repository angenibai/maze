#!/bin/sh

jpackage --input bin/ \
  --name "maze" \
  --main-jar maze-1.0-SNAPSHOT.jar \
  --main-class main.Main \
  --type dmg \
  --mac-package-identifier com.angenibai.maze \
  --mac-package-name "maze" \
  --app-version "1.0" \
  --java-options "-Xms256m -Xmx512m" \
  --dest ./dist