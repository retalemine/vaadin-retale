package in.retalemine.view.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@JavaScript({ "datetime.js" })
public class BillingHeaderBuilder extends HorizontalLayout {
	private static final long serialVersionUID = 585003085458113021L;
	private static final Logger logger = LoggerFactory
			.getLogger(BillingAmountPanel.class);
	private Label logoLB;
	private Label dateTimeLB;

	public BillingHeaderBuilder() {
		logger.info("Initializing {}", getClass().getSimpleName());

		setImmediate(false);
		setWidth("100%");
		setHeight("20px");
		setMargin(false);
		setSpacing(true);

		logoLB = new Label();
		dateTimeLB = new Label(
				"<div style=\"text-align: right;\"><span id=\"retaledatetime\"></span></div>",
				ContentMode.HTML);

		logoLB.setValue("Retale(M)ine Billing Solution");
		logoLB.setWidth("100%");

		addComponent(logoLB);
		addComponent(dateTimeLB);

		setComponentAlignment(logoLB, Alignment.TOP_LEFT);
		setComponentAlignment(dateTimeLB, Alignment.TOP_RIGHT);

		Page.getCurrent().getJavaScript()
				.execute("setInterval(displayDateTime, 1000);");
	}

}
