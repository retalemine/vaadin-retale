package in.retalemine.view.component;

import in.retalemine.util.RegExUtil;
import in.retalemine.util.RetaSI;
import in.retalemine.util.Rupee;
import in.retalemine.util.UnitUtil;
import in.retalemine.view.VO.BillItemVO;
import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.converter.AmountConverter;
import in.retalemine.view.ui.ProductQuantityCB;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

public class BillingComponent extends CustomComponent {

	private static final long serialVersionUID = -5001424944545200006L;

	static {
		RetaSI.getInstance();
	}

	private static final Logger logger = LoggerFactory
			.getLogger(BillingComponent.class);

	private VerticalLayout mainLayout;

	private Label billNoLB = new Label();
	private Integer billNoSeq = 1001;
	private DateField billDateDF = new DateField();

	private Table billableItemsTB = new Table();

	private Label subTotalValueLB = new Label();
	private Label totalValueLB = new Label();
	private HashMap<String, Double> taxPercentageMap = new HashMap<String, Double>() {

		private static final long serialVersionUID = -3348281343861653600L;

		{
			put("VAT", 4.0);
			put("Sales Tax", 5.0);
			put("Service Tax", 12.0);
		}
	};
	private ComboBox taxTypeCB = new ComboBox(null, taxPercentageMap.keySet());
	private Label taxValueLB = new Label();

	private ComboBox productNameCB = new ComboBox(
			null,
			Arrays.asList(new String[] { "Lux Sandal", "Hamam", "Cinthol Old" }));
	private ComboBox productRateCB = new ComboBox(null,
			Arrays.asList(new Double[] { 10.0, 20.0, 30.0, 40.0, 50.0 }));
	private ProductQuantityCB quantityCB = new ProductQuantityCB();
	private Button addToCartBT = new Button();
	private Button billMeBT = new Button();

	private TextField cusNameTF = new TextField();
	private TextField cusContactNoTF = new TextField();
	private TextArea cusAddressTA = new TextArea();

	private final String SUB_TOTAL = "SubTotal";
	private final String COLON = ":";
	private final String TOTAL = "Total";
	private final String TAX = "Tax";
	private final String PID_SERIAL_NO = "serialNo";
	private final String PID_PRODUCT_NAME = "productName";
	private final String PID_PRODUCT_UNIT = "productUnit";
	private final String PID_PRODUCT_DESCRIPTION = "productDescription";
	private final String PID_UNIT_RATE = "unitPrice";
	private final String PID_QUANTITY = "quantity";
	private final String PID_AMOUNT = "amount";
	private final String SERIAL_NO = "No.";
	private final String PRODUCT_NAME = "Product Name";
	private final String PRODUCT_UNIT = "Unit";
	private final String UNIT_RATE = "Unit Rate";
	private final String QUANTITY = "Quantity";
	private final String AMOUNT = "Amount";
	private final String DATE = "Date :";
	private final String ADD_TO_CART = "Add To Cart";
	private final String UPDATE_CART = "Update Cart";
	private final String PROMPT_PRODUCT_NAME = "Product Name";
	private final String PROMPT_PRODUCT_RATE = "Rate";
	private final String HOME_DELIVERY = "Home delivery";
	private final String BILL_ME = "Bill Me";
	private final String PAY_CASH = "Cash";
	private final String PAY_CHEQUE = "Cheque";
	private final String PAY_DELAYED = "Delayed";
	private final String CUSTOMER_NAME = "Customer Name";
	private final String CUSTOMER_CONTACT_NO = "Contact No.";
	private final String CUSTOMER_ADDRESS = "Address";
	private final Integer CUSTOMER_ADDRESS_ROWS = 2;

	public BillingComponent() {
		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		Component billingTableComponent;

		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		setWidth("100.0%");
		setHeight("100.0%");

		billingTableComponent = buildBillingTable();

		mainLayout.addComponent(buildBillingHeader());
		mainLayout.addComponent(buildAddToCart());
		mainLayout.addComponent(billingTableComponent);
		mainLayout.addComponent(buildBillingFooter());

		mainLayout.setExpandRatio(billingTableComponent, 1);

		return mainLayout;
	}

