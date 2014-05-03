package in.retalemine.view.ui;

import in.retalemine.view.constants.UIconstants;

import com.vaadin.ui.Button;

public class CartBT extends Button {

	private static final long serialVersionUID = 685524012882788938L;

	public CartBT() {
		setWidth("100%");
		setCaption(UIconstants.ADD_TO_CART);
		setImmediate(true);
	}

}
