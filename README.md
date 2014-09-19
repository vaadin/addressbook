Addressbook Tutorial
====================

This tutorial teaches you some of the basic concepts in Vaadin Framework. It is meant to be 
a fast read for learning how to get started - not an example on how application should be
designed.


Running the example
-------------------
mvn jetty:run


Importing in Eclipse
--------------------
Make sure you have "Eclipse IDE for Java EE Developers" and Maven integration "m2e-wtp" installed. You will get Eclipse from http://eclipse.org/downloads/ and plugins through Help -> Eclipse Marketplace... menu

To checkout and run the project from Eclipse, do:
- File -> Import...
- Check out Maven Projects from CMS
- Choose Git from SCM menu and set URL to git://github.com/vaadin/addressbook.git
  - If you do not see "Git" in the SCM menu, click "Find more SCM connectors in the m2e Marketplace" and install "m2e-egit"
- Now you should have an "addressbook" project in your workspace
- To run it, right click and choose Run As -> Run on Server
- Start experimenting

Note that if you are missing EGit plugin, "Maven SCM Handler for EGit" or a local server to run the address book on, you will be asked to install these while doing the above.
