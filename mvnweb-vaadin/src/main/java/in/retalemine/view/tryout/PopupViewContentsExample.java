package in.retalemine.view.tryout;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PopupViewContentsExample extends VerticalLayout {

	private static final long serialVersionUID = 7072882144894465194L;

	public PopupViewContentsExample() {
		setSpacing(true);

		Label content = new Label(
				"This is a simple Label component inside the popup. You can place any Vaadin components here.");
		content.setWidth("300px");
		PopupView popup = new PopupView("Static HTML content", content);
		addComponent(popup);

		popup = new PopupView(new PopupTextField());
		popup.setDescription("Click to edit");
		popup.setHideOnMouseOut(false);
		addComponent(popup);
	}

	public class PopupTextField implements PopupView.Content {

		private static final long serialVersionUID = -8749300854632724764L;
		private TextField tf = new TextField("Edit me");
		private VerticalLayout root = new VerticalLayout();

		public PopupTextField() {
			root.setSizeUndefined();
			root.setSpacing(true);
			root.setMargin(true);
			root.addComponent(new Label(
					"The changes made to any components inside the popup are reflected automatically when the popup is closed, but you might want to provide explicit action buttons for the user, like \"Save\" or \"Close\"."));
			root.addComponent(tf);
			tf.setValue("Initial dynamic content");
			tf.setWidth("300px");
		}

		public String getMinimizedValueAsHTML() {
			return tf.getValue().toString();
		}

		public Component getPopupComponent() {
			return root;
		}
	};
}