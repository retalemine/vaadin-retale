package in.retalemine.view.component;

import in.retalemine.util.BillingComputationUtil;
import in.retalemine.view.constants.BillingConstants;

import java.util.Collection;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class QuantityComponent extends Table {

	private static final long serialVersionUID = -3694515840057046220L;
	private final TextField quantityValue;
	private final ComboBox quantityUnits;
	private static final String VALUE = "value";
	private static final String UNITS = "units";

	public QuantityComponent(String caption, String value, Collection<?> units) {
		super(caption);
		quantityValue = new QuantityValue(value);
		quantityUnits = new QuantityUnits(units);

		addContainerProperty(VALUE, QuantityValue.class, null);
		addContainerProperty(UNITS, QuantityUnits.class, null);
		setColumnExpandRatio(VALUE, 1.0f);
		setColumnExpandRatio(UNITS, 2.0f);

		setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		setStyleName("r-table-border-less");
		setPageLength(size());
		setImmediate(true);
		setWidth("100%");

		addItem(new Object[] { quantityValue, quantityUnits }, new Integer(0));

	}

	@Override
	public Object getValue() {
		String value = ((QuantityValue) getItem(0).getItemProperty(VALUE)
				.getValue()).getValue();
		Object unit = ((QuantityUnits) getItem(0).getItemProperty(UNITS)
				.getValue()).getValue();
		if (null == value || null == unit) {
			return null;
		}
		return value + " " + unit;
	}

	private class QuantityValue extends TextField {
		private static final long serialVersionUID = 6517119978593629649L;

		private QuantityValue(String value) {
			super(null, value);
			setInputPrompt(BillingConstants.BOX_PROMPT_QUANTITY_VALUE);
			setConverter(new StringToDoubleConverter());
			setNullSelectionAllowed(true);
			setImmediate(true);
			setWidth("100%");
			setHeight("24px");

			addValueChangeListener(new Property.ValueChangeListener() {

				private static final long serialVersionUID = 2837254791166964894L;

				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					if (null != event.getProperty()) {
						if (null == event.getProperty().getValue()) {
							setValue("1");
						}
					}

				}
			});
		}

	}

	private class QuantityUnits extends ComboBox {

		private static final long serialVersionUID = -8194547645529843044L;

		private QuantityUnits(Collection<?> options) {
			super(null, options);
			setInputPrompt(BillingConstants.BOX_PROMPT_QUANTITY_UNIT);
			setFilteringMode(FilteringMode.CONTAINS);
			setNullSelectionAllowed(true);
			setImmediate(true);
			setWidth("100%");
			setNewItemsAllowed(true);
			setNewItemHandler(new NewItemHandler() {

				private static final long serialVersionUID = -4532254587935410919L;

				@Override
				public void addNewItem(String newItemCaption) {
					String unit = BillingComputationUtil
							.getValidUnit(newItemCaption);
					if (null != unit) {
						setValue(unit);
					}
				}
			});

		}
	}
}
