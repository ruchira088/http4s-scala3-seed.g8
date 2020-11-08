FROM cimg/openjdk:11.0

# Install sbt
RUN echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list && \
    sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
    sudo apt update && \
    sudo apt install sbt -y

# Install kubectl
RUN curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add - && \
    echo "deb https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee -a /etc/apt/sources.list.d/kubernetes.list && \
    sudo apt update && \
    sudo apt install -y kubectl

# Install Ansible
RUN sudo apt update && \
    sudo apt install python3-pip -y && \
    pip3 install boto3 botocore openshift ansible