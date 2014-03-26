[tick]: https://raw.github.com/retalemine/roadmap/master/images/tick-16x12.png "Done"

####Action Items:
* Create vaadin project using archetype ![][tick]
* Import to eclipse ![][tick]
* Run on server ![][tick]
* Run in debug mode ![][tick]
* Code billing component UI ![][tick]

####Known issues:
* Total can possibly have four decimals
* Update cart button enables without modification
* Robust way of handling unit price unit and quantity unit

####Vaadin classes - Try out:
* FieldGroup, TabSheet, RichTextArea, Embedded, Table(addGeneratedColumn)
* Popup view, Keyboard shortcuts, Voverlay, Combobox dropdown,
* TextChangeEventMode.LAZY
* Custom Filter
* ContainerDataSource, ItemDataSource, PropertyDataSource
* BeanItemContainer, IndexedContainer, LazyQueryContainer, FilesystemContainer...
* AbstractApplicationServlet
* FacadeFactory

####Web.xml:
* Params
  * productionMode
  * contextConfigLocation
  * contextInitializerClasses --> ApplicationContextInitializer<ConfigurableWebApplicationContext>
* Listeners  
  * ContextLoaderListener
  * RequestContextListener

####Classes
* WebApplicationContextUtils
* ConfigurableEnvironment -> setActiveProfiles

####Packages Name
* View
  * ui
  * view
  * window
* Controller
  * filter
  * service
  * util
  * controller
* Model
  * bean
  * entity
  * domain model
  * query
  * repository
  * container

####Naming Enabler:
* ContextProfileInitializer
* ContextHelper
* ApplicationServlet

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



