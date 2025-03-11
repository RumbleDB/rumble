#!/bin/bash

# Script provided by https://github.com/fkellner, Thank you!

INSTALL_PATH=$HOME/RumbleDB
APACHE_SPARK_RELEASE=3.2.0
HADOOP_RELEASE=3.2
RUMBLEDB_VERSION=1.15.0

if [ "$1" == "--help" ]; then
  echo "Usage:"
  echo "    ./install-rumbledb.sh"
  echo "Downloads Apache Spark and RumbleDB (and installs Java 8 if not present)"
  echo "Creates a RumbleDB REPL script and a RumbleDB File execution script"
  echo "Scripts are added to PATH via an edit to ~/.bashrc"
  echo "Everything, including an uninstaller, will be stored in $INSTALL_PATH"
  echo ""
  echo "This script will download Apache Spark $APACHE_SPARK_RELEASE, Hadoop $HADOOP_RELEASE"
  echo "and RumbleDB $RUMBLEDB_VERSION. To change that, edit the variables declared at the"
  echo "top of this script."
  exit 0
fi

echo "######### checking if we need to install Java"
if [ $(which java) ]; then
  echo "Java version is $(java -version). Version 8 is recommended, but 11 might work, too."
else
  if [ $(which apt-get) ]; then
    echo "No Java found, trying to install OpenJDK 8 JRE via apt-get"
    echo "You will need to enter your password for this"
    sudo apt-get install openjdk-8-jre
  else
    echo "No Java and no apt-get package manager found, please install Java 8"
    echo "and then return."
    exit 1
  fi
fi

echo "########## creating installation folder"
mkdir $INSTALL_PATH
cd $INSTALL_PATH

echo "########## downloading Apache Spark"
SPARK_FILENAME=spark-$APACHE_SPARK_RELEASE-bin-hadoop$HADOOP_RELEASE.tgz
wget https://dlcdn.apache.org/spark/spark-$APACHE_SPARK_RELEASE/$SPARK_FILENAME
wget https://downloads.apache.org/spark/KEYS
wget https://downloads.apache.org/spark/spark-$APACHE_SPARK_RELEASE/$SPARK_FILENAME.asc

echo "##### verifying download if gpg is installed"
if [ $(which gpg) ]; then
  echo "### importing apache spark signing keys"
  gpg --import KEYS
  echo "### verifying"
  gpg --verify $SPARK_FILENAME.asc $SPARK_FILENAME
  echo "### note:"
  echo "a warning like 'There is no indication that the signature belongs to the owner.'"
  echo "can relatively safely be ignored, to alleviate it you would need to get in"
  echo "touch with an Apache Spark developer."
else
  echo "gpg not installed, could not verify download"
fi

echo "if there is reason for concern, you have 5s to abort"

sleep 5

echo "##### unpacking file"
tar -xzf $SPARK_FILENAME

echo "########### downloading RumbleDB"
wget "https://github.com/RumbleDB/rumble/releases/download/v$RUMBLEDB_VERSION/rumbledb-$RUMBLEDB_VERSION.jar"

echo "########### creating scripts"
cat >rumble-repl <<EOF
#!/bin/bash
RUMBLE_JAR_PATH=$INSTALL_PATH/rumbledb-$RUMBLEDB_VERSION.jar
SPARK_HOME=$INSTALL_PATH/spark-$APACHE_SPARK_RELEASE-bin-hadoop$HADOOP_RELEASE
JAVA_VERSION=\$(java -version)
if [ "\$1" == "--help" ]; then
	echo "Usage:";
	echo "    rumble-repl"
	echo "Invokes an interactive RumbleDB Shell using downloaded JARs and Binaries"
	echo "Rumble Jar taken from \$RUMBLE_JAR_PATH"
	echo "Spark resides at \$SPARK_HOME"
	echo "Current version of Java: \$JAVA_VERSION"
	exit 0
fi

\$SPARK_HOME/bin/spark-submit \$RUMBLE_JAR_PATH --shell yes --output-format json
EOF

cat >rumble-file <<EOF
#!/bin/bash
RUMBLE_JAR_PATH=$INSTALL_PATH/rumbledb-$RUMBLEDB_VERSION.jar
SPARK_HOME=$INSTALL_PATH/spark-$APACHE_SPARK_RELEASE-bin-hadoop$HADOOP_RELEASE
JAVA_VERSION=\$(java -version)
if [ "\$1" == "--help" ]; then
	echo "Usage:";
	echo "    rumble-file <file>"
	echo "Execute JSONiq query taken from <file> using RumbleDB"
	echo "Rumble Jar taken from \$RUMBLE_JAR_PATH"
	echo "Spark resides at \$SPARK_HOME"
	echo "Current version of Java: \$JAVA_VERSION"
	exit 0
fi

\$SPARK_HOME/bin/spark-submit \$RUMBLE_JAR_PATH --query-path \$1 --output-format json
EOF

cat >uninstall-rumble <<EOF
#!/bin/bash
if [ "\$1" == "--help" ]; then
	echo "Usage:";
	echo "    uninstall-rumble"
	echo "Uninstalls Rumble by removing $INSTALL_PATH and"
	echo "removing the PATH modification in ~/.bashrc"
	echo "If the installer installed Java, it will stay installed."
	exit 0
fi

echo "Removing Path modification from ~/.bashrc"
cp ~/.bashrc bashrc-old
cat bashrc-old | sed 's/PATH=\$PATH:$(echo $INSTALL_PATH | sed 's/\//\\\//g')\/scripts//g' > ~/.bashrc

echo "Deleting files"
cd ~
rm -rf $INSTALL_PATH
EOF

mkdir scripts
mv rumble-repl rumble-file uninstall-rumble scripts
chmod +x scripts/*

echo "########## editing ~/.bashrc to add scripts to path"
echo "PATH=\$PATH:$INSTALL_PATH/scripts" >>~/.bashrc

echo "########## done. reloading ~/.bashrc and showing help output of scripts"
source ~/.bashrc
rumble-file --help
rumble-repl --help
uninstall-rumble --help
