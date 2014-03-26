package in.retalemine.view.ui;

import in.retalemine.view.VO.QuantityVO;
import in.retalemine.view.constants.UIconstants;

import java.util.List;
import java.util.Map;

import javax.measure.quantity.Quantity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;

public class ProductQuantityUI extends ComboBox {

	private static final long serialVersionUID = -466419943610903057L;
	private static final Logger logger = LoggerFactory
			.getLogger(ProductQuantityUI.class);

	private javax.measure.unit.Unit<? extends Quantity> unit;

	public ProductQuantityUI() {
		setSizeFull();
		setPageLength(6);
		setInputPrompt(UIconstants.PROMPT_QUANTITY);
		setFilteringMode(FilteringMode.STARTSWITH);
		setNullSelectionAllowed(true);
		setContainerDataSource(new BeanItemContainer<QuantityVO>(
				QuantityVO.class));
		setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
		setItemCaptionPropertyId("value");
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
				QuantityVO quantityVO = new QuantityVO(newQuantity);
				getContainerDataSource().addItem(quantityVO);
				setValue(quantityVO);
				logger.info("new value set {}", newQuantity);
			}
		});
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
				int value = Integer.parseInt(newFilter);
				getContainerDataSource().removeAllItems();
				getContainerDataSource().addItem(
						new QuantityVO(value + " " + this.unit));
				logger.info("new value suggested {}", value);
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
