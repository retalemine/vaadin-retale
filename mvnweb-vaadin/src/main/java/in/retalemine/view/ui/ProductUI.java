package in.retalemine.view.ui;

import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.VO.QuantityVO;
import in.retalemine.view.container.DummyProductContainer;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ProductUI extends CustomComponent implements ValueChangeListener {

	private static final long serialVersionUID = -8533819088700592639L;
	private static final Logger logger = LoggerFactory
			.getLogger(ProductUI.class);

	private ProductNameUI productNameUI;
	private ProductRateUI productRateUI;
	private ProductQuantityUI productQuantityUI;

	@SuppressWarnings("unchecked")
	public ProductUI(AbstractLayout layout) {
		this.productNameUI = new ProductNameUI();
		this.productNameUI.addValueChangeListener((ValueChangeListener) this);
		this.productRateUI = new ProductRateUI();
		this.productRateUI.addValueChangeListener((ValueChangeListener) this);
		this.productQuantityUI = new ProductQuantityUI();
		this.productQuantityUI
				.addValueChangeListener((ValueChangeListener) this);

		setCompositionRoot(layout);

		layout.setWidth("100%");
		layout.addComponent(this.productNameUI);
		layout.addComponent(this.productRateUI);
		layout.addComponent(this.productQuantityUI);

		/**
		 * need a robust way to ensure lazy loading is implemented successfully
		 */
		((BeanItemContainer<ProductVO>) productNameUI.getContainerDataSource())
				.addAll(DummyProductContainer.DummyProductList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void valueChange(ValueChangeEvent event) {
		Property<?> property = event.getProperty();
		if (property instanceof ProductNameUI && null != property.getValue()) {
			ProductVO productVO = (ProductVO) property.getValue();
			Notification.show("Value change event",
					productVO.getProductDescription(), Type.TRAY_NOTIFICATION);
			logger.info("Value change event {}",
					productVO.getProductDescription());
			BeanItemContainer<Amount<Money>> rateContainer = (BeanItemContainer<Amount<Money>>) productRateUI
					.getContainerDataSource();
			rateContainer.removeAllItems();
			rateContainer.addAll(productVO.getUnitPrices());
			productRateUI.select(rateContainer.firstItemId());
			productQuantityUI.getContainerDataSource().removeAllItems();
			productQuantityUI.setUnit(productVO.getProductUnit().getUnit());
		} else if (property instanceof ProductQuantityUI
				&& null != property.getValue()) {
			QuantityVO quantity = (QuantityVO) property.getValue();
			Notification.show("Value change event", quantity.getValue(),
					Type.TRAY_NOTIFICATION);
			logger.info("Value change event {}", quantity.getValue());
		}
	}

}
