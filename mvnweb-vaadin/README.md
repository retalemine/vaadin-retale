* Maven archetype
``
mvn archetype:generate \
-DarchetypeArtifactId=maven-archetype-webapp \
-DarchetypeVersion=LATEST
``

* Maven commands:
 * mvn eclipse:eclipse -Dwtpversion=2.0 __converts to eclipse web project__
 * mvn clean
 * mvn compile
 * mvn jetty:run  __scattered mode__
 * mvn jetty:run-war  __packaged mode__
 * mvn jetty:stop
 * mvn -Djetty.port=8181 jetty:run __enables to run the app in another port if 8080 is occupied__
 * mvn package  __creates war file__

