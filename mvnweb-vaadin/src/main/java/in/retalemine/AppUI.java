package in.retalemine;

import in.retalemine.component.BillingComponent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

public class AppUI extends UI {

	private static final long serialVersionUID = -4411379836207492227L;

	@Override
	protected void init(VaadinRequest request) {
		setContent(new BillingComponent());
	}

}
