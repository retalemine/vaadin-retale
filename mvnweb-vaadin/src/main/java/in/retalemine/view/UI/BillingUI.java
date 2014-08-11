package in.retalemine.view.UI;

import in.retalemine.util.BillingUnits;
import in.retalemine.view.component.BillingCartBuilder;
import in.retalemine.view.component.BillingFooterBuilder;
import in.retalemine.view.component.BillingHeaderBuilder;
import in.retalemine.view.component.BillingTable;

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
	private BillingHeaderBuilder billingHeader;
	private BillingCartBuilder billingCart;
	private BillingTable billingTable;
	private BillingFooterBuilder billingFooter;

	static {
		BillingUnits.getInstance();
	}

	@Override
	protected void init(VaadinRequest request) {

		logger.info("Initializing {}", getClass().getSimpleName());

		setWidth("100.0%");
		setHeight("100.0%");

		billingVLayout = new VerticalLayout();
		billingHeader = new BillingHeaderBuilder();
		billingCart = new BillingCartBuilder(eventBus);
		billingTable = new BillingTable(eventBus);
		billingFooter = new BillingFooterBuilder(eventBus);

		billingVLayout.setImmediate(false);
		billingVLayout.setWidth("100%");
		billingVLayout.setHeight("100%");
		billingVLayout.setMargin(true);
		billingVLayout.setSpacing(true);

		billingVLayout.addComponent(billingHeader);
		billingVLayout.addComponent(billingCart);
		billingVLayout.addComponent(billingTable);
		billingVLayout.addComponent(billingFooter);

		billingVLayout.setExpandRatio(billingTable, 1.f);

		setContent(billingVLayout);

		eventBus.register(billingTable);
	}
}
