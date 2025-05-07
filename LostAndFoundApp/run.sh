#!/bin/bash

# Get the script's current directory (project root)
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
LIB_JAR="$PROJECT_DIR/lib/mysql-connector-j-9.3.0.jar"
OUT_DIR="$PROJECT_DIR/out"
SOURCE_LIST="$PROJECT_DIR/sources.txt"

# Clean previous build
echo "🔧 Cleaning previous build..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

# Check if src directory exists
if [ ! -d "$SRC_DIR" ]; then
  echo "❌ 'src' directory not found in: $PROJECT_DIR"
  exit 1
fi

# Find all .java files
find "$SRC_DIR" -name "*.java" > "$SOURCE_LIST"

# Check if any source files were found
if [ ! -s "$SOURCE_LIST" ]; then
  echo "❌ No Java source files found in $SRC_DIR"
  rm "$SOURCE_LIST"
  exit 1
fi

# Compile Java files
echo "📦 Compiling Java files..."
javac -cp "$LIB_JAR" -d "$OUT_DIR" @"$SOURCE_LIST"
COMPILE_STATUS=$?

# Clean up sources.txt
rm "$SOURCE_LIST"

# Handle compile result
if [ $COMPILE_STATUS -ne 0 ]; then
  echo "❌ Compilation failed."
  exit 1
fi

# Run main class
echo "🚀 Running application..."
java -cp "$OUT_DIR:$LIB_JAR" app.Main


