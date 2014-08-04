package in.retalemine.view.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class CartBuilder extends CustomComponent {

	private static final long serialVersionUID = -6367178892792317224L;
	private static final Logger logger = LoggerFactory
			.getLogger(CartBuilder.class);
	private final EventBus eventBus;
	private HorizontalLayout cartHLayout;
	private ProductComboBox productCB;
	private RateComboBox rateCB;
	private QuantityComboBox quantityCB;
	private CartButton cartBT;

	public CartBuilder(final EventBus eventBus) {

		logger.info("Initializing {}", getClass().getSimpleName());

		this.eventBus = null != eventBus ? eventBus : new EventBus();

		cartHLayout = new HorizontalLayout();
		productCB = new ProductComboBox(this.eventBus);
		rateCB = new RateComboBox(this.eventBus);
		quantityCB = new QuantityComboBox(this.eventBus);
		cartBT = new CartButton(this.eventBus);

		cartHLayout.setImmediate(false);
		cartHLayout.setWidth("100%");
		cartHLayout.setMargin(false);
		cartHLayout.setSpacing(true);

		cartHLayout.addComponent(productCB);
		cartHLayout.addComponent(rateCB);
		cartHLayout.addComponent(quantityCB);
		cartHLayout.addComponent(cartBT);

		cartHLayout.setExpandRatio(productCB, 5);
		cartHLayout.setExpandRatio(rateCB, 1.5f);
		cartHLayout.setExpandRatio(quantityCB, 1.5f);
		cartHLayout.setExpandRatio(cartBT, 1f);

		setCompositionRoot(cartHLayout);

		this.eventBus.register(productCB);
		this.eventBus.register(rateCB);
		this.eventBus.register(quantityCB);
		this.eventBus.register(cartBT);
	}

}
