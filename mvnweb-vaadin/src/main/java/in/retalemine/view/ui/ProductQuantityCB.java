package in.retalemine.view.ui;

import in.retalemine.util.RegExUtil;
import in.retalemine.util.UnitUtil;
import in.retalemine.view.constants.UIconstants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.measure.Measure;
import javax.measure.converter.ConversionException;
import javax.measure.quantity.Quantity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ProductQuantityCB extends ComboBox {

	private static final long serialVersionUID = -466419943610903057L;
	private static final Logger logger = LoggerFactory
			.getLogger(ProductQuantityCB.class);

	private javax.measure.unit.Unit<? extends Quantity> unit;

	public ProductQuantityCB() {
		setWidth("100%");
		setPageLength(6);
		setInputPrompt(UIconstants.PROMPT_QUANTITY);
		setFilteringMode(FilteringMode.STARTSWITH);
		setRequired(true);
		setNullSelectionAllowed(true);
		// TODO check iscontiner actually used in code
		setContainerDataSource(new BeanItemContainer<Measure<Double, ? extends Quantity>>(
				Measure.class));
		setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID);
		setImmediate(true);
		addItemSetChangeListener(new ItemSetChangeListener() {
			private static final long serialVersionUID = 2122075138884519543L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				logger.info("Prod Quantity item set change {}",
						((ComboBox) event.getContainer())
								.getContainerDataSource());
			}
		});
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = -332009848701480764L;

			@Override
			public void addNewItem(String newQuantity) {
				String[] quantitySplit = null;
				javax.measure.unit.Unit<?> newUnit = null;
				Measure<Double, ? extends Quantity> quantity = null;
				try {
					quantity = Measure.valueOf(
							Double.parseDouble(newQuantity.trim()), unit);
					addItem(quantity);
					setValue(quantity);
				} catch (NumberFormatException e) {
					quantitySplit = RegExUtil.resolveQuantity(newQuantity);
					if (null != quantitySplit) {
						String validUnit = null;
						if (null != (validUnit = UnitUtil
								.getValidUnit(quantitySplit[1]))) {
							newUnit = javax.measure.unit.Unit
									.valueOf(validUnit);
							try {
								unit.getConverterTo(newUnit);
							} catch (ConversionException ce) {
								if(!UnitUtil.unitList.get(UnitUtil.unitList.size()-1).contains(newUnit.toString())){
									displayModal(Double
											.parseDouble(quantitySplit[0]));	
								}
							}
							quantity = Measure.valueOf(
									Double.parseDouble(quantitySplit[0]),
									newUnit);
							addItem(quantity);
							setValue(quantity);
						} else {
							displayModal(Double.parseDouble(quantitySplit[0]));
						}
					} else {
						displayModal(1);
					}
				}
			}

		});
	}

	protected void displayModal(double quantity) {
		Window sub = new Window("Enter Quantity and Unit");

		VerticalLayout vForm = new VerticalLayout();
		HorizontalLayout hContent = new HorizontalLayout();
		Button submit = new Button("Submit");

		Label quantityLB = new Label("Quantity : ");
		final TextField quantityTF = new TextField();
		final ComboBox quantityUnitCB = new ComboBox(null,
				UnitUtil.getValidAltUnitList(unit.toString()));

		quantityLB.setImmediate(false);

		quantityTF.setInputPrompt("Quantity");
		quantityTF.setWidth("100%");
		quantityTF.setConverter(new StringToDoubleConverter());
		quantityTF.setValue(String.valueOf(quantity));

		quantityUnitCB.setInputPrompt("Unit");
		quantityUnitCB.setFilteringMode(FilteringMode.CONTAINS);
		quantityUnitCB.setNullSelectionAllowed(true);
		quantityUnitCB.setWidth("100%");
		quantityUnitCB.setImmediate(true);
		quantityUnitCB.setNewItemsAllowed(true);
		quantityUnitCB.setNewItemHandler(new NewItemHandler() {

			private static final long serialVersionUID = 399742240123844605L;

			@Override
			public void addNewItem(String newUnit) {
				String validUnit = null;
				if (null != (validUnit = UnitUtil.getValidUnit(newUnit))) {
					Iterator<?> iterator = quantityUnitCB.getItemIds()
							.iterator();
					while (iterator.hasNext()) {
						String item = (String) iterator.next();
						if (item.equals(validUnit)) {
							quantityUnitCB.setValue(item);
							return;
						}
					}
				}
				quantityUnitCB.setValue(null);
			}
		});

		hContent.setImmediate(false);
		hContent.setWidth("100%");
		hContent.setMargin(false);
		hContent.setSpacing(true);
		hContent.addComponent(quantityLB);
		hContent.addComponent(quantityTF);
		hContent.addComponent(quantityUnitCB);
		hContent.setExpandRatio(quantityLB, .5f);
		hContent.setExpandRatio(quantityTF, .75f);
		hContent.setExpandRatio(quantityUnitCB, 1f);

		vForm.setImmediate(false);
		vForm.setWidth("100%");
		vForm.setMargin(true);
		vForm.setSpacing(true);
		vForm.addComponent(hContent);
		vForm.addComponent(submit);
		vForm.setComponentAlignment(submit, Alignment.MIDDLE_RIGHT);

		sub.setContent(vForm);
		sub.setModal(true);
		sub.setWidth("30%");
		sub.setResizable(false);
		sub.addActionHandler(new Handler() {

			private static final long serialVersionUID = 4470126167093872862L;
			Action actionEsc = new ShortcutAction("Close Modal Box",
					ShortcutAction.KeyCode.ESCAPE, null);

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (sender instanceof Window) {
					if (action == actionEsc) {
						((Window) sender).close();
					}
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return new Action[] { actionEsc };
			}
		});

		submit.setImmediate(true);
		submit.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 8518609553777583947L;

			@Override
			public void buttonClick(ClickEvent event) {
				Object parent;
				if ((parent = event.getComponent().getParent().getParent()) instanceof Window) {
					if (null != quantityTF.getValue()
							&& !quantityTF.getValue().trim().isEmpty()
							&& null != quantityUnitCB.getValue()
							&& !((String) quantityUnitCB.getValue()).trim()
									.isEmpty()) {
						((Window) parent).close();
						logger.info("window closed!! {}",quantityUnitCB.getValue());
						getNewItemHandler().addNewItem(
								quantityTF.getValue()
										+ quantityUnitCB.getValue());
					} else {
						// TODO display error msg to update every field
					}
				}
			}
		});

		UI.getCurrent().addWindow(sub);
		quantityTF.focus();
	}

	public void setUnit(javax.measure.unit.Unit<? extends Quantity> unit) {
		this.unit = unit;
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		logger.info("changeVariables {}", source);
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			logger.info("variables key= {}, value={}", entry.getKey(),
					entry.getValue());
		}
		String newFilter = ((String) variables.get("filter"));
		if (null != this.unit && null != newFilter
				&& !"".equals(newFilter.trim()) && !newFilter.endsWith(" ")) {
			try {
				getContainerDataSource().removeAllItems();
				getContainerDataSource().addItem(
						Measure.valueOf(Double.parseDouble(newFilter),
								this.unit));
				logger.info("new value suggested {}", newFilter);
			} catch (NumberFormatException e) {
				getContainerDataSource().removeAllItems();
				logger.info("new value not suggested for {}", newFilter);
			}
		}
		super.changeVariables(source, variables);
	}

	@Override
	protected List<?> getOptionsWithFilter(boolean needNullSelectOption) {
		logger.info("getOptionsWithFilter - needNullSelectOption - {}",
				needNullSelectOption);
		return super.getOptionsWithFilter(needNullSelectOption);
	}

	@Override
	protected List<?> getFilteredOptions() {
		logger.info("getFilteredOptions");
		return super.getFilteredOptions();
	}

	@Override
	protected Filter buildFilter(String filterString,
			FilteringMode filteringMode) {
		logger.info("buildFilter - filterString = {} - filteringMode = {} ",
				filterString, filteringMode);
		return super.buildFilter(filterString, filteringMode);
	}

	@Override
	public void setItemCaptionPropertyId(Object propertyId) {
		logger.info("setItemCaptionPropertyId {}", propertyId);
		super.setItemCaptionPropertyId(propertyId);
	}

}
