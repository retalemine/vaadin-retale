####Action Items:
* Create vaadin project using archetype
* Import to eclipse
* Run on server
* Run in debug mode
* code billing function

####Commands:
* Vaadin application creation using 'application' archetype (includes custom widget):
```
mvn archetype:generate \
   -DarchetypeGroupId=com.vaadin \
   -DarchetypeArtifactId=vaadin-archetype-application \
   -DarchetypeVersion=LATEST \
   -DgroupId=in.retalemine \
   -DartifactId=mvnvaadin \
   -Dversion=1.0 \
   -Dpackaging=war
```

* Creating war and custom widgetset:
```
mvn package
```

* Running the app using embedded jetty server:
```
mvn jetty:run
visit: http://localhost:8080/
```



