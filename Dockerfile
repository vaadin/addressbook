# Build stage
FROM maven:latest AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the source code directory to the container
COPY src /app/src
# Copy the pom.xml file to the container
COPY pom.xml /app

# Run the Maven build to compile and package the application
RUN mvn clean install

# Final stage
FROM tomcat:9.0-alpine

# Copy the built WAR file from the build stage to the Tomcat webapps directory
COPY --from=build /app/target/addressbook.war /usr/local/tomcat/webapps/

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat and run the deployed application
CMD ["catalina.sh", "run"]
