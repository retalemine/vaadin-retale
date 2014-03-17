package in.retalemine.view;

import in.retalemine.view.ui.ProductUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

public class AppUI extends UI {

	private static final long serialVersionUID = -4411379836207492227L;
	private static final Logger logger = LoggerFactory.getLogger(AppUI.class);

	@Override
	protected void init(VaadinRequest request) {
		// setContent(new BillingComponent());
		setContent(new ProductUI(new HorizontalLayout()));
		logger.info("Completed setting layout");
	}

}
