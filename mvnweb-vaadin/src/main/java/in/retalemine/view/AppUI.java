package in.retalemine.view;

import in.retalemine.view.component.BillingComponent;
import in.retalemine.view.tryout.PopupViewContentsExample;
import in.retalemine.view.tryout.PopupViewExample;
import in.retalemine.view.tryout.ProductQuantityUITF;
import in.retalemine.view.tryout.SubWindowExample;
import in.retalemine.view.tryout.blackboard.BlackBoardExampleApp;
import in.retalemine.view.ui.ProductUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class AppUI extends UI {

	private static final long serialVersionUID = -4411379836207492227L;
	private static final Logger logger = LoggerFactory.getLogger(AppUI.class);

	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setSpacing(true);
		vLayout.setWidth("100%");

		String profile = request.getParameter("profile");
		logger.info("profile {}", profile);

		if ("bcomp".equals(profile)) {
			setContent(new BillingComponent());
		} else if ("tryout".equals(profile)) {
			String type = request.getParameter("type");
			String context = request.getParameter("context");
			logger.info("Type {}", type);
			logger.info("Context {}", context);
			if ("blackboard".equals(type)) {
				new BlackBoardExampleApp();
			} else if ("popup".equals(type)) {
				vLayout.addComponent(new PopupViewContentsExample());
				vLayout.addComponent(new PopupViewExample());
				vLayout.addComponent(new ProductQuantityUITF());
			} else if ("subwindow".equals(type)) {
				vLayout.addComponent(new SubWindowExample(context));
			}
			setContent(vLayout);
		} else {
			vLayout.addComponent(new ProductUI());
			setContent(vLayout);
		}
		logger.info("Completed setting layout");
	}
}