	private Component buildBillingFooter() {
		GridLayout footerGrid = new GridLayout(3, 1);

		footerGrid.setImmediate(false);
		footerGrid.setWidth("100%");
		footerGrid.setMargin(false);
		footerGrid.setSpacing(true);

		footerGrid.addComponent(buildCustomerProfile(), 0, 0, 1, 0);
		footerGrid.addComponent(buildBillingPayments(), 2, 0);

		return footerGrid;
	}

	private Component buildBillingPayments() {
		VerticalLayout paymentLayout = new VerticalLayout();
		HorizontalLayout subTotalLayout = new HorizontalLayout();
		HorizontalLayout taxLayout = new HorizontalLayout();
		HorizontalLayout totalLayout = new HorizontalLayout();
		Panel paymentModeLayout = new Panel("Payment Mode");
		HorizontalLayout deliveryBillMeLayout = new HorizontalLayout();
		Label subTotalLB = new Label();
		Label subTotalColonLB = new Label();
		Label totalLB = new Label();
		Label totalColonLB = new Label();
		Label taxColonLB = new Label();
		final OptionGroup payModeOG = new OptionGroup();
		final CheckBox deliveryChB = new CheckBox(HOME_DELIVERY);

		subTotalLB.setValue(SUB_TOTAL);
		subTotalValueLB.setConverter(new AmountConverter());
		subTotalValueLB
				.setPropertyDataSource(new ObjectProperty<Amount<Money>>(Amount
						.valueOf(0.0, Rupee.INR)));
		subTotalValueLB.setStyleName("v-align-right");
		subTotalColonLB.setValue(COLON);
		subTotalColonLB.setStyleName("v-align-right");

		totalLB.setValue(TOTAL);
		totalValueLB.setConverter(new AmountConverter());
		totalValueLB.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				Amount.valueOf(0.0, Rupee.INR)));
		totalValueLB.setStyleName("v-align-right");
		totalColonLB.setValue(COLON);
		totalColonLB.setStyleName("v-align-right");

		taxTypeCB.setInputPrompt(TAX);
		taxTypeCB.setFilteringMode(FilteringMode.CONTAINS);
		taxTypeCB.setNullSelectionAllowed(true);
		taxTypeCB.setWidth("100%");
		taxTypeCB.setImmediate(true);
		taxTypeCB.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = -2751845674814890015L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Amount<Money> subTotalVal = (Amount<Money>) subTotalValueLB
						.getPropertyDataSource().getValue();
				Amount<Money> taxVal = Amount.valueOf(0.0, Rupee.INR);
				if (null != event.getProperty().getValue()) {
					taxVal = subTotalVal.times(
							taxPercentageMap
									.get(event.getProperty().getValue()))
							.divide(100);
					taxValueLB.getPropertyDataSource().setValue(taxVal);
				} else {
					taxValueLB.getPropertyDataSource().setValue(taxVal);

				}
				totalValueLB.getPropertyDataSource().setValue(
						subTotalVal.plus(taxVal));
			}
		});

		taxValueLB.setConverter(new AmountConverter());
		taxValueLB.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				Amount.valueOf(0.0, Rupee.INR)));
		taxValueLB.setStyleName("v-align-right");
		taxColonLB.setValue(COLON);
		taxColonLB.setStyleName("v-align-right");

		payModeOG.addItem(PAY_CASH);
		payModeOG.addItem(PAY_CHEQUE);
		payModeOG.addItem(PAY_DELAYED);
		payModeOG.setMultiSelect(false);
		payModeOG.select(PAY_CASH);
		payModeOG.setWidth("100%");
		payModeOG.setImmediate(true);
		payModeOG.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 5654804808822582441L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (PAY_CHEQUE.equals(event.getProperty().getValue())) {
					mandateCustomerDetails(true);
				} else if (PAY_DELAYED.equals(event.getProperty().getValue())) {
					mandateCustomerDetails(true);
				} else if (PAY_CASH.equals(event.getProperty().getValue())) {
					if (!deliveryChB.getValue()) {
						mandateCustomerDetails(false);
					}

				}
			}
		});

		deliveryChB.setImmediate(true);
		deliveryChB.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 5867647913560616744L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (PAY_CASH.equals(payModeOG.getValue())) {
					mandateCustomerDetails((Boolean) event.getProperty()
							.getValue());
				}
			}
		});

		billMeBT.setCaption(BILL_ME);
		billMeBT.setEnabled(false);
		billMeBT.setImmediate(true);
		billMeBT.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 8093769158458975223L;

			@Override
			public void buttonClick(ClickEvent event) {
				billNoLB.setValue(String.valueOf(billNoSeq++));
				billDateDF.setValue(new Date());
				resetAddToCart();
				billableItemsTB.getContainerDataSource().removeAllItems();
				updateBillingPayments(null, 0);
				payModeOG.select(PAY_CASH);
				deliveryChB.setValue(false);
			}
		});

		paymentLayout.addComponent(subTotalLayout);
		paymentLayout.addComponent(taxLayout);
		paymentLayout.addComponent(totalLayout);
		paymentLayout.addComponent(paymentModeLayout);
		paymentLayout.addComponent(deliveryBillMeLayout);

		subTotalLayout.addComponent(subTotalLB);
		subTotalLayout.addComponent(subTotalColonLB);
		subTotalLayout.addComponent(subTotalValueLB);

		taxLayout.addComponent(taxTypeCB);
		taxLayout.addComponent(taxColonLB);
		taxLayout.addComponent(taxValueLB);

		totalLayout.addComponent(totalLB);
		totalLayout.addComponent(totalColonLB);
		totalLayout.addComponent(totalValueLB);

		paymentModeLayout.setContent(payModeOG);

		deliveryBillMeLayout.addComponent(deliveryChB);
		deliveryBillMeLayout.addComponent(billMeBT);

		subTotalLayout.setComponentAlignment(subTotalLB, Alignment.BOTTOM_LEFT);
		subTotalLayout.setComponentAlignment(subTotalColonLB,
				Alignment.BOTTOM_CENTER);
		subTotalLayout.setComponentAlignment(subTotalValueLB,
				Alignment.BOTTOM_RIGHT);
		subTotalLayout.setImmediate(false);
		subTotalLayout.setWidth("100%");
		subTotalLayout.setMargin(false);
		subTotalLayout.setSpacing(true);

		taxLayout.setComponentAlignment(taxTypeCB, Alignment.BOTTOM_LEFT);
		taxLayout.setComponentAlignment(taxColonLB, Alignment.BOTTOM_CENTER);
		taxLayout.setComponentAlignment(taxValueLB, Alignment.BOTTOM_RIGHT);
		taxLayout.setImmediate(false);
		taxLayout.setWidth("100%");
		taxLayout.setMargin(false);
		taxLayout.setSpacing(true);

		totalLayout.setComponentAlignment(totalLB, Alignment.BOTTOM_LEFT);
		totalLayout
				.setComponentAlignment(totalColonLB, Alignment.BOTTOM_CENTER);
		totalLayout.setComponentAlignment(totalValueLB, Alignment.BOTTOM_RIGHT);
		totalLayout.setImmediate(false);
		totalLayout.setWidth("100%");
		totalLayout.setMargin(false);
		totalLayout.setSpacing(true);

		paymentModeLayout.setStyleName(Runo.PANEL_LIGHT);
		paymentModeLayout.setImmediate(false);
		paymentModeLayout.setWidth("100%");

		deliveryBillMeLayout.setComponentAlignment(deliveryChB,
				Alignment.MIDDLE_LEFT);
		deliveryBillMeLayout.setComponentAlignment(billMeBT,
				Alignment.MIDDLE_RIGHT);
		deliveryBillMeLayout.setImmediate(false);
		deliveryBillMeLayout.setWidth("100%");
		deliveryBillMeLayout.setMargin(false);
		deliveryBillMeLayout.setSpacing(true);

		paymentLayout.setImmediate(false);
		paymentLayout.setWidth("100%");
		paymentLayout.setMargin(true);
		paymentLayout.setSpacing(true);

		return paymentLayout;
	}

	protected void mandateCustomerDetails(boolean isRequired) {
		cusNameTF.setRequired(isRequired);
		cusContactNoTF.setRequired(isRequired);
		cusAddressTA.setRequired(isRequired);
	}

	private Component buildCustomerProfile() {
		Panel customerPanel = new Panel();
		VerticalLayout customerVLayout = new VerticalLayout();
		FormLayout customerForm = new FormLayout();

		cusNameTF.setCaption(CUSTOMER_NAME);
		cusNameTF.setWidth("50%");
		cusContactNoTF.setCaption(CUSTOMER_CONTACT_NO);
		cusContactNoTF.setWidth("50%");
		cusAddressTA.setCaption(CUSTOMER_ADDRESS);
		cusAddressTA.setWidth("60%");
		cusAddressTA.setRows(CUSTOMER_ADDRESS_ROWS);

		customerForm.addComponent(cusNameTF);
		customerForm.addComponent(cusContactNoTF);
		customerForm.addComponent(cusAddressTA);

		customerVLayout.addComponent(customerForm);
		customerPanel.setContent(customerVLayout);

		customerVLayout.setImmediate(false);
		customerVLayout.setWidth("100%");
		customerVLayout.setMargin(true);
		customerVLayout.setSpacing(true);

		customerPanel.setImmediate(false);
		customerPanel.setWidth("100%");

		return customerPanel;
	}

	private Component buildBillingTable() {
		Panel billingPanel = new Panel();

		billableItemsTB.addContainerProperty(PID_SERIAL_NO, Integer.class,
				null, SERIAL_NO, null, null);
		billableItemsTB.addContainerProperty(PID_PRODUCT_NAME, String.class,
				"", PRODUCT_NAME, null, null);
		billableItemsTB.addContainerProperty(PID_PRODUCT_UNIT, Measure.class,
				0.0, PRODUCT_UNIT, null, null);
		billableItemsTB.addContainerProperty(PID_UNIT_RATE, Amount.class, 0.0,
				UNIT_RATE, null, Align.RIGHT);
		billableItemsTB.addContainerProperty(PID_QUANTITY,
				javax.measure.unit.Unit.class, "1", QUANTITY, null, null);
		billableItemsTB.addContainerProperty(PID_AMOUNT, Amount.class, 0.0,
				AMOUNT, null, Align.RIGHT);

		billableItemsTB
				.setContainerDataSource(new BeanItemContainer<BillItemVO<? extends Quantity, ? extends Quantity>>(
						BillItemVO.class));

		billableItemsTB.setVisibleColumns(new Object[] { PID_SERIAL_NO,
				PID_PRODUCT_NAME, PID_PRODUCT_UNIT, PID_UNIT_RATE,
				PID_QUANTITY, PID_AMOUNT });

		billableItemsTB.setColumnExpandRatio(PID_SERIAL_NO, 1);
		billableItemsTB.setColumnExpandRatio(PID_PRODUCT_NAME, 18);
		billableItemsTB.setColumnExpandRatio(PID_PRODUCT_UNIT, 4);
		billableItemsTB.setColumnExpandRatio(PID_UNIT_RATE, 4);
		billableItemsTB.setColumnExpandRatio(PID_QUANTITY, 4);
		billableItemsTB.setColumnExpandRatio(PID_AMOUNT, 6);

		billableItemsTB.setSizeFull();
		billableItemsTB.setSelectable(true);
		billableItemsTB.setImmediate(true);

		billableItemsTB
				.addValueChangeListener(new Property.ValueChangeListener() {

					private static final long serialVersionUID = 1578053093846509122L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event.getProperty().getValue()) {
							// TODO
							// Item selectedItem = billableItemsTB.getItem(event
							// .getProperty().getValue());
							// productNameCB.setValue(selectedItem
							// .getItemProperty(PRODUCT_DESC).getValue());
							// productRateCB.setValue(selectedItem
							// .getItemProperty(UNIT_RATE).getValue());
							// quantityCB.setValue(selectedItem.getItemProperty(
							// QUANTITY).getValue());
							// addToCartBT.setCaption(UPDATE_CART);
							// addToCartBT.setEnabled(false);
							// billableItemsTB.focus();
						} else {
							resetAddToCart();
						}
					}
				});

		billableItemsTB
				.addItemSetChangeListener(new Container.ItemSetChangeListener() {

					private static final long serialVersionUID = 8595891627053109722L;

					@Override
					public void containerItemSetChange(ItemSetChangeEvent event) {
						if (event.getContainer().size() > 0) {
							billMeBT.setEnabled(true);
						} else {
							billMeBT.setEnabled(false);
						}
					}
				});

		billingPanel.setImmediate(true);
		billingPanel.addActionHandler(new Handler() {

			private static final long serialVersionUID = -6411275870895883440L;
			Action actionDel = new ShortcutAction("Delete Item",
					ShortcutAction.KeyCode.DELETE, null);

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (target instanceof Table) {
					if (action == actionDel) {
						updateBillingPayments(
								((BillItemVO<?, ?>) billableItemsTB.getValue())
										.getAmount(), -1);
						billableItemsTB.getContainerDataSource().removeItem(
								billableItemsTB.getValue());
						reOrderBillingSNo();
					}
				}
			}

			@SuppressWarnings("unchecked")
			private void reOrderBillingSNo() {
				Integer serialNoSeq = 1;
				for (Iterator<?> i = billableItemsTB.getContainerDataSource()
						.getItemIds().iterator(); i.hasNext();) {
					billableItemsTB.getContainerDataSource().getItem(i.next())
							.getItemProperty(PID_SERIAL_NO)
							.setValue(serialNoSeq++);
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return new Action[] { actionDel };
			}
		});

		billingPanel.setSizeFull();
		billingPanel.setContent(billableItemsTB);

		return billingPanel;
	}

	@SuppressWarnings("unchecked")
	protected void updateBillingPayments(Amount<Money> rate, int mode) {

		Amount<Money> subTotalVal = (Amount<Money>) subTotalValueLB
				.getPropertyDataSource().getValue();

		if (null == rate) {
			subTotalVal = subTotalVal.times(0.0);
		} else if (mode == 1) {
			subTotalVal = subTotalVal.plus(rate);
		} else {
			subTotalVal = subTotalVal.minus(rate);
		}

		subTotalValueLB.getPropertyDataSource().setValue(subTotalVal);

		if (null == taxTypeCB.getValue()) {
			totalValueLB.getPropertyDataSource().setValue(subTotalVal);
		} else {
			Amount<Money> taxVal = subTotalVal.times(
					taxPercentageMap.get(taxTypeCB.getValue())).divide(100);
			taxValueLB.getPropertyDataSource().setValue(taxVal);
			totalValueLB.getPropertyDataSource().setValue(
					subTotalVal.plus(taxVal));
		}
	}

	private Component buildAddToCart() {
		HorizontalLayout addToCartLayout = new HorizontalLayout();
		Property.ValueChangeListener addToCartVCListener;

		addToCartVCListener = new Property.ValueChangeListener() {

			private static final long serialVersionUID = 6161204006947720960L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Property<?> property = event.getProperty();
				if (property instanceof ComboBox && null != property.getValue()) {
					if (property.getValue() instanceof ProductVO<?>) {
						ProductVO<? extends Quantity> productVO = (ProductVO<? extends Quantity>) property
								.getValue();
						Notification.show("Value change event",
								productVO.getProductDescription(),
								Type.TRAY_NOTIFICATION);
						logger.info("Value change event {}",
								productVO.getProductDescription());
						BeanItemContainer<Amount<Money>> rateContainer = (BeanItemContainer<Amount<Money>>) productRateCB
								.getContainerDataSource();
						rateContainer.removeAllItems();
						logger.info("price list {}", productVO.getUnitPrices());
						if (null != productVO.getUnitPrices()) {
							logger.info("inside price");
							rateContainer.addAll(productVO.getUnitPrices());
							productRateCB.select(rateContainer.firstItemId());
						}
						quantityCB.getContainerDataSource().removeAllItems();
						quantityCB
								.setUnit(productVO.getProductUnit().getUnit());
					}
				}
				if (null != productNameCB.getValue()) {
					if (null != productRateCB.getValue()
							&& null != quantityCB.getValue()) {
						logger.info("Cart BT enabled");
						addToCartBT.setEnabled(true);
						addToCartBT.focus();
					} else {
						logger.info("Cart BT disabled");
						addToCartBT.setEnabled(false);
					}
				} else {
					BeanItemContainer<Amount<Money>> rateContainer = (BeanItemContainer<Amount<Money>>) productRateCB
							.getContainerDataSource();
					rateContainer.removeAllItems();
					productRateCB.setValue(null);
					logger.info("Cart BT disabled and rate reset");
					addToCartBT.setEnabled(false);
				}
			}
		};

		productNameCB.setInputPrompt(PROMPT_PRODUCT_NAME);
		productNameCB.setFilteringMode(FilteringMode.CONTAINS);
		productNameCB.setWidth("100%");
		productNameCB.setRequired(true);
		productNameCB.setPageLength(10);
		productNameCB.setNullSelectionAllowed(true);
		productNameCB
				.setContainerDataSource(new BeanItemContainer<ProductVO<? extends Quantity>>(
						ProductVO.class));
		productNameCB
				.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
		productNameCB
				.setItemCaptionPropertyId(PID_PRODUCT_DESCRIPTION);
		productNameCB.setImmediate(true);
		productNameCB.setNewItemsAllowed(true);
		productNameCB.setNewItemHandler(new NewItemHandler() {

			private static final long serialVersionUID = -6825295673246553710L;

			@Override
			public void addNewItem(String newProductName) {
				String camelCasePName = RegExUtil
						.getCamelCaseString(newProductName);
				String[] result = RegExUtil.resolveProductUnit(camelCasePName);
				if (null != result) {
					String validUnit = null;
					if (null != (validUnit = UnitUtil.getValidUnit(result[1]))) {
						result[1] = validUnit;
						ProductVO<? extends Quantity> productVO = populateProductVO(result);
						productNameCB.getContainerDataSource().addItem(
								productVO);
						productNameCB.setValue(productVO);
						productRateCB.focus();
					} else {
						displayModal(result[2], Double.parseDouble(result[0]));
					}
				} else {
					displayModal(camelCasePName, 1.0);
				}
			}
		});
		productNameCB.addValueChangeListener(addToCartVCListener);

		productRateCB.setInputPrompt(PROMPT_PRODUCT_RATE);
		productRateCB.setFilteringMode(FilteringMode.STARTSWITH);
		productRateCB.setWidth("100%");
		productRateCB.setRequired(true);
		productRateCB.setPageLength(10);
		productRateCB.setNullSelectionAllowed(true);
		productRateCB
				.setContainerDataSource(new BeanItemContainer<Amount<Money>>(
						Amount.class));
		productRateCB.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID);
		productRateCB.setImmediate(true);
		productRateCB.setNewItemsAllowed(true);
		productRateCB.setNewItemHandler(new NewItemHandler() {

			private static final long serialVersionUID = -4362154011117158486L;

			@SuppressWarnings("unchecked")
			@Override
			public void addNewItem(String newRate) {
				try {
					Amount<Money> rate = Amount.valueOf(
							Double.parseDouble(newRate.trim()), Rupee.INR);
					productRateCB.addItem(rate);
					productRateCB.setValue(rate);

					BeanItemContainer<ProductVO<? extends Quantity>> prodContainer = (BeanItemContainer<ProductVO<? extends Quantity>>) productNameCB
							.getContainerDataSource();
					((List<Amount<Money>>) prodContainer
							.getItem(productNameCB.getValue())
							.getItemProperty("unitPrices").getValue())
							.add(rate);

					quantityCB.focus();
				} catch (NumberFormatException e) {
					Notification.show("Invalid Rate", newRate,
							Type.TRAY_NOTIFICATION);
					productRateCB.setValue(null);
					productRateCB.focus();
				}
			}
		});
		productRateCB.addValueChangeListener(addToCartVCListener);

		quantityCB.addValueChangeListener(addToCartVCListener);

		addToCartBT.setCaption(ADD_TO_CART);
		addToCartBT.setWidth("100%");
		addToCartBT.setImmediate(true);
		addToCartBT.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -6322775023239570121L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {

				if (ADD_TO_CART.equals(addToCartBT.getCaption())) {
					int serialNo = billableItemsTB.getContainerDataSource()
							.size() + 1;
					String productName = ((ProductVO<? extends Quantity>) productNameCB
							.getValue()).getProductName();
					Measure<Double, ? extends Quantity> productUnit = ((ProductVO<? extends Quantity>) productNameCB
							.getValue()).getProductUnit();
					String productDescription = ((ProductVO<? extends Quantity>) productNameCB
							.getValue()).getProductDescription();
					Amount<Money> unitPrice = ((Amount<Money>) productRateCB
							.getValue());
					Measure<Double, ? extends Quantity> quantity = (Measure<Double, ? extends Quantity>) quantityCB
							.getValue();
					BillItemVO<?, ?> bItem = BillItemVO.valueOf(serialNo,
							productName, productUnit, productDescription,
							unitPrice, quantity);
					billableItemsTB.getContainerDataSource().addItem(bItem);
					billableItemsTB.setCurrentPageFirstItemId(bItem);
					updateBillingPayments(bItem.getAmount(), 1);
				} else {
					// TODO
					// Item selectedItemId = billableItemsTB
					// .getItem(billableItemsTB.getValue());
					// selectedItemId.getItemProperty(PRODUCT_DESC).setValue(
					// productNameCB.getValue());
					// selectedItemId.getItemProperty(UNIT_RATE).setValue(
					// productRateCB.getValue());
					// selectedItemId.getItemProperty(QUANTITY).setValue(
					// quantityTF.getValue() + " "
					// + qtySuffixCB.getValue());
					// selectedItemId.getItemProperty(AMOUNT)
					// .setValue(
					// (Double) productRateCB.getValue()
					// * Double.parseDouble(quantityTF
					// .getValue()));
				}
				resetAddToCart();
			}

		});

		resetAddToCart();

		addToCartLayout.setImmediate(false);
		addToCartLayout.setWidth("100%");
		addToCartLayout.setMargin(false);
		addToCartLayout.setSpacing(true);

		addToCartLayout.addComponent(productNameCB);
		addToCartLayout.addComponent(productRateCB);
		addToCartLayout.addComponent(quantityCB);
		addToCartLayout.addComponent(addToCartBT);

		addToCartLayout.setExpandRatio(productNameCB, 5);
		addToCartLayout.setExpandRatio(productRateCB, 1.5f);
		addToCartLayout.setExpandRatio(quantityCB, 1.5f);
		addToCartLayout.setExpandRatio(addToCartBT, 1f);

		return addToCartLayout;
	}

	protected void displayModal(String prodName, Double quantity) {
		Window sub = new Window("Enter Product Name and Unit");

		VerticalLayout vForm = new VerticalLayout();
		HorizontalLayout hContent = new HorizontalLayout();
		Button submit = new Button("Submit");
		vForm.addComponent(hContent);
		vForm.addComponent(submit);

		VerticalLayout vLabel = new VerticalLayout();
		VerticalLayout vDelimiter = new VerticalLayout();
		VerticalLayout vData = new VerticalLayout();
		hContent.addComponent(vLabel);
		hContent.addComponent(vDelimiter);
		hContent.addComponent(vData);

		vLabel.addComponent(new Label("Product Name"));
		vLabel.addComponent(new Label("Quantity"));

		vDelimiter.addComponent(new Label(":"));
		vDelimiter.addComponent(new Label(":"));

		final TextField productNameTF = new TextField();
		HorizontalLayout hQuantity = new HorizontalLayout();
		final TextField quantityTF = new TextField();
		final ComboBox quantityUnitCB = new ComboBox(null,
				UnitUtil.getValidUnitList());
		hQuantity.addComponent(quantityTF);
		hQuantity.addComponent(quantityUnitCB);
		vData.addComponent(productNameTF);
		vData.addComponent(hQuantity);

		productNameTF.setInputPrompt("Enter Product Name");
		productNameTF.setWidth("100%");
		productNameTF.setValue(prodName.replaceAll("[0-9]*$", ""));

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

			private static final long serialVersionUID = -4532254587935410919L;

			@Override
			public void addNewItem(String newItemCaption) {
				String unit = UnitUtil.getValidUnit(newItemCaption);
				if (null != unit) {
					quantityUnitCB.setValue(unit);
				}
			}
		});

		hQuantity.setImmediate(false);
		hQuantity.setWidth("100%");
		hQuantity.setMargin(false);
		hQuantity.setSpacing(true);
		hQuantity.setExpandRatio(quantityTF, 1.0f);
		hQuantity.setExpandRatio(quantityUnitCB, 2.0f);

		vData.setImmediate(false);
		vData.setWidth("100%");
		vData.setMargin(false);
		vData.setSpacing(true);

		vDelimiter.setImmediate(false);
		vDelimiter.setWidth("100%");
		vDelimiter.setMargin(false);
		vDelimiter.setSpacing(true);

		vLabel.setImmediate(false);
		vLabel.setWidth("100%");
		vLabel.setMargin(false);
		vLabel.setSpacing(true);

		hContent.setImmediate(false);
		hContent.setWidth("100%");
		hContent.setMargin(false);
		hContent.setSpacing(true);
		hContent.setComponentAlignment(vLabel, Alignment.MIDDLE_LEFT);
		hContent.setComponentAlignment(vDelimiter, Alignment.MIDDLE_RIGHT);
		hContent.setComponentAlignment(vData, Alignment.MIDDLE_LEFT);
		hContent.setExpandRatio(vLabel, 5.0f);
		hContent.setExpandRatio(vDelimiter, 1.0f);
		hContent.setExpandRatio(vData, 5.0f);

		vForm.setImmediate(false);
		vForm.setWidth("100%");
		vForm.setMargin(true);
		vForm.setSpacing(true);
		vForm.setComponentAlignment(submit, Alignment.MIDDLE_RIGHT);

		sub.setContent(vForm);
		sub.setModal(true);
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
					if (null != productNameTF.getValue()
							&& !productNameTF.getValue().trim().isEmpty()
							&& null != quantityTF.getValue()
							&& !quantityTF.getValue().trim().isEmpty()
							&& null != quantityUnitCB.getValue()
							&& !((String) quantityUnitCB.getValue()).trim()
									.isEmpty()) {
						((Window) parent).close();
						productNameCB.getNewItemHandler().addNewItem(
								productNameTF.getValue()
										+ quantityTF.getValue()
										+ quantityUnitCB.getValue());
					} else {
						// TODO display error msg to update every field
					}
				}
			}
		});

		UI.getCurrent().addWindow(sub);
		productNameTF.focus();
	}

	protected ProductVO<? extends Quantity> populateProductVO(String[] result) {
		return ProductVO.valueOf(result[2], Measure.valueOf(
				Double.parseDouble(result[0]),
				javax.measure.unit.Unit.valueOf(result[1])), null);
	}

	protected void resetAddToCart() {
		productNameCB.setValue(null);
		productRateCB.setValue(null);
		quantityCB.setValue(null);
		addToCartBT.setEnabled(false);
		addToCartBT.setCaption(ADD_TO_CART);
		productNameCB.focus();
	}

	private Component buildBillingHeader() {
		AbsoluteLayout billingHeaderLayout = new AbsoluteLayout();
		Label logoLB = new Label();
		FormLayout dateFL = new FormLayout();
		TextField dateTF = new TextField(DATE);

		logoLB.setValue("Retale(M)ine Billing Solution");
		logoLB.setWidth("100%");
		billDateDF.setValue(new Date());
		dateTF.setPropertyDataSource(billDateDF);
		dateTF.setReadOnly(true);
		dateTF.setWidth("100%");

		dateFL.setImmediate(false);
		dateFL.setWidth("100%");
		dateFL.setMargin(false);
		dateFL.setSpacing(false);
		dateFL.addComponent(dateTF);
		dateFL.setComponentAlignment(dateTF, Alignment.MIDDLE_RIGHT);

		billingHeaderLayout.setImmediate(false);
		billingHeaderLayout.setWidth("100%");
		billingHeaderLayout.setHeight("30px");
		billingHeaderLayout.addComponent(logoLB, "left: 0px; top: 0px;");
		billingHeaderLayout.addComponent(dateFL, "right: 0px; top: 0px;");

		return billingHeaderLayout;
	}
}
