package in.retalemine;

import in.retalemine.component.BillingComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

public class AppUI extends UI {

	private static final long serialVersionUID = -4411379836207492227L;
	private static final Logger logger = LoggerFactory.getLogger(AppUI.class);

	@Override
	protected void init(VaadinRequest request) {
		setContent(new BillingComponent());
		logger.info("Completed setting layout");
	}

}
