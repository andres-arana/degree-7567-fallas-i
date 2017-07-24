FROM clojure:lein

# Setup the project directory
RUN mkdir -p /opt/project
WORKDIR /opt/project

# Setup the application dependencies
COPY project.clj /opt/project/
RUN lein deps

# Setup the application code
COPY src /opt/project/src
