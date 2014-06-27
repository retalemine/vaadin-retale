package in.retalemine.view.tryout.javascript;

import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class JSIntegrationExample extends VerticalLayout {
	private static final long serialVersionUID = -6235574698344484522L;

	public JSIntegrationExample() {
		final MyComponent mycomponent = new MyComponent();
		mycomponent.setValue("Server-side value");
		mycomponent.addListener(new MyComponent.ValueChangeListener() {
			private static final long serialVersionUID = -984941325364460695L;

			@Override
			public void valueChange() {
				Notification.show("New value: " + mycomponent.getValue());
			}
		});
		addComponent(mycomponent);
	}
}
