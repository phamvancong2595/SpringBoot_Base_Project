#!/bin/sh
# Maven Wrapper script
# This script downloads and runs the Maven Wrapper if not already present

set -e

MAVEN_WRAPPER_VERSION=3.2.0
MAVEN_VERSION=3.9.6
WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"

# If the wrapper jar doesn't exist, download it
if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    mkdir -p .mvn/wrapper
    if command -v wget > /dev/null; then
        wget -q -O "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/$MAVEN_WRAPPER_VERSION/maven-wrapper-$MAVEN_WRAPPER_VERSION.jar"
    elif command -v curl > /dev/null; then
        curl -sL -o "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/$MAVEN_WRAPPER_VERSION/maven-wrapper-$MAVEN_WRAPPER_VERSION.jar"
    else
        echo "Error: wget or curl is required to download the Maven Wrapper."
        exit 1
    fi
fi

# Create properties file if not exists
if [ ! -f "$WRAPPER_PROPERTIES" ]; then
    echo "distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip" > "$WRAPPER_PROPERTIES"
    echo "wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/$MAVEN_WRAPPER_VERSION/maven-wrapper-$MAVEN_WRAPPER_VERSION.jar" >> "$WRAPPER_PROPERTIES"
fi

# Execute Maven via wrapper jar
exec java \
    $MAVEN_OPTS \
    -classpath "$WRAPPER_JAR" \
    org.apache.maven.wrapper.MavenWrapperMain "$@"
