[tick]: https://raw.github.com/retalemine/roadmap/master/images/tick-16x12.png "Done"

####Action Items:
* Create vaadin project using archetype ![][tick]
* Import to eclipse ![][tick]
* Run on server ![][tick]
* Run in debug mode ![][tick]
* Code billing component UI
  * Dynamic loading of product catalog
  * Dynamic loading of unit price value ![][tick]
  * Dynamic suggestion of quantity in various valid unit
  * Add to cart
  * units in pkg and pcs
* Code billing services using mongoDB

####Events:
* Build components skeleton without data
* Search product
  * dynamic fetch( limit and lazy loading on scroll)
* Product on confirm
  * load price (filter)
  * load quantity unit (filter  logic) 
  
* Maven archetype
```
mvn archetype:generate \
-DarchetypeArtifactId=maven-archetype-webapp \
-DarchetypeVersion=LATEST
```

* Maven commands:
 * mvn eclipse:eclipse -Dwtpversion=2.0 __converts to eclipse web project__
 * mvn clean
 * mvn compile
 * mvn jetty:run  __scattered mode__
 * mvn jetty:run-war  __packaged mode__
 * mvn jetty:stop
 * mvn -Djetty.port=8181 jetty:run __enables to run the app in another port if 8080 is occupied__
 * mvn package  __creates war file__

