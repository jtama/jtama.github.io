#!/bin/sh
# Hook d'installation (root), exécuté à la création du sandbox.
set -eu

JAVA_HOME=/opt/java/openjdk
MAVEN_HOME=/usr/share/maven

# 0) Mettre java, javac, mvn... sur le PATH et exporter JAVA_HOME pour les shells
ln -sf "$JAVA_HOME"/bin/* /usr/local/bin/
ln -sf "$MAVEN_HOME/bin/mvn" /usr/local/bin/mvn
printf 'export JAVA_HOME=%s\nexport MAVEN_HOME=%s\n' "$JAVA_HOME" "$MAVEN_HOME" \
  > /etc/profile.d/java-maven.sh


# 3) Le volume ~/.m2 est monté en root : on le rend à l'utilisateur agent (UID 1000).
mkdir -p /home/agent/.m2
chown 1000:1000 /home/agent/.m2