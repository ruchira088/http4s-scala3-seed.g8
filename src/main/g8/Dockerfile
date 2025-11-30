FROM eclipse-temurin:25-jdk

RUN apt-get update && \
    apt-get install apt-transport-https bc ca-certificates software-properties-common curl -y

# Install sbt
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import && \
    chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg && \
    apt-get update && \
    apt-get install sbt -y

WORKDIR /opt/$name;format="normalize"$

COPY . .

EXPOSE 8080 5005

ENTRYPOINT ["sbt", "-jvm-debug", "5005"]

CMD ["run"]
