package in.retalemine.view.tryout.print.UI;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@Theme("retaletheme")
@Title("Printing")
public class PrintNewUI extends UI {

	private static final long serialVersionUID = 1844561070788343953L;

	@Override
	protected void init(VaadinRequest request) {
		setContent(new Label("<h1>Cooler</h1>\n"
				+ "<p>This is to be printed.</p>", ContentMode.HTML));

		JavaScript.getCurrent()
				.execute(
						"setTimeout(function() {"
								+ "  print(); self.close();}, 2000);");
	}
}