package in.retalemine.view.ui;

import in.retalemine.view.constants.UIconstants;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

public class CartTB extends Table {

	private static final long serialVersionUID = 849224757151191987L;

	public CartTB() {
		setSizeFull();
		setSelectable(true);
		setImmediate(true);

		addContainerProperty(UIconstants.SERIAL_NO, Integer.class, null);
		addContainerProperty(UIconstants.PRODUCT_DESC, String.class, "");
		addContainerProperty(UIconstants.UNIT_RATE, Double.class, 0.0);
		addContainerProperty(UIconstants.QUANTITY, String.class, "1");
		addContainerProperty(UIconstants.AMOUNT, Double.class, 0.0);

		setColumnAlignment(UIconstants.AMOUNT, Align.RIGHT);

		setColumnExpandRatio(UIconstants.SERIAL_NO, 1);
		setColumnExpandRatio(UIconstants.PRODUCT_DESC, 18);
		setColumnExpandRatio(UIconstants.UNIT_RATE, 4);
		setColumnExpandRatio(UIconstants.QUANTITY, 2);
		setColumnExpandRatio(UIconstants.AMOUNT, 6);

		addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 172879437178906284L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				if (null != event.getProperty().getValue()) {
					Item selectedItem = getItem(event.getProperty().getValue());
					focus();
				} else {
				}
			}
		});
	}

}
