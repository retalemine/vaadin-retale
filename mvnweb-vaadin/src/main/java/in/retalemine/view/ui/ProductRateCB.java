package in.retalemine.view.ui;

import in.retalemine.view.constants.UIconstants;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ProductRateCB extends ComboBox {

	private static final long serialVersionUID = -6557115445568992868L;
	private static final Logger logger = LoggerFactory
			.getLogger(ProductRateCB.class);

	public ProductRateCB() {
		setWidth("100%");
		setInputPrompt(UIconstants.PROMPT_PRODUCT_RATE);
		setFilteringMode(FilteringMode.STARTSWITH);
		setPageLength(10);
		setNullSelectionAllowed(true);
		setContainerDataSource(new BeanItemContainer<Amount<Money>>(
				Amount.class));
		setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID);
		setImmediate(true);
		// setNewProductHandler();
		addItemSetChangeListener(new ItemSetChangeListener() {
			private static final long serialVersionUID = -4938169787284240599L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				logger.info("rate item set change {}", ((ComboBox) event
						.getContainer()).getContainerDataSource());
			}
		});
	}

	private void setNewProductHandler() {
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = -332009848701480764L;

			@Override
			public void addNewItem(String newRate) {
				try {
					Double parsedRate = Double.parseDouble(newRate.trim());
					addItem(parsedRate);
					setValue(parsedRate);
				} catch (NumberFormatException e) {
					Notification.show("Invalid Rate", newRate,
							Type.TRAY_NOTIFICATION);
					setValue(null);
					focus();
				}
			}
		});
	}

}
