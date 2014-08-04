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
  * Add to cart ![][tick]
  * Update cart ![][tick]
  * units in dz and pcs ![][tick]
  * units in pkt
* Code billing services using mongoDB
* Print bill
* Print deliverable
* Test Cases

####Known Issues and Enhancements:
* With product name not mentioned, upon entering price getting error. ![][tick]
* End user browser time should be chosen to display bill time. ![][tick]
* Analysis on mongodb date save w.r.t browser local time.
* Enable user to define tax and tax groups. Also perform a business analysis on implication of each tax type especially the VAT tax which may vary across product category.
* Noticeable size for 'bill me' button. ![][tick]
* Include feature to cancel a bill. ![][tick]
* Ensure that item quantity is not zero. ![][tick]
* Include feature to edit an billItem in table. ![][tick]
* Include feature to enable cashier to input the received pay amount and display the pay back amount. ![][tick]
* ProductNameCB container getting updated with duplicate values. ![][tick]
* BillItemTB container getting updated with duplicate values.
* Not able to manually enter date to payable date.
* Negative floating value as pay back amount is throwing error.

####Components:
 * ProductComboBox
   * Holds product description __sugar - 1kg__
   * Data construction
     * Dynamic
     * Lazy loading
     * Cache
   * Error handling
   * Listens:
     * RateSelectionEvent
     * CartSelectionEvent
     * BillItemSelectionEvent
   * Post:
     * ProductSelectionEvent
       * Product [Name,Unit]
       * Product [Name,Unit,UnitRates]

 * RateComboBox     
   * Holds unitRates __ 145.50 INR__
   * Error handling
   * Listens:
     * ProductSelectionEvent
     * CartSelectionEvent
     * BillItemSelectionEvent     
   * Post:
     * RateSelectionEvent
       * Rate [new]
       * Rate [old]

 * QuantityComboBox
   * Holds quantityUnit __1 kg__
   * Data construction
     * Suggest reasonable units
   * Error handling
   * Listens:
     * ProductSelectionEvent
     * RateSelectionEvent
     * CartSelectionEvent
     * BillItemSelectionEvent     
   * Post:
     * QuantitySelectionEvent
       * NetQuantity
           
 * CartButton
   * Add/Update Cart
   * Validation & Error Handling
   * Listens:
     * ProductSelectionEvent
     * RateSelectionEvent
     * QuantitySelectionEvent
     * BillItemSelectionEvent     
   * Post:
     * CartSelectionEvent
       * Add -> add					[produntDesc,unitRate] - new
       * Add -> update				[produntDesc,unitRate] - exist
       * Update -> update			[produntDesc,unitRate] - exist [qty modified]
       * Update -> remove;add		[produntDesc,modified unitRate] - new   [unitRate itself modified(optional-qty modified)]
       * Update -> remove;update	[produntDesc,modified unitRate] - exist [unitRate itself modified(optional-qty modified)]

 * BillTable
   * Holds billItems
   * Error handling
   * Listens:
     * CartSelectionEvent     
   * Post:
     * BillItemSelectionEvent
       * BillItem [selected]
       * BillItem [unselected]

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
 * mvn compile -U __force update__

