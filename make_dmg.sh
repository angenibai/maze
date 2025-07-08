#!/bin/sh

jpackage --input bin/ \
  --name "chomp" \
  --main-jar chomp-0.4.0.jar \
  --main-class main.Main \
  --type dmg \
  --mac-package-identifier com.angenibai.chomp \
  --mac-package-name "chomp" \
  --app-version "1.0" \
  --java-options "-Xms256m -Xmx512m" \
  --dest ./dist