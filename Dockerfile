FROM ubuntu:18.04

# Install system requirements
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y \
        git \
        python3 \
        python3-dev \
        python3-setuptools \
        python3-pip \
        nginx \
        supervisor \
        sqlite3 \
        virtualenv

# Remove apt caches to save space
RUN rm -rf /var/lib/apt/lists/*

# Install python deployment tools
RUN pip3 install -U pip setuptools
RUN pip3 install https://projects.unbit.it/downloads/uwsgi-lts.tar.gz

# Setup config files for nginx and supervisor
COPY deployment/nginx.conf /etc/nginx/sites-available/default
COPY deployment/supervisor.conf /etc/supervisor/conf.d/

# Install requirements early to help with docker caching
COPY requirements.txt /home/docker/code/
RUN pip3 install -r /home/docker/code/requirements.txt

# Copy all of the project files
COPY . /home/docker/code/

# Expose website on port 80
EXPOSE 80

# Run supervisor in interactive mode (-n signifies _not_ daemon)
CMD ["supervisord", "-n"]