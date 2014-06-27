package in.retalemine.view.tryout.javascript.inline;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@JavaScript({ "timer.js" })
public class ExternalJS extends VerticalLayout {

	private static final long serialVersionUID = 5806841269818556979L;

	public ExternalJS() {
		Label date = new Label(
				"<div style=\"text-align: left;\"><span id=\"theDate\">00.00.0000</span></div>",
				ContentMode.HTML);
		Label time = new Label(
				"<div style=\"text-align: left;\"><span id=\"theTime\">00.00.0000</span></div>",
				ContentMode.HTML);
		Page.getCurrent().getJavaScript()
				.execute("setInterval(setTime, 1000);");

		addComponent(date);
		addComponent(time);
		addComponent(new Label("External"));
	}

}
