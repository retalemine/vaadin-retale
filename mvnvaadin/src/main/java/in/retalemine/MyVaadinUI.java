package in.retalemine;

import java.util.Date;
import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
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
        /*Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
            	new MyMongoDB().updateProductSet();
                layout.addComponent(new Label("Mongo DB updated"));
            }
        });
        layout.addComponent(button);*/
        
        HorizontalLayout billNoLayout = new HorizontalLayout();
        billNoLayout.setSpacing(true);
        Label billNoCaption = new Label();
        billNoCaption.setValue("Bill no#");
        billNoLayout.addComponent(billNoCaption);
        TextField billNo = new TextField();
        billNo.setValue("1");
        billNo.setReadOnly(true);
        billNoLayout.addComponent(billNo);
        layout.addComponent(billNoLayout);
        
        HorizontalLayout billDateLayout = new HorizontalLayout();
        Label billDateCaption = new Label();
        billDateCaption.setValue("Date:");
        billDateLayout.addComponent(billDateCaption);
        DateField billDate = new DateField();
        billDate.setValue(new Date());
        billDate.setReadOnly(true);
        billDate.setDateFormat("dd-mm-yy");
        billDateLayout.addComponent(billDate);
        billDateLayout.setSpacing(true);
        layout.addComponent(billDateLayout);
        
        HorizontalLayout productInfoLayout = new HorizontalLayout();
        final Object[] productInfo =  new Object[5];
        final TextField product = new TextField("Product Name");
        productInfoLayout.addComponent(product);
        final TextField price = new TextField("Product Unit price");
        productInfoLayout.addComponent(price);
        final TextField qty = new TextField("Qty");
        productInfoLayout.addComponent(qty);
        Button addProduct = new Button("Add product");
        productInfoLayout.addComponent(addProduct);
        layout.addComponent(productInfoLayout);
        
        /*VerticalLayout billItemLayout = new VerticalLayout();
        billItemLayout.addComponent(billItems);
        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidth("100%");
        Label grossAmtLabel = new Label("Gross Amounts");
        footerLayout.addComponent(grossAmtLabel);
        footerLayout.setComponentAlignment(grossAmtLabel, Alignment.TOP_RIGHT);
        Label grossAmt = new Label("Gross Amount value");
        grossAmt.setValue("0.0");
        footerLayout.addComponent(grossAmt);
        footerLayout.setComponentAlignment(grossAmt, Alignment.TOP_RIGHT);
        billItemLayout.addComponent(footerLayout);
        layout.addComponent(billItemLayout);*/
        
        final Table billItems = new Table("Items list");
        billItems.setWidth("100%");
        billItems.addContainerProperty("SNo#", Integer.class, null);
        billItems.addContainerProperty("Product Description", String.class,  "");
        billItems.addContainerProperty("Unit price", Double.class, 0.0);
        billItems.addContainerProperty("Quantity", String.class, "1");
        billItems.addContainerProperty("Net Price", Double.class, 0.0);
        billItems.setPageLength(5);
        billItems.setFooterVisible(true);
        billItems.setColumnFooter("Quantity","Net Price");
        billItems.setColumnFooter("Net Price", "0.0");
        layout.addComponent(billItems);
        
        addProduct.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
            	//productInfo = new Object[] {product.getValue(),product.getValue()};
            	productInfo[0] = billItems.size()+1;
            	productInfo[1] = product.getValue();
            	productInfo[2] = Double.parseDouble(price.getValue());
            	productInfo[3] = qty.getValue();
            	productInfo[4] = Double.parseDouble(price.getValue())*Double.parseDouble(qty.getValue());
            	billItems.addItem(productInfo,billItems.size());
            	billItems.setCurrentPageFirstItemId(billItems.size()-billItems.getPageLength());
            	billItems.setColumnFooter("Net Price", String.valueOf(Double.parseDouble(billItems.getColumnFooter("Net Price"))+(Double)productInfo[4]));
            }
        });
        
        verSplitPanel.addComponent(layout);

        Label console = new Label();
        console.setValue("MongoDB collections: "+new MyMongoDB().printCollections());
        verSplitPanel.addComponent(console);
        
    }

}
