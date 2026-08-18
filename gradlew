#!/bin/sh
# Gradle wrapper - downloads gradle automatically
set -e
GRADLE_VERSION=8.9
GRADLE_HOME="$HOME/.gradle/wrapper/dists"
if [ ! -f "$HOME/.gradle/wrapper/gradle-$GRADLE_VERSION/bin/gradle" ]; then
  echo "Downloading Gradle $GRADLE_VERSION..."
  mkdir -p "$HOME/.gradle/wrapper"
  curl -sL -o /tmp/gradle.zip "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
  unzip -q -o /tmp/gradle.zip -d "$HOME/.gradle/wrapper"
fi
exec "$HOME/.gradle/wrapper/gradle-$GRADLE_VERSION/bin/gradle" "$@"