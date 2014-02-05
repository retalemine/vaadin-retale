package in.retalemine;

import javax.servlet.annotation.WebServlet;

import org.jsoup.nodes.Document.QuirksMode;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "in.retalemine.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalSplitPanel verSplitPanel = new VerticalSplitPanel();
        setContent(verSplitPanel);
        
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
            	new MyMongoDB().updateProductSet();
                layout.addComponent(new Label("Mongo DB updated"));
            }
        });
        layout.addComponent(button);
        verSplitPanel.addComponent(layout);

        Label console = new Label();
        console.setValue("MongoDB collections: "+new MyMongoDB().printCollections());
        verSplitPanel.addComponent(console);
        
    }

}
