#!/bin/bash

# Script provided by https://github.com/fkellner, Thank you!

INSTALL_PATH=$HOME/RumbleDB
APACHE_SPARK_RELEASE=3.5.6
HADOOP_RELEASE=3
SCALA_RELEASE=2.13
RUMBLEDB_VERSION=1.23.0
JAVA_VERSION=17

SPARK_BASE_URL=https://dlcdn.apache.org/spark

if [ "$1" == "--help" ]; then
  echo "Usage:"
  echo "    ./install-rumbledb.sh"
  echo "Downloads Apache Spark and RumbleDB (and installs Java $JAVA_VERSION if not present)"
  echo "Creates a RumbleDB REPL script and a RumbleDB File execution script"
  echo "Scripts are added to PATH via an edit to ~/.bashrc"
  echo "Everything, including an uninstaller, will be stored in $INSTALL_PATH"
  echo ""
  echo "This script will download Apache Spark $APACHE_SPARK_RELEASE, Hadoop $HADOOP_RELEASE"
  echo "and RumbleDB $RUMBLEDB_VERSION. To change that, edit the variables declared at the"
  echo "top of this script."
  exit 0
fi

command -v wget >/dev/null 2>&1 || { echo "wget not found"; exit 1; }
command -v gpg >/dev/null 2>&1 || echo "WARNING: gpg not found — skipping verification"

echo "######### Checking if Java $JAVA_VERSION is installed"
if command -v java >/dev/null 2>&1; then
  JAVA_FOUND=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
  echo "Java version is $JAVA_FOUND. Version $JAVA_VERSION is required."
else
  echo "No Java found. Please install Java $JAVA_VERSION manually."
  echo "Reccomended OpenJDK $JAVA_VERSION JDK"
  exit 1
fi

echo "########## Creating installation folder"
mkdir -p "$INSTALL_PATH" || {
  echo "Failed to create $INSTALL_PATH"
  exit 1
}
cd "$INSTALL_PATH" || {
  echo "Failed to enter $INSTALL_PATH"
  exit 1
}

echo "########## Downloading Apache Spark"
SPARK_FILENAME="spark-$APACHE_SPARK_RELEASE-bin-hadoop$HADOOP_RELEASE-scala$SCALA_RELEASE.tgz"
SPARK_URL="$SPARK_BASE_URL/spark-$APACHE_SPARK_RELEASE/$SPARK_FILENAME"

attempt=0
while [ $attempt -lt 3 ]; do
  wget -O "$SPARK_FILENAME" "$SPARK_URL" && break
  if [ $? -eq 8 ]; then
    echo "Error 302: Redirect detected. Retrying in 3 seconds..."
    sleep 3
  else
    echo "Download failed. Retrying in 3 seconds..."
    sleep 3
  fi
  ((attempt++))
done

if [ $attempt -eq 3 ]; then
  echo "Failed to download Apache Spark after 3 attempts. Aborting."
  exit 1
fi

wget -O KEYS "$SPARK_BASE_URL/KEYS"
wget -O "$SPARK_FILENAME.asc" "$SPARK_URL.asc"

if command -v gpg >/dev/null 2>&1; then
  echo "### Importing Apache Spark signing keys"
  gpg --import KEYS
  echo "### Verifying"
  if ! gpg --verify "$SPARK_FILENAME.asc" "$SPARK_FILENAME"; then
    echo "Signature verification failed! Aborting."
    exit 1
  fi
else
  echo "GPG not installed, could not verify download"
fi

echo "##### Unpacking file"
tar -xzf "$SPARK_FILENAME"

echo "########### Downloading RumbleDB"
RUMBLE_JAR=rumbledb-$RUMBLEDB_VERSION-standalone.jar
wget -O "$RUMBLE_JAR" "https://github.com/RumbleDB/rumble/releases/download/v$RUMBLEDB_VERSION/$RUMBLE_JAR"

echo "########### Creating scripts"
mkdir -p scripts
cat >scripts/rumble-repl <<EOF
#!/bin/bash
RUMBLE_JAR_PATH=$INSTALL_PATH/$RUMBLE_JAR
SPARK_HOME=$INSTALL_PATH/${SPARK_FILENAME%.tgz}
JAVA_VERSION=\$(java -version 2>&1 | awk -F '"' '/version/ {print \$2}')
\$SPARK_HOME/bin/spark-submit \$RUMBLE_JAR_PATH --shell yes --output-format json
EOF

cat >scripts/rumble-file <<EOF
#!/bin/bash
RUMBLE_JAR_PATH=$INSTALL_PATH/$RUMBLE_JAR
SPARK_HOME=$INSTALL_PATH/${SPARK_FILENAME%.tgz}
JAVA_VERSION=\$(java -version 2>&1 | awk -F '"' '/version/ {print \$2}')
\$SPARK_HOME/bin/spark-submit \$RUMBLE_JAR_PATH --query-path \$1 --output-format json
EOF

cat >scripts/uninstall-rumble <<EOF
#!/bin/bash
echo "Removing PATH modification from ~/.bashrc"
sed -i "/$INSTALL_PATH\/scripts/d" ~/.bashrc
echo "Deleting files"
rm -rf "$INSTALL_PATH"
EOF

chmod +x scripts/*

if ! grep -q "$INSTALL_PATH/scripts" ~/.bashrc; then
  echo "export PATH=\"\$PATH:$INSTALL_PATH/scripts\"" >>~/.bashrc
fi

echo "########## Done. To use RumbleDB, restart your shell or run:"
echo "    export PATH=\"\$PATH:$INSTALL_PATH/scripts\""
