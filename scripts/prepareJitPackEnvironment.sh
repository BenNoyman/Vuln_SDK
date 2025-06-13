#!/bin/bash

# Prepare JitPack Environment for VulnSDK
echo "Preparing JitPack environment for VulnSDK..."

# Set JAVA_HOME to JDK 11 for JitPack
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/

# Make gradlew executable
chmod +x gradlew

echo "JitPack environment prepared successfully!" 