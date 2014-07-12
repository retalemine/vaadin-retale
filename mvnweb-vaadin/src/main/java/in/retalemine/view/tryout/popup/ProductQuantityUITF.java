package in.retalemine.view.tryout.popup;

import in.retalemine.view.constants.UIconstants;
import in.retalemine.view.tryout.DummyTableData;

import com.vaadin.data.Property;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ProductQuantityUITF extends VerticalLayout {

	private static final long serialVersionUID = -3355425856963483825L;
	private TextField tf = new TextField();
	private Table table = new Table(null,
			DummyTableData.createDummyDatasource());

	public ProductQuantityUITF() {

		table.setSelectable(true);
		table.setVisibleColumns(DummyTableData.getVisibleColumnsList());
		final PopupView popup = new PopupView("Small", table);

		tf.setInputPrompt(UIconstants.PROMPT_QUANTITY);
		tf.setImmediate(true);
		tf.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = -7331971790077682727L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				popup.setPopupVisible(true);
			}
		});

		addComponent(popup);
		addComponent(tf);

	}

}
