package in.retalemine.view.ui;

import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.VO.QuantityVO;
import in.retalemine.view.constants.UIconstants;
import in.retalemine.view.container.DummyProductContainer;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ProductUI extends CustomComponent implements ValueChangeListener,
		ClickListener {

	private static final long serialVersionUID = -8533819088700592639L;
	private static final Logger logger = LoggerFactory
			.getLogger(ProductUI.class);

	private ProductNameCB productNameCB;
	private ProductRateCB productRateCB;
	private ProductQuantityCB productQuantityCB;
	private CartBT cartBT;

	@SuppressWarnings("unchecked")
	public ProductUI() {
		HorizontalLayout layout = new HorizontalLayout();
		this.productNameCB = new ProductNameCB();
		this.productNameCB.addValueChangeListener((ValueChangeListener) this);
		this.productRateCB = new ProductRateCB();
		this.productRateCB.addValueChangeListener((ValueChangeListener) this);
		this.productQuantityCB = new ProductQuantityCB();
		this.productQuantityCB
				.addValueChangeListener((ValueChangeListener) this);
		this.cartBT = new CartBT();
		this.cartBT.addClickListener((ClickListener) this);

		setCompositionRoot(layout);

		layout.setImmediate(false);
		layout.setWidth("100%");
		layout.setMargin(false);
		layout.setSpacing(true);

		layout.addComponent(this.productNameCB);
		layout.addComponent(this.productRateCB);
		layout.addComponent(this.productQuantityCB);
		layout.addComponent(this.cartBT);

		layout.setExpandRatio(this.productNameCB, 5);
		layout.setExpandRatio(this.productRateCB, 1.5f);
		layout.setExpandRatio(this.productQuantityCB, 1.5f);
		layout.setExpandRatio(this.cartBT, 1f);

		layout.setComponentAlignment(this.productNameCB,
				Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(this.productRateCB,
				Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(this.productQuantityCB,
				Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(this.cartBT, Alignment.MIDDLE_CENTER);

		/**
		 * need a robust way to ensure lazy loading is implemented successfully
		 */
		((BeanItemContainer<ProductVO>) productNameCB.getContainerDataSource())
				.addAll(DummyProductContainer.DummyProductList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void valueChange(ValueChangeEvent event) {
		Property<?> property = event.getProperty();
		if (property instanceof ProductNameCB && null != property.getValue()) {
			ProductVO productVO = (ProductVO) property.getValue();
			Notification.show("Value change event",
					productVO.getProductDescription(), Type.TRAY_NOTIFICATION);
			logger.info("Value change event {}",
					productVO.getProductDescription());
			BeanItemContainer<Amount<Money>> rateContainer = (BeanItemContainer<Amount<Money>>) productRateCB
					.getContainerDataSource();
			rateContainer.removeAllItems();
			rateContainer.addAll(productVO.getUnitPrices());
			productRateCB.select(rateContainer.firstItemId());
			productQuantityCB.getContainerDataSource().removeAllItems();
			productQuantityCB.setUnit(productVO.getProductUnit().getUnit());
		} else if (property instanceof ProductQuantityCB
				&& null != property.getValue()) {
			QuantityVO quantity = (QuantityVO) property.getValue();
			Notification.show("Value change event", quantity.getQuantity()
					.toString(), Type.TRAY_NOTIFICATION);
			logger.info("Value change event {}", quantity.getQuantity());
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {

		final Object[] productInfo = new Object[5];

		if (UIconstants.ADD_TO_CART.equals(cartBT.getCaption())) {
			/*
			 * productInfo[0] = billableItemsTB.size() + 1; productInfo[1] =
			 * productNameCB.getValue(); productInfo[2] =
			 * productRateCB.getValue(); productInfo[3] = quantityTF.getValue()
			 * + " " + qtySuffixCB.getValue(); productInfo[4] = (Double)
			 * productRateCB.getValue()
			 * Double.parseDouble(quantityTF.getValue());
			 */
			// billableItemsTB.addItem(productInfo, billableItemIdSeq);
			// billableItemsTB.setCurrentPageFirstItemId(billableItemIdSeq++);
		} else {
			/*
			 * Item selectedItemId = billableItemsTB.getItem(billableItemsTB
			 * .getValue());
			 * selectedItemId.getItemProperty(PRODUCT_DESC).setValue(
			 * productNameCB.getValue());
			 * selectedItemId.getItemProperty(UNIT_RATE).setValue(
			 * productRateCB.getValue());
			 * selectedItemId.getItemProperty(QUANTITY).setValue(
			 * quantityTF.getValue() + " " + qtySuffixCB.getValue());
			 * selectedItemId.getItemProperty(AMOUNT).setValue( (Double)
			 * productRateCB.getValue()
			 * Double.parseDouble(quantityTF.getValue()));
			 */
		}
		// updateBillingPayments();
		// resetAddToCart();

	}

}
