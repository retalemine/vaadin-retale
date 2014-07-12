package in.retalemine.view;

import in.retalemine.view.component.BillingComponent;
import in.retalemine.view.tryout.beancontainer.BeanContainerEx;
import in.retalemine.view.tryout.beancontainer.BeanContainerIDResolverEx;
import in.retalemine.view.tryout.blackboard.BlackBoardExampleApp;
import in.retalemine.view.tryout.browser.BrowserInformationExample;
import in.retalemine.view.tryout.javascript.JSIntegrationExample;
import in.retalemine.view.tryout.javascript.advanced.JSAPIExample;
import in.retalemine.view.tryout.javascript.inline.ExternalJS;
import in.retalemine.view.tryout.javascript.inline.InlineJS;
import in.retalemine.view.tryout.popup.PopupViewContentsExample;
import in.retalemine.view.tryout.popup.PopupViewExample;
import in.retalemine.view.tryout.popup.ProductQuantityUITF;
import in.retalemine.view.tryout.print.PrintingExample;
import in.retalemine.view.tryout.print.jcprint.JcPrintingExample;
import in.retalemine.view.tryout.window.SubWindowExample;
import in.retalemine.view.ui.ProductUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("retaletheme")
@PreserveOnRefresh
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
			} else if ("beancontainer".equals(type)) {
				vLayout.addComponent(new BeanContainerEx());
				vLayout.addComponent(new BeanContainerIDResolverEx());
			} else if ("browserinfo".equals(type)) {
				vLayout.addComponent(new BrowserInformationExample());
			} else if ("javascript".equals(type)) {
				vLayout.addComponent(new JSIntegrationExample());
			} else if ("advjavascript".equals(type)) {
				vLayout.addComponent(new JSAPIExample(context));
			} else if ("inlinejavascript".equals(type)) {
				vLayout.addComponent(new InlineJS());
			} else if ("externaljavascript".equals(type)) {
				vLayout.addComponent(new ExternalJS());
			} else if ("print".equals(type)) {
				vLayout.addComponent(new PrintingExample(context));
			} else if ("jcprint".equals(type)) {
				vLayout.setHeight("100%");
				vLayout.addComponent(new JcPrintingExample());
			}
			setContent(vLayout);
		} else {
			vLayout.addComponent(new ProductUI());
			setContent(vLayout);
		}
		logger.info("Completed setting layout");
	}
}
