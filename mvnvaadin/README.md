[tick]: https://raw.github.com/retalemine/roadmap/master/images/tick-16x12.png "Done"

####Action Items:
* Create vaadin project using archetype ![][tick]
* Import to eclipse ![][tick]
* Run on server ![][tick]
* Run in debug mode ![][tick]
* Code billing component UI ![][tick]
* Code billing services using mongoDB

####Known issues:
* Total can possibly have four decimals
* Update cart button enables without modification

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



