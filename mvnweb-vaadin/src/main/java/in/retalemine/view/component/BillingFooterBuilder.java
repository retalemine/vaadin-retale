package in.retalemine.view.component;

import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.event.DeliveryModeSelectionEvent;
import in.retalemine.view.event.ResetBillingEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class BillingFooterBuilder extends HorizontalLayout {
	private static final long serialVersionUID = 7336427744288513111L;
	private static final Logger logger = LoggerFactory
			.getLogger(BillingFooterBuilder.class);
	private final CustomerForm customerForm;

	private final VerticalLayout optionsLayout;
	private final PaymentOptions paymentOptions;
	private final DeliveryOption deliveryOption;

	private final VerticalLayout verticalLayout;
	private final BillingAmountPanel amountPanel;
	private final BillProcessing billProcessing;

	public BillingFooterBuilder(final EventBus eventBus) {
		logger.info("Initializing {}", getClass().getSimpleName());

		setImmediate(false);
		setWidth("100%");
		setMargin(false);
		setSpacing(true);

		customerForm = new CustomerForm();
		paymentOptions = new PaymentOptions(eventBus);
		deliveryOption = new DeliveryOption(eventBus);
		amountPanel = new BillingAmountPanel(eventBus);
		billProcessing = new BillProcessing(eventBus,
				customerForm.getCustomerPropertysetItem());

		optionsLayout = new VerticalLayout();
		optionsLayout.addComponent(paymentOptions);
		optionsLayout.addComponent(deliveryOption);
		optionsLayout.setWidth("100%");
		optionsLayout.setSpacing(true);
		optionsLayout.setMargin(false);
		optionsLayout.setImmediate(false);

		verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(amountPanel);
		verticalLayout.addComponent(billProcessing);
		verticalLayout.setWidth("100%");
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(false);
		verticalLayout.setImmediate(false);

		addComponent(customerForm);
		addComponent(optionsLayout);
		addComponent(verticalLayout);

		setExpandRatio(customerForm, 2.5f);
		setExpandRatio(optionsLayout, 1f);
		setExpandRatio(verticalLayout, 2f);

		eventBus.register(customerForm);
		eventBus.register(paymentOptions);
		eventBus.register(deliveryOption);
		eventBus.register(amountPanel);
		eventBus.register(billProcessing);
	}

}

class DeliveryOption extends CheckBox {

	private static final long serialVersionUID = 5520268560631937179L;
	private static final Logger logger = LoggerFactory
			.getLogger(DeliveryOption.class);

	public DeliveryOption(final EventBus eventBus) {
		super(BillingConstants.HOME_DELIVERY);
		logger.info("Initializing {}", getClass().getSimpleName());
		setImmediate(true);
		addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = -258286270168005949L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				logger.info("{} posts DeliveryModeSelectionEvent", getClass()
						.getSimpleName());
				eventBus.post(new DeliveryModeSelectionEvent((Boolean) event
						.getProperty().getValue()));
			}
		});
	}

	@Subscribe
	public void listenResetBillingEvent(final ResetBillingEvent event) {
		logger.info("Event - {} : handler - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName());
		setValue(false);
	}
}
