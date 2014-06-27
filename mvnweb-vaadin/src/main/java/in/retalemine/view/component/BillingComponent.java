package in.retalemine.view.component;

import in.retalemine.util.RegExUtil;
import in.retalemine.util.RetaSI;
import in.retalemine.util.Rupee;
import in.retalemine.util.UnitUtil;
import in.retalemine.view.VO.BillItemVO;
import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.converter.AmountConverter;
import in.retalemine.view.ui.ProductQuantityCB;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SimpleTimeZone;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
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

	private DateField billDateDF = new DateField();
	private WebBrowser webBrowser = null;
	private SimpleTimeZone clientTZ = null;

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

	private ComboBox productNameCB = new ComboBox();
	private ComboBox productRateCB = new ComboBox();
	private ProductQuantityCB quantityCB = new ProductQuantityCB();
	private Button addToCartBT = new Button();
	private Button billMeBT = new Button();
	private Button resetBT = new Button();
	private OptionGroup payModeOG = new OptionGroup();
	private CheckBox deliveryChB = new CheckBox();

	private TextField cusNameTF = new TextField();
	private TextField cusContactNoTF = new TextField();
	private TextArea cusAddressTA = new TextArea();

	BeanContainer<String, ProductVO<? extends Quantity>> productNameCBCTR = new BeanContainer<String, ProductVO<? extends Quantity>>(
			ProductVO.class);

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
	private final String ADD_TO_CART = "Add To Cart";
	private final String UPDATE_CART = "Update Cart";
	private final String PROMPT_PRODUCT_NAME = "Product Name";
	private final String PROMPT_PRODUCT_RATE = "Rate";
	private final String HOME_DELIVERY = "Home delivery";
	private final String BILL_ME = "Bill Me";
	private final String RESET = "Reset";
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

	public static Currency getGlobalCurrency() {
		return Rupee.INR;
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
		GridLayout footerGrid = new GridLayout(5, 1);

		footerGrid.setImmediate(false);
		footerGrid.setWidth("100%");
		footerGrid.setMargin(false);
		footerGrid.setSpacing(true);

		footerGrid.addComponent(buildCustomerProfile(), 0, 0, 2, 0);
		footerGrid.addComponent(buildBillingPayments(), 3, 0, 4, 0);

		return footerGrid;
	}

	private Component buildBillingPayments() {
		VerticalLayout paymentLayout = new VerticalLayout();
		HorizontalLayout subTotalLayout = new HorizontalLayout();
		HorizontalLayout taxLayout = new HorizontalLayout();
		HorizontalLayout totalLayout = new HorizontalLayout();
		Panel paymentModeLayout = new Panel("Payment Mode");
		VerticalLayout paymentDeliveryLayout = new VerticalLayout();
		VerticalLayout BillMeLayout = new VerticalLayout();
		HorizontalLayout paymentDeliveryBillMeLayout = new HorizontalLayout();

		Label subTotalLB = new Label();
		Label subTotalColonLB = new Label();
		Label totalLB = new Label();
		Label totalColonLB = new Label();
		Label taxColonLB = new Label();

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
		payModeOG.setStyleName("v-select-optiongroup-horizontal");
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

		deliveryChB.setCaption(HOME_DELIVERY);
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
		billMeBT.setWidth("100%");
		billMeBT.setEnabled(false);
		billMeBT.setImmediate(true);
		billMeBT.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 8093769158458975223L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO validate mandatory fields
				// TODO based on payment mode respective modal box
				if (PAY_CASH.equals(payModeOG.getValue())) {
					displayCashModal();
				} else if (PAY_CHEQUE.equals(payModeOG.getValue())) {
					displayChequeModal();
				} else if (PAY_DELAYED.equals(payModeOG.getValue())) {
					displayDelayedModal();
				} else {
					Notification.show("Bill Me", "Invalid Payment Mode",
							Type.TRAY_NOTIFICATION);
				}
			}
		});

		resetBT.setCaption(RESET);
		resetBT.setWidth("100%");
		resetBT.setEnabled(false);
		resetBT.setImmediate(true);
		resetBT.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 9118398197062156254L;

			@Override
			public void buttonClick(ClickEvent event) {
				resetBillingComponent();
			}
		});

		paymentLayout.addComponent(subTotalLayout);
		paymentLayout.addComponent(taxLayout);
		paymentLayout.addComponent(totalLayout);
		paymentLayout.addComponent(paymentDeliveryBillMeLayout);

		subTotalLayout.addComponent(subTotalLB);
		subTotalLayout.addComponent(subTotalColonLB);
		subTotalLayout.addComponent(subTotalValueLB);

		taxLayout.addComponent(taxTypeCB);
		taxLayout.addComponent(taxColonLB);
		taxLayout.addComponent(taxValueLB);

		totalLayout.addComponent(totalLB);
		totalLayout.addComponent(totalColonLB);
		totalLayout.addComponent(totalValueLB);

		paymentDeliveryBillMeLayout.addComponent(paymentDeliveryLayout);
		paymentDeliveryBillMeLayout.addComponent(BillMeLayout);

		paymentDeliveryLayout.addComponent(paymentModeLayout);
		paymentDeliveryLayout.addComponent(deliveryChB);

		paymentModeLayout.setContent(payModeOG);

		BillMeLayout.addComponent(billMeBT);
		BillMeLayout.addComponent(resetBT);
		BillMeLayout.setComponentAlignment(billMeBT, Alignment.BOTTOM_RIGHT);
		BillMeLayout.setComponentAlignment(resetBT, Alignment.BOTTOM_RIGHT);

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

		paymentDeliveryLayout.setImmediate(false);
		paymentDeliveryLayout.setWidth("100%");
		paymentDeliveryLayout.setMargin(false);
		paymentDeliveryLayout.setSpacing(true);

		paymentModeLayout.setStyleName(Runo.PANEL_LIGHT);
		paymentModeLayout.setImmediate(false);
		paymentModeLayout.setWidth("100%");

		BillMeLayout.setImmediate(false);
		BillMeLayout.setWidth("100%");
		BillMeLayout.setHeight("");
		BillMeLayout.setMargin(false);
		BillMeLayout.setSpacing(true);

		paymentDeliveryBillMeLayout.setImmediate(false);
		paymentDeliveryBillMeLayout.setWidth("100%");
		paymentDeliveryBillMeLayout.setMargin(false);
		paymentDeliveryBillMeLayout.setSpacing(true);
		paymentDeliveryBillMeLayout.setExpandRatio(paymentDeliveryLayout, 2.0f);
		paymentDeliveryBillMeLayout.setExpandRatio(BillMeLayout, 1.0f);
		paymentDeliveryBillMeLayout.setComponentAlignment(BillMeLayout,
				Alignment.BOTTOM_RIGHT);

		paymentLayout.setImmediate(false);
		paymentLayout.setWidth("100%");
		paymentLayout.setMargin(true);
		paymentLayout.setSpacing(true);

		return paymentLayout;
	}

	protected void displayCashModal() {
		final Window sub = new Window("Cash Payment");

		VerticalLayout contentVL = new VerticalLayout();
		FormLayout cashFL = new FormLayout();
		final TextField billAmt = new TextField();
		final TextField amtReceived = new TextField();
		final TextField payBackAmt = new TextField();
		HorizontalLayout footerHL = new HorizontalLayout();
		Button saveDraft = new Button();
		Button printBill = new Button();

		billAmt.setCaption("Billable Amount");
		billAmt.setWidth("100%");
		billAmt.setStyleName("v-textfield-align-right");
		billAmt.setReadOnly(true);
		billAmt.setConverter(new AmountConverter());
		billAmt.setPropertyDataSource(totalValueLB.getPropertyDataSource());

		amtReceived.setCaption("Received Amount");
		amtReceived.setWidth("100%");
		amtReceived.setStyleName("v-textfield-align-right");
		amtReceived.setConverter(new AmountConverter());
		amtReceived.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				Amount.valueOf(0.0, Rupee.INR)));
		amtReceived.setImmediate(true);
		amtReceived.addFocusListener(new FocusListener() {

			private static final long serialVersionUID = -5687359956231689993L;

			@Override
			public void focus(FocusEvent event) {
				((TextField) event.getComponent()).setValue("");
			}
		});
		amtReceived.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 5355490604466827525L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event.getProperty().getValue()
						&& !((String) event.getProperty().getValue()).isEmpty()) {
					payBackAmt
							.getPropertyDataSource()
							.setValue(
									((Amount<Money>) amtReceived
											.getPropertyDataSource().getValue())
											.minus((Amount<Money>) billAmt
													.getPropertyDataSource()
													.getValue()));
				}

			}
		});

		payBackAmt.setCaption("Payback Amount");
		payBackAmt.setWidth("100%");
		payBackAmt.setStyleName("v-textfield-align-right");
		payBackAmt.setReadOnly(true);
		payBackAmt.setConverter(new AmountConverter());
		payBackAmt.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				Amount.valueOf(0.0, Rupee.INR)));

		saveDraft.setCaption("Save Draft");
		saveDraft.setSizeUndefined();
		saveDraft.setImmediate(true);
		saveDraft.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 3808358760477618923L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill as draft and don't print it
				// TODO do provision to save the draft and make a print also
				// TODO what will be the payment mode defaulted to?
				sub.close();
				resetBillingComponent();
			}
		});

		printBill.setCaption("Print Bill");
		printBill.setSizeUndefined();
		printBill.setImmediate(true);
		printBill.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -779726628843991503L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill and print it
				sub.close();
				resetBillingComponent();
			}
		});

		contentVL.addComponent(cashFL);
		contentVL.addComponent(footerHL);
		contentVL.setComponentAlignment(footerHL, Alignment.MIDDLE_RIGHT);

		cashFL.addComponent(billAmt);
		cashFL.addComponent(amtReceived);
		cashFL.addComponent(payBackAmt);

		footerHL.addComponent(saveDraft);
		footerHL.addComponent(printBill);
		footerHL.setComponentAlignment(saveDraft, Alignment.MIDDLE_RIGHT);
		footerHL.setComponentAlignment(printBill, Alignment.MIDDLE_RIGHT);

		cashFL.setImmediate(false);
		cashFL.setWidth("100%");
		cashFL.setMargin(false);
		cashFL.setSpacing(true);

		footerHL.setImmediate(false);
		footerHL.setSizeUndefined();
		footerHL.setMargin(false);
		footerHL.setSpacing(true);

		contentVL.setImmediate(false);
		contentVL.setWidth("100%");
		contentVL.setMargin(true);
		contentVL.setSpacing(true);

		sub.setContent(contentVL);
		sub.setModal(true);
		sub.setResizable(false);
		sub.addActionHandler(new Handler() {

			private static final long serialVersionUID = -5095434104534198849L;
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

		UI.getCurrent().addWindow(sub);

	}

	protected void displayChequeModal() {
		final Window sub = new Window("Cheque Payment");

		FormLayout chequeFL = new FormLayout();
		TextField bankName = new TextField();
		TextField chequeNumber = new TextField();
		final TextField billAmt = new TextField();
		final TextField amtReceived = new TextField();
		final TextField payBackAmt = new TextField();
		Button printBill = new Button();

		bankName.setCaption("Bank Name");
		bankName.setWidth("100%");
		bankName.setStyleName("v-textfield-align-right");

		chequeNumber.setCaption("Cheque Number#");
		chequeNumber.setWidth("100%");
		chequeNumber.setStyleName("v-textfield-align-right");

		billAmt.setCaption("Billable Amount");
		billAmt.setWidth("100%");
		billAmt.setStyleName("v-textfield-align-right");
		billAmt.setReadOnly(true);
		billAmt.setConverter(new AmountConverter());
		billAmt.setPropertyDataSource(totalValueLB.getPropertyDataSource());

		amtReceived.setCaption("Received Amount");
		amtReceived.setWidth("100%");
		amtReceived.setStyleName("v-textfield-align-right");
		amtReceived.setConverter(new AmountConverter());
		amtReceived.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				Amount.valueOf(0.0, Rupee.INR)));
		amtReceived.setImmediate(true);
		amtReceived.addFocusListener(new FocusListener() {

			private static final long serialVersionUID = 3216033202874236783L;

			@Override
			public void focus(FocusEvent event) {
				((TextField) event.getComponent()).setValue("");
			}
		});
		amtReceived.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 7908458548102263922L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event.getProperty().getValue()
						&& !((String) event.getProperty().getValue()).isEmpty()) {
					payBackAmt
							.getPropertyDataSource()
							.setValue(
									((Amount<Money>) amtReceived
											.getPropertyDataSource().getValue())
											.minus((Amount<Money>) billAmt
													.getPropertyDataSource()
													.getValue()));
				}

			}
		});

		payBackAmt.setCaption("Payback Amount");
		payBackAmt.setWidth("100%");
		payBackAmt.setStyleName("v-textfield-align-right");
		payBackAmt.setReadOnly(true);
		payBackAmt.setConverter(new AmountConverter());
		payBackAmt.setPropertyDataSource(new ObjectProperty<Amount<Money>>(
				Amount.valueOf(0.0, Rupee.INR)));

		printBill.setCaption("Print Bill");
		printBill.setSizeUndefined();
		printBill.setImmediate(true);
		printBill.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 535739329405714802L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill and print it
				sub.close();
				resetBillingComponent();
			}
		});

		chequeFL.addComponent(bankName);
		chequeFL.addComponent(chequeNumber);
		chequeFL.addComponent(billAmt);
		chequeFL.addComponent(amtReceived);
		chequeFL.addComponent(payBackAmt);
		chequeFL.addComponent(printBill);

		chequeFL.setImmediate(false);
		chequeFL.setWidth("100%");
		chequeFL.setMargin(true);
		chequeFL.setSpacing(true);

		sub.setContent(chequeFL);
		sub.setModal(true);
		sub.setResizable(false);
		sub.addActionHandler(new Handler() {

			private static final long serialVersionUID = 5855458178562827022L;
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

		UI.getCurrent().addWindow(sub);
	}

	protected void displayDelayedModal() {
		final Window sub = new Window("Delayed Payment");

		FormLayout delayedFL = new FormLayout();
		final TextField billAmt = new TextField();
		final DateField payableDate = new DateField();
		Date currentDate = null;
		Button printBill = new Button();

		billAmt.setCaption("Billable Amount");
		billAmt.setWidth("100%");
		billAmt.setReadOnly(true);
		billAmt.setConverter(new AmountConverter());
		billAmt.setPropertyDataSource(totalValueLB.getPropertyDataSource());

		payableDate.setCaption("Payable Date");
		payableDate.setWidth("100%");
		payableDate.setDateFormat("dd-MMM-yyyy (E)");
		payableDate.setImmediate(true);
		payableDate.setRangeStart((currentDate = new Date()));
		payableDate.setRangeEnd(new Date(currentDate.getTime() + 2592000000L));
		payableDate.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -5135380705893758953L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event.getProperty()) {
				}
			}
		});

		printBill.setCaption("Print Bill");
		printBill.setSizeUndefined();
		printBill.setImmediate(true);
		printBill.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 9049648993181705643L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO save the bill and print it
				sub.close();
				resetBillingComponent();
			}
		});

		delayedFL.addComponent(billAmt);
		delayedFL.addComponent(payableDate);
		delayedFL.addComponent(printBill);

		delayedFL.setImmediate(false);
		delayedFL.setWidth("100%");
		delayedFL.setMargin(true);
		delayedFL.setSpacing(true);

		sub.setContent(delayedFL);
		sub.setModal(true);
		sub.setResizable(false);
		sub.addActionHandler(new Handler() {

			private static final long serialVersionUID = 5779658659989871382L;
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

		UI.getCurrent().addWindow(sub);
	}

	protected void resetBillingComponent() {
		billDateDF.setReadOnly(false);
		billDateDF.setValue(new Date());
		billDateDF.setReadOnly(true);
		resetAddToCart();
		billableItemsTB.getContainerDataSource().removeAllItems();
		updateBillingPayments(null, 0);
		payModeOG.select(PAY_CASH);
		deliveryChB.setValue(false);
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
		billableItemsTB.setMultiSelect(false);
		billableItemsTB.setImmediate(true);

		billableItemsTB
				.addValueChangeListener(new Property.ValueChangeListener() {

					private static final long serialVersionUID = 1578053093846509122L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event.getProperty().getValue()) {
							Item selectedItem = billableItemsTB.getItem(event
									.getProperty().getValue());
							productNameCB.setValue(selectedItem
									.getItemProperty(PID_PRODUCT_DESCRIPTION)
									.getValue());
							productNameCB.setEnabled(false);
							productRateCB.setValue(selectedItem
									.getItemProperty(PID_UNIT_RATE).getValue());
							quantityCB.addItem(selectedItem.getItemProperty(
									PID_QUANTITY).getValue());
							quantityCB.setValue(selectedItem.getItemProperty(
									PID_QUANTITY).getValue());
							addToCartBT.setCaption(UPDATE_CART);
							addToCartBT.setEnabled(false);
							billableItemsTB.focus();
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
							resetBT.setEnabled(true);
						} else {
							billMeBT.setEnabled(false);
							resetBT.setEnabled(false);
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
						resetAddToCart();
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

		productNameCB.setInputPrompt(PROMPT_PRODUCT_NAME);
		productNameCB.setFilteringMode(FilteringMode.CONTAINS);
		productNameCB.setWidth("100%");
		productNameCB.setRequired(true);
		productNameCB.setPageLength(10);
		productNameCB.setNullSelectionAllowed(true);
		productNameCBCTR.setBeanIdProperty(PID_PRODUCT_DESCRIPTION);
		productNameCB.setContainerDataSource(productNameCBCTR);
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
						productNameCBCTR.addBean(productVO);
						productNameCB.setValue(productVO
								.getProductDescription());
						productRateCB.focus();
					} else {
						displayModal(result[2], Double.parseDouble(result[0]));
					}
				} else {
					displayModal(camelCasePName, 1.0);
				}
			}
		});
		productNameCB.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 4924793475484630166L;

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event.getProperty().getValue()) {
					ProductVO<? extends Quantity> productVO = (ProductVO<? extends Quantity>) productNameCBCTR
							.getItem(event.getProperty().getValue()).getBean();
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
						rateContainer.addAll(productVO.getUnitPrices());
						productRateCB.select(rateContainer.firstItemId());
					}
					quantityCB.getContainerDataSource().removeAllItems();
					quantityCB.setUnit(productVO.getProductUnit().getUnit());
				}
				updateAddToCartStatus();
			}
		});

		productRateCB.setInputPrompt(PROMPT_PRODUCT_RATE);
		productRateCB.setFilteringMode(FilteringMode.STARTSWITH);
		productRateCB.setWidth("100%");
		productRateCB.setRequired(true);
		productRateCB.setEnabled(false);
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

					((List<Amount<Money>>) productNameCBCTR
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
		productRateCB.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -7502332085136797624L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAddToCartStatus();
			}
		});

		quantityCB.setEnabled(false);
		quantityCB.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 2140623144360706800L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAddToCartStatus();
			}
		});

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
					ProductVO<? extends Quantity> productVO = (ProductVO<? extends Quantity>) productNameCBCTR
							.getItem(productNameCB.getValue()).getBean();
					String productName = productVO.getProductName();
					Measure<Double, ? extends Quantity> productUnit = productVO
							.getProductUnit();
					String productDescription = productVO
							.getProductDescription();
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
					BillItemVO<?, ?> billItemVO = (BillItemVO<?, ?>) billableItemsTB
							.getValue();
					updateBillingPayments(billItemVO.getAmount(), -1);
					Item selectedItem = billableItemsTB.getItem(billableItemsTB
							.getValue());
					selectedItem.getItemProperty(PID_UNIT_RATE).setValue(
							productRateCB.getValue());
					selectedItem.getItemProperty(PID_QUANTITY).setValue(
							quantityCB.getValue());
					selectedItem.getItemProperty(PID_AMOUNT).setValue(
							BillItemVO.computeAmount(billItemVO));
					updateBillingPayments(billItemVO.getAmount(), 1);
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

	@SuppressWarnings("unchecked")
	protected void updateAddToCartStatus() {
		if (null != productNameCB.getValue()) {
			productRateCB.setEnabled(true);
			if (null != productRateCB.getValue()) {
				quantityCB.setEnabled(true);
				if (null != quantityCB.getValue()) {
					logger.info("Cart BT enabled");
					addToCartBT.setEnabled(true);
					addToCartBT.focus();
				} else {
					logger.info("Cart BT disabled");
					addToCartBT.setEnabled(false);
				}
			} else {
				quantityCB.setValue(null);
				quantityCB.setEnabled(false);
				logger.info("Cart BT disabled");
				addToCartBT.setEnabled(false);
			}
		} else {
			BeanItemContainer<Amount<Money>> rateContainer = (BeanItemContainer<Amount<Money>>) productRateCB
					.getContainerDataSource();
			rateContainer.removeAllItems();
			productRateCB.setValue(null);
			quantityCB.setValue(null);
			productRateCB.setEnabled(false);
			quantityCB.setEnabled(false);
			logger.info("Cart BT disabled and rate,quantity reset");
			addToCartBT.setEnabled(false);
		}
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
		// productNameCB.unselect(productNameCB.getValue());
		productNameCB.setValue(null);
		productRateCB.setValue(null);
		quantityCB.setValue(null);

		productNameCB.setEnabled(true);
		productRateCB.setEnabled(false);
		quantityCB.setEnabled(false);
		addToCartBT.setEnabled(false);
		addToCartBT.setCaption(ADD_TO_CART);
		productNameCB.focus();
	}

	private Component buildBillingHeader() {
		AbsoluteLayout billingHeaderLayout = new AbsoluteLayout();
		Label logoLB = new Label();
		webBrowser = Page.getCurrent().getWebBrowser();
		clientTZ = new SimpleTimeZone(webBrowser.getTimezoneOffset(), "");

		logoLB.setValue("Retale(M)ine Billing Solution");
		logoLB.setWidth("100%");

		billDateDF.setDateFormat("MMM dd, yyyy hh:mm:ss a");
		billDateDF.setResolution(Resolution.SECOND);
		billDateDF.setTimeZone(clientTZ);
		billDateDF.setValue(new Date());
		billDateDF.setReadOnly(true);
		billDateDF.setWidth("100%");
		billDateDF.setImmediate(false);

		billingHeaderLayout.setImmediate(false);
		billingHeaderLayout.setWidth("100%");
		billingHeaderLayout.setHeight("30px");
		billingHeaderLayout.addComponent(logoLB, "left: 0px; top: 0px;");
		billingHeaderLayout.addComponent(billDateDF, "right: 0px; top: 0px;");

		return billingHeaderLayout;
	}
}
