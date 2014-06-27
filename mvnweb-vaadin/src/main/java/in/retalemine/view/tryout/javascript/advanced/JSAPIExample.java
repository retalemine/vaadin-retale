package in.retalemine.view.tryout.javascript.advanced;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class JSAPIExample extends VerticalLayout {
	private static final long serialVersionUID = -3205020480634478985L;
	String context;

	public JSAPIExample(String context) {
		if ("basic".equals(context))
			basic();
		else if ("status".equals(context))
			status();
		else if ("callbackbasic".equals(context))
			callbackbasic();
		else if ("callbackparameters".equals(context))
			callbackparameters();
		else if ("screendump".equals(context))
			screendump();

	}

	void basic() {
		Button execute = new Button("Execute", new ClickListener() {
			private static final long serialVersionUID = 4941615365448832738L;

			public void buttonClick(ClickEvent event) {
				Page.getCurrent().getJavaScript().execute("alert('Hello')");
			}
		});

		Button shorthand = new Button("Shorthand", new ClickListener() {
			private static final long serialVersionUID = 4941615365448832738L;

			public void buttonClick(ClickEvent event) {
				JavaScript.getCurrent().execute("alert('Hello')");
			}
		});
		addComponent(execute);
		addComponent(shorthand);
	}

	public static final String statusDescription = "<h1>Setting Status Message</h1>"
			+ "<p>Enter a message and observe the status bar of the browser.</p>"
			+ "<p><b>Note</b>: Modification of status messages must be enabled in the browser. "
			+ "For example in Firefox they are not, but must be set in "
			+ "<b>Preferences \u2192 Content \u2192 Enable JavaScript \u2192 Advanced \u2192 Change status bar text</b>.</p>";

	void status() {
		final TextField msgField = new TextField("Status Message");
		addComponent(msgField);

		Button set = new Button("Set Status");
		set.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 4941611508948832738L;

			public void buttonClick(ClickEvent event) {
				String message = (String) msgField.getValue();

				message = message.replace("'", "\\'");

				JavaScript.getCurrent().execute(
						"window.status = '" + message + "';");
			}
		});
		addComponent(set);

		set.setClickShortcut(KeyCode.ENTER);
		set.addStyleName("primary");
	}

	void callbackbasic() {
		JavaScript.getCurrent().addFunction("com.example.foo.myfunc",
				new JavaScriptFunction() {
					private static final long serialVersionUID = -2399933021928502854L;

					@Override
					public void call(JSONArray arguments) throws JSONException {
						Notification.show("Received call");
					}
				});

		Link link = new Link("Send Message", new ExternalResource(
				"javascript:com.example.foo.myfunc()"));
		addComponent(link);
	}

	void callbackparameters() {
		JavaScript.getCurrent().addFunction("com.example.foo.myfunc",
				new JavaScriptFunction() {
					private static final long serialVersionUID = -2399933021928502854L;

					@Override
					public void call(JSONArray arguments) throws JSONException {
						try {
							String message = arguments.getString(0);
							int value = arguments.getInt(1);
							Notification.show("Message: " + message
									+ ", value: " + value);
						} catch (JSONException e) {
							Notification.show("Error: " + e.getMessage());
						}
					}
				});

		Link link = new Link("Send Message", new ExternalResource(
				"javascript:com.example.foo.myfunc(prompt('Message'), 42)"));
		addComponent(link);
	}

	void screendump() {
		final Panel panel = new Panel("Dumped Content");
		panel.setWidth("640px");
		panel.setHeight("400px");
		final Label dumpContent = new Label();
		panel.setContent(dumpContent);
		addComponent(panel);

		Page.getCurrent().getJavaScript()
				.addFunction("dumpcontent", new JavaScriptFunction() {
					private static final long serialVersionUID = -2431634757915680123L;

					@Override
					public void call(JSONArray arguments) throws JSONException {
						dumpContent.setValue(arguments.getString(0));
					}
				});

		Button takeadump = new Button("Take a Dump");
		takeadump.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7000595589752593427L;

			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent()
						.getJavaScript()
						.execute(
								"dumpcontent(document.documentElement.innerHTML)");
			}
		});
		addComponent(takeadump);
	}
}
