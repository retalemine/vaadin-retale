package in.retalemine.view.component;

import in.retalemine.view.VO.CustomerVO;
import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.event.DeliveryModeSelectionEvent;
import in.retalemine.view.event.PaymentModeSelectionEvent;
import in.retalemine.view.event.ResetBillingEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class CustomerForm extends FormLayout {

	private static final long serialVersionUID = -4175119032966737916L;
	private static final Logger logger = LoggerFactory
			.getLogger(CustomerForm.class);

	private final PropertysetItem customerInfo;
	private final FieldGroup binder;
	private final TextField customerName;
	private final TextField contactNumber;
	private final TextArea addressLines;
	private Boolean isNonCashPaymentMode = false;
	private Boolean isHomeDelivery = false;

	public CustomerForm() {
		logger.info("Initializing {}", getClass().getSimpleName());

		customerInfo = new PropertysetItem();
		customerInfo.addItemProperty(BillingConstants.CUSTOMER_NAME,
				new ObjectProperty<String>(""));
		customerInfo.addItemProperty(BillingConstants.CUSTOMER_CONTACT_NO,
				new ObjectProperty<String>(""));
		customerInfo.addItemProperty(BillingConstants.CUSTOMER_ADDRESS,
				new ObjectProperty<String>(""));

		customerName = new TextField(BillingConstants.CUSTOMER_NAME);
		contactNumber = new TextField(BillingConstants.CUSTOMER_CONTACT_NO);
		addressLines = new TextArea(BillingConstants.CUSTOMER_ADDRESS);

		customerName.setWidth("90%");
		customerName.setImmediate(true);
		contactNumber.setWidth("90%");
		contactNumber.setImmediate(true);
		addressLines.setWidth("90%");
		addressLines.setRows(BillingConstants.ADDRESS_ROWS_COUNT);
		addressLines.setImmediate(true);

		binder = new FieldGroup(customerInfo);
		binder.bind(customerName, BillingConstants.CUSTOMER_NAME);
		binder.bind(contactNumber, BillingConstants.CUSTOMER_CONTACT_NO);
		binder.bind(addressLines, BillingConstants.CUSTOMER_ADDRESS);
		binder.setBuffered(false);

		addComponent(customerName);
		addComponent(contactNumber);
		addComponent(addressLines);

		setImmediate(true);
		setWidth("100%");
		setMargin(false);
		setSpacing(false);

	}

	public PropertysetItem getCustomerPropertysetItem() {
		return customerInfo;
	}

	public void mandateCustomerDetails(boolean isRequired) {
		customerName.setRequired(isRequired);
		contactNumber.setRequired(isRequired);
		addressLines.setRequired(isRequired);
	}

	public static CustomerVO buildCustomerVO(PropertysetItem propertysetItem) {
		Object value = null;
		Object customerName = null;
		Object customerNumber = null;
		Object customerAddress = null;

		customerName = null != (value = propertysetItem.getItemProperty(
				BillingConstants.CUSTOMER_NAME).getValue())
				&& !String.valueOf(value).trim().isEmpty() ? value : null;
		customerNumber = null != (value = propertysetItem.getItemProperty(
				BillingConstants.CUSTOMER_CONTACT_NO).getValue())
				&& !String.valueOf(value).trim().isEmpty() ? value : null;
		customerAddress = null != (value = propertysetItem.getItemProperty(
				BillingConstants.CUSTOMER_ADDRESS).getValue())
				&& !String.valueOf(value).trim().isEmpty() ? value : null;
		if (null != customerName || null != customerNumber
				|| null != customerAddress) {
			if (null != customerNumber) {
				return new CustomerVO((String) customerName,
						Integer.parseInt((String) customerNumber),
						(String) customerAddress);
			} else {
				return new CustomerVO((String) customerName, null,
						(String) customerAddress);
			}
		}
		return null;
	}

	@Subscribe
	public void listenPaymentModeSelectionEvent(
			final PaymentModeSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (BillingConstants.PAY_CASH.equals(event.getPaymentMode())) {
			isNonCashPaymentMode = false;
		} else {
			isNonCashPaymentMode = true;
		}
		mandateCustomerDetails(isNonCashPaymentMode || isHomeDelivery);
	}

	@Subscribe
	public void listenDeliveryModeSelectionEvent(
			final DeliveryModeSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		isHomeDelivery = event.getIsDoorDelivery();
		mandateCustomerDetails(isNonCashPaymentMode || isHomeDelivery);
	}

	@SuppressWarnings("unchecked")
	@Subscribe
	public void listenResetBillingEvent(final ResetBillingEvent event) {
		logger.info("Event - {} : handler - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName());
		for (Object id : customerInfo.getItemPropertyIds()) {
			customerInfo.getItemProperty(id).setValue("");
		}
		isNonCashPaymentMode = false;
		isHomeDelivery = false;
	}
}
