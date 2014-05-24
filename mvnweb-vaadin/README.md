[tick]: https://raw.github.com/retalemine/roadmap/master/images/tick-16x12.png "Done"

####Action Items:
* Create vaadin project using archetype ![][tick]
* Import to eclipse ![][tick]
* Run on server ![][tick]
* Run in debug mode ![][tick]
* Code billing component UI
  * Dynamic loading of product catalog. Include Lazy loading.
  * Dynamic addition of new product that resolves product name and product unit and model to collect unit, in case.
  * Input error handling.
  * Dynamic loading of unit price value ![][tick]
  * Dynamic suggestion of quantity in selected valid unit ![][tick]
  * Dynamic suggestion of quantity in various valid unit
  * Dynamic suggestion of quantity in various valid unit via JS without server side interaction
  * Add to cart
  * units in pkt and pcs ![][tick]
* Code billing services using mongoDB
* Test Cases

####Known Issues and Enhancements:
* With product name not mentioned, upon entering price getting error. ![][tick]
* End user browser time should be chosen to display bill time. And subsequently need an analysis on mongodb date save w.r.t browser local time.
* Enable user to define tax and tax groups. Also perform a business analysis on implication of each tax type especially the VAT tax which may vary across product category.
* Noticeable size for 'bill me' button.
* Include feature to cancel a bill.
* Ensure that item quantity is not zero. ![][tick]
* Include feature to edit an billItem in table.
* Include feature to enable cashier to input the received pay amount and display the pay back amount.
* Container getting updated with duplicate values. Be it ProductNameCB container or the BillItemTB container.

####Commands:  
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

