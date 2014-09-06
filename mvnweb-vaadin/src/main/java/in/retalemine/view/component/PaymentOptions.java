package in.retalemine.view.component;

import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.event.PaymentModeSelectionEvent;
import in.retalemine.view.event.ResetBillingEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.ui.OptionGroup;

public class PaymentOptions extends OptionGroup {
	private static final long serialVersionUID = -655019496744157719L;
	private static final Logger logger = LoggerFactory
			.getLogger(PaymentOptions.class);

	public PaymentOptions(final EventBus eventBus) {
		super(BillingConstants.PAY_MODE);
		logger.info("Initializing {}", getClass().getSimpleName());

		addItem(BillingConstants.PAY_CASH);
		addItem(BillingConstants.PAY_CHEQUE);
		addItem(BillingConstants.PAY_DELAYED);
		select(BillingConstants.PAY_CASH);
		setMultiSelect(false);
		setWidth("100%");
		setImmediate(true);
		addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 4500649840768905500L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				logger.info("{} posts PaymentModeSelectionEvent", getClass()
						.getSimpleName());
				eventBus.post(new PaymentModeSelectionEvent((String) event
						.getProperty().getValue()));
			}
		});

		setItemEnabled(BillingConstants.PAY_CHEQUE, false);
		setItemEnabled(BillingConstants.PAY_DELAYED, false);
	}

	@Subscribe
	public void listenResetBillingEvent(final ResetBillingEvent event) {
		logger.info("Event - {} : handler - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName());
		select(BillingConstants.PAY_CASH);
	}
}
