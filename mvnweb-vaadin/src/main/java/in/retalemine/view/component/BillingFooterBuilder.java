package in.retalemine.view.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.vaadin.ui.HorizontalLayout;

public class BillingFooterBuilder extends HorizontalLayout {
	private static final long serialVersionUID = 7336427744288513111L;
	private static final Logger logger = LoggerFactory
			.getLogger(BillingFooterBuilder.class);
	private CustomerForm customerForm;
	private BillingAmountPanel amountPanel;

	public BillingFooterBuilder(final EventBus eventBus) {
		logger.info("Initializing {}", getClass().getSimpleName());

		setImmediate(false);
		setWidth("100%");
		setMargin(false);
		setSpacing(true);

		customerForm = new CustomerForm();
		amountPanel = new BillingAmountPanel(eventBus);

		addComponent(customerForm);
		addComponent(amountPanel);

		setExpandRatio(customerForm, 2f);
		setExpandRatio(amountPanel, 1f);

		eventBus.register(customerForm);
		eventBus.register(amountPanel);
	}

}
