FROM bitnami/tomcat
COPY target/addressbook-2.0.war /apps/addressbook.war
