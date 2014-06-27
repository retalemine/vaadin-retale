package in.retalemine.view.tryout.javascript;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

@JavaScript({ "mylibrary.js", "mycomponent-connector.js" })
public class MyComponent extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 269748434495164213L;

	ArrayList<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

	public MyComponent() {
		addFunction("onClick", new JavaScriptFunction() {
			private static final long serialVersionUID = 1256984845028849243L;

			@Override
			public void call(JSONArray arguments) throws JSONException {
				getState().setValue(arguments.getString(0));
				for (ValueChangeListener listener : listeners)
					listener.valueChange();
			}
		});
	}

	void addListener(ValueChangeListener listener) {
		listeners.add(listener);
	}

	public void setValue(String value) {
		getState().setValue(value);
		markAsDirty();
	}

	public String getValue() {
		return getState().getValue();
	}

	@Override
	public MyComponentState getState() {
		return (MyComponentState) super.getState();
	}

	public interface ValueChangeListener extends Serializable {
		void valueChange();
	}
}
