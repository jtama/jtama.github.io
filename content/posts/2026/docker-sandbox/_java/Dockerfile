FROM maven:3-eclipse-temurin-27 AS src

FROM scratch
COPY --from=src /opt/java/openjdk /opt/java/openjdk
COPY --from=src /usr/share/maven /usr/share/maven
COPY --chmod=0755 setup.sh /usr/local/lib/java-maven/setup.sh