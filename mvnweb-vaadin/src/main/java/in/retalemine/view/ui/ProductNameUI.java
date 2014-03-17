package in.retalemine.view.ui;

import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.constants.UIconstants;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;

public class ProductNameUI extends ComboBox {

	private static final long serialVersionUID = 6595774216366213098L;
	private static final Logger logger = LoggerFactory
			.getLogger(ProductNameUI.class);

	public ProductNameUI() {
		setSizeFull();
		setPageLength(15);
		setInputPrompt(UIconstants.PROMPT_PRODUCT_NAME);
		setFilteringMode(FilteringMode.CONTAINS);
		setNullSelectionAllowed(true);
		setContainerDataSource(new BeanItemContainer<ProductVO>(ProductVO.class));
		setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
		setItemCaptionPropertyId("productDescription");
		setImmediate(true);
		// setNewProductHandler();
		addItemSetChangeListener(new ItemSetChangeListener() {

			private static final long serialVersionUID = 2122075138884519543L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				logger.info("Prod Name item set change {}", ((ComboBox) event
						.getContainer()).getContainerDataSource());
			}
		});
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		logger.info("changeVariables {}", source);
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			logger.info("variables key= {}, value={}", entry.getKey(),
					entry.getValue());
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
	protected Filter buildFilter(String filterString,
			FilteringMode filteringMode) {
		logger.info("buildFilter - filterString = {} - filteringMode = {} ",
				filterString, filteringMode);
		return super.buildFilter(filterString, filteringMode);
	}

	private void setNewProductHandler() {
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = -5815947094191981101L;

			@Override
			public void addNewItem(String newProductName) {
				StringBuffer camelCasePName = new StringBuffer();
				Matcher camelCaseMatcher = Pattern.compile("([a-z])([a-z]*)",
						Pattern.CASE_INSENSITIVE).matcher(
						newProductName.trim().replaceAll("\\s+", " "));
				while (camelCaseMatcher.find()) {
					camelCaseMatcher.appendReplacement(camelCasePName,
							camelCaseMatcher.group(1).toUpperCase()
									+ camelCaseMatcher.group(2).toLowerCase());
				}
				camelCaseMatcher.appendTail(camelCasePName);
				ProductVO productVO = new ProductVO();
				productVO.setProductName(camelCasePName.toString());
				productVO.setProductDescription(camelCasePName.toString());
				addItem(productVO);
				// setValue(camelCasePName.toString());
				logger.info("Add new value {}", newProductName);
			}
		});
	}

}
