package in.retalemine.view.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.vaadin.ui.HorizontalLayout;

public class BillingCartBuilder extends HorizontalLayout {

	private static final long serialVersionUID = -6367178892792317224L;
	private static final Logger logger = LoggerFactory
			.getLogger(BillingCartBuilder.class);
	private ProductComboBox productCB;
	private RateComboBox rateCB;
	private QuantityComboBox quantityCB;
	private CartButton cartBT;

	public BillingCartBuilder(final EventBus eventBus) {

		logger.info("Initializing {}", getClass().getSimpleName());

		setImmediate(false);
		setWidth("100%");
		setMargin(false);
		setSpacing(true);

		productCB = new ProductComboBox(eventBus);
		rateCB = new RateComboBox(eventBus);
		quantityCB = new QuantityComboBox(eventBus);
		cartBT = new CartButton(eventBus);

		addComponent(productCB);
		addComponent(rateCB);
		addComponent(quantityCB);
		addComponent(cartBT);

		setExpandRatio(productCB, 5);
		setExpandRatio(rateCB, 1.5f);
		setExpandRatio(quantityCB, 1.5f);
		setExpandRatio(cartBT, 1f);

		eventBus.register(productCB);
		eventBus.register(rateCB);
		eventBus.register(quantityCB);
		eventBus.register(cartBT);
	}
}
