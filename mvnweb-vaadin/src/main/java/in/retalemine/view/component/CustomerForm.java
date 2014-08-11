package in.retalemine.view.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;

public class CustomerForm extends FormLayout {

	private static final long serialVersionUID = -4175119032966737916L;
	private static final Logger logger = LoggerFactory
			.getLogger(CustomerForm.class);

	public CustomerForm() {
		logger.info("Initializing {}", getClass().getSimpleName());
		addComponent(new Label("CustomerDetails"));
	}

}
