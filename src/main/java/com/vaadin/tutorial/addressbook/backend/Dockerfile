# Use Tomcat with Java 17 (matches a WAR project)
FROM tomcat:9.0-jdk17

# Clean default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR built by Maven
COPY target/addressbook.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
