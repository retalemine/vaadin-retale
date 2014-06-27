package in.retalemine.view.tryout.javascript.inline;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class InlineJS extends VerticalLayout {

	private static final long serialVersionUID = 5806841269818556979L;

	public InlineJS() {
		Label date = new Label(
				"<div style=\"text-align: left;\"><span id=\"theDate\">00.00.0000</span></div>",
				ContentMode.HTML);
		Label time = new Label(
				"<div style=\"text-align: left;\"><span id=\"theTime\">00.00.0000</span></div>",
				ContentMode.HTML);

		StringBuilder script = new StringBuilder();

		script.append("function setTime(){");
		script.append("var c = new Date();");
		script.append("var date = fm(c.getDate()) + \".\" + fm(c.getMonth()+1) + \".\" + (c.getYear() + 1900);");
		script.append("var time = c.getHours() + ':' + fm(c.getMinutes()) + ':' + fm(c.getSeconds());");
		script.append("document.getElementById('theDate').innerHTML= date;");
		script.append("document.getElementById('theTime').innerHTML= time;");
		script.append("}");
		script.append("function fm(num) { if (num < 10) { return '0' + num; } return num; }");
		// script.append("setInterval(\"setTime()\", 1000);");
		script.append("setInterval(setTime, 1000);");
		JavaScript.getCurrent().execute(script.toString());

		addComponent(date);
		addComponent(time);
	}

}
