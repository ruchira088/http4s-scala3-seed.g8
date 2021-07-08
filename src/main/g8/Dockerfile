FROM openjdk:8-jdk

RUN apt-get update && \
    apt-get install apt-transport-https bc ca-certificates software-properties-common -y

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo apt-key add && \
    sudo apt update && \
    sudo apt install sbt

WORKDIR /opt/$name;format="normalize"$

COPY . .

EXPOSE 5005

ENTRYPOINT ["sbt", "-jvm-debug", "5005"]

CMD ["run"]
