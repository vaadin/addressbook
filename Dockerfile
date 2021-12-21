FROM bitnami/tomcat
COPY target/addressbook-2.0.war /app/addressbook.war
