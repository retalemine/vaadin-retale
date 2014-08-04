package in.retalemine.view.component;

import in.retalemine.util.BillingUnits;
import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.event.BillItemSelectionEvent;
import in.retalemine.view.event.CartSelectionEvent;
import in.retalemine.view.event.ProductSelectionEvent;
import in.retalemine.view.event.RateSelectionEvent;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class RateComboBox extends ComboBox {

	private static final long serialVersionUID = -1601377899000920083L;
	private static final Logger logger = LoggerFactory
			.getLogger(RateComboBox.class);
	private BeanItemContainer<Amount<Money>> container = new BeanItemContainer<Amount<Money>>(
			Amount.class);
	private Boolean isNew = false;
	private Boolean reset = true;

	public RateComboBox(final EventBus eventBus) {

		logger.info("Initializing {}", getClass().getSimpleName());
		setWidth("100%");
		setInputPrompt(BillingConstants.PROMPT_PRODUCT_RATE);
		setContainerDataSource(container);
		setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID);
		setPageLength(5);
		setFilteringMode(FilteringMode.STARTSWITH);
		setNullSelectionAllowed(true);
		setEnabled(false);
		setImmediate(true);
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {

			private static final long serialVersionUID = -7267527387724023614L;

			@SuppressWarnings("unchecked")
			@Override
			public void addNewItem(String newRate) {
				logger.info("{} addNewItem starts", getClass().getSimpleName());
				Amount<Money> rate = null;
				try {
					rate = Amount.valueOf(Double.parseDouble(newRate.trim()),
							BillingUnits.INR);
				} catch (NumberFormatException e) {
					rate = (Amount<Money>) Amount.valueOf(newRate);
				}
				if (null == rate) {
					// TODO handle invalid rate
					Notification.show("Invalid Rate", newRate,
							Type.TRAY_NOTIFICATION);
					setValue(null);
					focus();
				} else {
					if (!container.containsId(rate)) {
						container.addItem(rate);
						isNew = true;
					}
					setValue(rate);
				}
				logger.info("{} addNewItem ends", getClass().getSimpleName());
			}
		});
		addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = -7565049295788242588L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				logger.info("{} valueChange", getClass().getSimpleName());
				if (null != event.getProperty().getValue()) {
					@SuppressWarnings("unchecked")
					Amount<Money> rate = (Amount<Money>) getValue();
					logger.info("{} posts RateSelectionEvent", getClass()
							.getSimpleName());
					eventBus.post(new RateSelectionEvent(rate, isNew));
					isNew = false;
				}
			}
		});

	}

	private void resetRateComboBox() {
		container.removeAllItems();
		setValue(null);
		setEnabled(false);
		isNew = false;
		reset = true;
	}

	@Subscribe
	public void listenProductSelectionEvent(final ProductSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (reset) {
			container.removeAllItems();
			if (null != event.getProductVO().getUnitRates()) {
				container.addAll(event.getProductVO().getUnitRates());
				setValue(container.lastItemId());
			}
			focus();
		}
		setEnabled(true);
	}

	@Subscribe
	public void listenCartSelectionEvent(final CartSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		resetRateComboBox();
	}

	@Subscribe
	public void listenBillItemSelectionEvent(final BillItemSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (null != event.getBillItemVO()) {
			container.removeAllItems();
			container.addAll(event.getBillItemVO().getUnitRates());
			setValue(event.getBillItemVO().getUnitRate());
			reset = false;
		} else {
			resetRateComboBox();
		}
	}

}
