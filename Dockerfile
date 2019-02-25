FROM ruby:2.5

# Install Java JDK
RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" >> /etc/apt/sources.list.d/webupd8team-java.list
RUN echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" >> /etc/apt/sources.list.d/webupd8team-java.list
RUN apt-key adv --no-tty --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get update && apt-get install -y oracle-java8-installer oracle-java8-set-default

# Set up Ruby environment to run Cucumber (if so, the base Ruby image could be used to install gems and run cucumber)
WORKDIR /usr/src/app
RUN bundle config --global frozen 1
COPY Gemfile Gemfile.lock Rakefile ./
RUN bundle install

ENTRYPOINT ["rake"]
CMD ["cucumber"]