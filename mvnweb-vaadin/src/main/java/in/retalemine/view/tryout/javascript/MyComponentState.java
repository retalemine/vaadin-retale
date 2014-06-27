package in.retalemine.view.tryout.javascript;

import com.vaadin.shared.ui.JavaScriptComponentState;

public class MyComponentState extends JavaScriptComponentState {

	private static final long serialVersionUID = 2193056609064546632L;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
