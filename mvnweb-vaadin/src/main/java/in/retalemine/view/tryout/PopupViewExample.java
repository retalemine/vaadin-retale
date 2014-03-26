package in.retalemine.view.tryout;

import com.vaadin.data.Property;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class PopupViewExample extends CustomComponent {
	private static final long serialVersionUID = 9106115858126838561L;

	public PopupViewExample() {
		AbsoluteLayout layout = new AbsoluteLayout();
		TextField tf = new TextField();
		Table table = new Table(null, DummyTableData.createDummyDatasource());
		final PopupView popup = new PopupView(null, table);

		layout.setWidth("200px");
		layout.setHeight("50px");
		layout.addComponent(tf, "left: 0px; top: 0px;");
		layout.addComponent(popup, "left: 0px; top: 100px;");

		table.setSelectable(true);
		table.setVisibleColumns(DummyTableData.getVisibleColumnsList());

		tf.setImmediate(true);
		tf.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = -7331971790077682727L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				popup.setPopupVisible(true);
			}
		});

		ComponentPosition tfpos = layout.getPosition(tf);
		ComponentPosition poppos = layout.getPosition(popup);
		poppos.setTopValue(tfpos.getTopValue());

		setCompositionRoot(layout);
	}

}
