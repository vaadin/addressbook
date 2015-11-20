Addressbook Tutorial
====================

This tutorial teaches you some of the basic concepts in [Vaadin Framework](https://vaadin.com). It is meant to be
a fast read for learning how to get started - not an example on how application should be
designed. Please note this example uses and requires Java 8 to work.

![Addressbook Screenshot](addressbook_screenshot.png "Addressbook Screenshot")


Running the example from the command line
-------------------
```
$ mvn jetty:run
```

Open [http://localhost:8080/](http://localhost:8080/)


Importing in IntelliJ IDEA 14
--------------------
These instructions were tested on IntelliJ IDEA 14 CE. You can get it from https://www.jetbrains.com/idea/

To get the project up and running in IDEA, do:
- File -> New -> Project from Version Control -> Git
- The URL to use is https://github.com/vaadin/addressbook.git
- If you get a message about "Non-managed pom.xml file found". Choose "Add as Maven Project"
- If you get a message about no JDK or SDK being selected. Choose "Configure" and select your installed JDK. You can also set the JDK using File -> Project Structure
- To start the project, find the "Maven Projects" tab on the right hand side of the screen and navigate to
  - Vaadin Web Application -> Plugins -> jetty -> jetty:run
  - Click the play button or right click and select Run (Select Debug instead to run in debug mode)

You should now have a Jetty server running on localhost:8080. Navigate to http://localhost:8080 to play with the application

Importing in NetBeans 8
--------------------
These instructions were tested on NetBeans 8.0.2. You can get it from https://www.netbeans.org

To checkout and run the project in NetBeans, do:
- Team -> Git -> Clone
- Set repository URL to https://github.com/vaadin/addressbook.git
- Finish
- Right click the imported project (Vaadin Addressbook Application) and select Run
- Select GlassFish Server 4.1 -> Remember in Current IDE Session -> OK

You should now have a GlassFish server running on localhost:8080 and a browser tab should also be automatically opened with this location

Importing in Eclipse
--------------------
These instructions were tested on Eclipse IDE for Java EE Developers Luna SR2. You can get it from http://eclipse.org/downloads/

To checkout and run the project in Eclipse, do:
- File -> Import...
- Check out Maven Projects from SCM
- Choose Git from SCM menu
  - If you do not see "Git" in the SCM menu, click "Find more SCM connectors in the m2e Marketplace" and install "m2e-egit". Restart Eclipse and start over.
- Set the repository URL to https://github.com/vaadin/addressbook.git
- Right click the imported "addressbook" and choose Run As -> Maven Build...
  - Set the goal to "jetty:run" and click "Run"

You should now have a Jetty server running on localhost:8080. Navigate to [http://localhost:8080/](http://localhost:8080/) to play with the application

To use the built in server adapters of Eclipse, instead of doing "Run As -> Maven Build..." you can do
- Run As -> Run on Server
- Select the server you want to run on, e.g. Apache Tomcat 8 and click ok
- *Do not use the suggested J2EE Preview server* as it is outdated, deprecated and does not support Servlet 3, which is required for this application
