package in.retalemine.view.UI;

import in.retalemine.util.BillingUnits;
import in.retalemine.view.component.BillingTable;
import in.retalemine.view.component.CartBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("retaletheme")
public class BillingUI extends UI {

	private static final long serialVersionUID = -4411379836207492227L;
	private static final Logger logger = LoggerFactory
			.getLogger(BillingUI.class);
	private final EventBus eventBus = new EventBus();
	private VerticalLayout billingVLayout;
	private CartBuilder cartBuilder;
	private BillingTable billingTable;

	static {
		BillingUnits.getInstance();
	}

	@Override
	protected void init(VaadinRequest request) {

		logger.info("Initializing {}", getClass().getSimpleName());

		billingVLayout = new VerticalLayout();
		cartBuilder = new CartBuilder(eventBus);
		billingTable = new BillingTable(eventBus);

		billingVLayout.addComponent(cartBuilder);
		billingVLayout.addComponent(billingTable);

		setWidth("100.0%");
		setHeight("100.0%");

		billingVLayout.setImmediate(false);
		billingVLayout.setWidth("100%");
		billingVLayout.setHeight("100%");
		billingVLayout.setMargin(true);
		billingVLayout.setSpacing(true);

		billingVLayout.setExpandRatio(billingTable, 1.f);

		setContent(billingVLayout);

		eventBus.register(billingTable);
	}
}
