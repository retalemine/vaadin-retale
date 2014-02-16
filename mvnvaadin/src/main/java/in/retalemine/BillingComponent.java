package in.retalemine;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BillingComponent extends CustomComponent {

	private static final long serialVersionUID = -156296978141491295L;
	private VerticalLayout mainLayout;

	private Label billNoLB = new Label();
	private Integer billNoSeq = 1001;
	private DateField billDateDF = new DateField();

	private Table billableItemsTB = new Table();
	private Integer billableItemIdSeq = new Integer(1);

	private Label subTotalValueLB = new Label();
	private Label totalValueLB = new Label();
	private ComboBox taxTypeCB = new ComboBox(null, Arrays.asList(new String[] {
			"Tax", "VAT", "Sales Tax", "Service Tax" }));
	private HashMap<String, Double> taxPercentageMap = new HashMap<String, Double>() {
		private static final long serialVersionUID = -6862987030835995078L;

		{
			put("Tax", 0.0);
			put("VAT", 4.0);
			put("Sales Tax", 5.0);
			put("Service Tax", 4.5);
		}
	};
	private Label taxValueLB = new Label();

	private ComboBox productNameCB = new ComboBox(
			null,
			Arrays.asList(new String[] { "Lux Sandal", "Hamam", "Cinthol Old" }));
	private ComboBox productPriceCB = new ComboBox(null,
			Arrays.asList(new Double[] { 10.0, 20.0, 30.0, 40.0, 50.0 }));
	private TextField quantityTF = new TextField();
	private ComboBox qtySuffixCB = new ComboBox(null,
			Arrays.asList(new String[] { "pcs", "kg", "lt" }));
	private Button addToCartBT = new Button();
	private Button billMeBT = new Button();

	private final String SUB_TOTAL = "SubTotal";
	private final String EMPTY = "";
	private final String ZERO = "0.0";
	private final String COLON = ":";
	private final String TOTAL = "Total";
	private final String TAX = "Tax";
	private final String SERIAL_NO = "No.";
	private final String PRODUCT_DESC = "Product Description";
	private final String UNIT_RATE = "Unit Rate";
	private final String QUANTITY = "Quantity";
	private final String AMOUNT = "Amount";
	private final String BILL_NO = "Bill No. ";
	private final String DATE = "Date :";
	private final String DATE_FORMAT = "dd-MM-yyyy";
	private final String ADD_TO_CART = "Add To Cart";
	private final String UPDATE_CART = "Update Cart";
	private final String PROMPT_PRODUCT_NAME = "Product Name";
	private final String PROMPT_PRODUCT_RATE = "Rate";
	private final String PROMPT_QUANTITY = "Quantity";
	private final String PROMPT_QTY_SUFFIX = "Unit";
	private final String BILL_ME = "Bill Me";
	private final String PAY_CASH = "Cash";
	private final String PAY_CHEQUE = "Cheque";
	private final String PAY_DELAYED = "Delayed";

	public BillingComponent() {
		setCompositionRoot(buildMainLayout());
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		setWidth("100.0%");
		setHeight("100.0%");

		mainLayout.addComponent(buildBillingHeader());
		mainLayout.addComponent(buildAddToCart());
		mainLayout.addComponent(buildBillingTable());
		mainLayout.addComponent(buildBillingFooter());

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
		Label subTotalLB = new Label();
		Label subTotalColonLB = new Label();
		Label totalLB = new Label();
		Label totalColonLB = new Label();
		Label taxColonLB = new Label();
		final OptionGroup payModeOG = new OptionGroup();

		subTotalLB.setValue(SUB_TOTAL);
		subTotalValueLB.setValue(ZERO);
		subTotalValueLB.setStyleName("v-align-right");
		subTotalColonLB.setValue(COLON);
		subTotalColonLB.setStyleName("v-align-right");

		totalLB.setValue(TOTAL);
		totalValueLB.setValue(ZERO);
		totalValueLB.setStyleName("v-align-right");
		totalColonLB.setValue(COLON);
		totalColonLB.setStyleName("v-align-right");

		taxTypeCB.select(TAX);
		taxTypeCB.setFilteringMode(FilteringMode.CONTAINS);
		taxTypeCB.setNullSelectionAllowed(false);
		taxTypeCB.setPageLength(5);
		taxTypeCB.setWidth("100%");
		taxTypeCB.setImmediate(true);
		taxTypeCB.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = -8058680659345726478L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				taxValueLB.setValue(String.valueOf((taxPercentageMap.get(event
						.getProperty().getValue()) * Double
						.parseDouble(subTotalValueLB.getValue())) / 100));
				totalValueLB.setValue(String.valueOf(Double
						.parseDouble(subTotalValueLB.getValue())
						+ Double.parseDouble(taxValueLB.getValue())));
			}
		});

		taxValueLB.setValue(ZERO);
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
			private static final long serialVersionUID = 1141565714804567261L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (PAY_CHEQUE.equals(event.getProperty().getValue())) {
					// customer details needed
				} else if (PAY_DELAYED.equals(event.getProperty().getValue())) {
					// customer details needed
				} else if (PAY_CASH.equals(event.getProperty().getValue())) {
					// customer details not mandatory
				}
			}
		});

		billMeBT.setCaption(BILL_ME);
		billMeBT.setEnabled(false);
		billMeBT.setImmediate(true);
		billMeBT.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 8926151523719182107L;

			@Override
			public void buttonClick(ClickEvent event) {
				billNoLB.setValue(String.valueOf(billNoSeq++));
				billDateDF.setReadOnly(false);
				billDateDF.setValue(new Date());
				billDateDF.setReadOnly(true);
				resetAddToCart();
				billableItemsTB.removeAllItems();
				updateBillingPayments();
				payModeOG.select(PAY_CASH);
			}
		});

		paymentLayout.addComponent(subTotalLayout);
		paymentLayout.addComponent(taxLayout);
		paymentLayout.addComponent(totalLayout);
		paymentLayout.addComponent(payModeOG);
		paymentLayout.addComponent(billMeBT);

		subTotalLayout.addComponent(subTotalLB);
		subTotalLayout.addComponent(subTotalColonLB);
		subTotalLayout.addComponent(subTotalValueLB);

		taxLayout.addComponent(taxTypeCB);
		taxLayout.addComponent(taxColonLB);
		taxLayout.addComponent(taxValueLB);

		totalLayout.addComponent(totalLB);
		totalLayout.addComponent(totalColonLB);
		totalLayout.addComponent(totalValueLB);

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

		paymentLayout.setImmediate(false);
		paymentLayout.setWidth("100%");
		paymentLayout.setMargin(true);
		paymentLayout.setSpacing(true);

		return paymentLayout;
	}

	private Component buildCustomerProfile() {
		Panel customerPanel = new Panel("Customer details");

		customerPanel.setImmediate(false);
		customerPanel.setWidth("100%");
		customerPanel.setHeight("100%");

		return customerPanel;
	}

	private Component buildBillingTable() {
		Panel billingPanel = new Panel();

		billableItemsTB.addContainerProperty(SERIAL_NO, Integer.class, null);
		billableItemsTB.addContainerProperty(PRODUCT_DESC, String.class, "");
		billableItemsTB.addContainerProperty(UNIT_RATE, Double.class, 0.0);
		billableItemsTB.addContainerProperty(QUANTITY, String.class, "1");
		billableItemsTB.addContainerProperty(AMOUNT, Double.class, 0.0);
		billableItemsTB.setColumnAlignment("Amount", Align.RIGHT);
		billableItemsTB.setPageLength(5);
		billableItemsTB.setWidth("100%");
		billableItemsTB.setColumnExpandRatio(SERIAL_NO, 1);
		billableItemsTB.setColumnExpandRatio(PRODUCT_DESC, 18);
		billableItemsTB.setColumnExpandRatio(UNIT_RATE, 4);
		billableItemsTB.setColumnExpandRatio(QUANTITY, 2);
		billableItemsTB.setColumnExpandRatio(AMOUNT, 6);
		billableItemsTB.setSelectable(true);
		billableItemsTB.setImmediate(true);
		billableItemsTB
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 172879437178906284L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (null != event.getProperty().getValue()) {
							Item selectedItem = billableItemsTB.getItem(event
									.getProperty().getValue());
							productNameCB.setValue(selectedItem
									.getItemProperty(PRODUCT_DESC).getValue());
							productPriceCB.setValue(selectedItem
									.getItemProperty(UNIT_RATE).getValue());
							String qtyArray[] = selectedItem
									.getItemProperty(QUANTITY).getValue()
									.toString().split(" ");
							quantityTF.setValue(qtyArray[0]);
							qtySuffixCB.setValue(qtyArray[1]);
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
					private static final long serialVersionUID = -8769363500051513731L;

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
			private static final long serialVersionUID = 7522649455023798865L;
			Action actionDel = new ShortcutAction("Delete Item",
					ShortcutAction.KeyCode.DELETE, null);

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				if (target instanceof Table) {
					if (action == actionDel) {
						billableItemsTB.removeItem(billableItemsTB.getValue());
						reOrderBillingSNo();
						updateBillingPayments();
					}
				}
			}

			@SuppressWarnings("unchecked")
			private void reOrderBillingSNo() {
				Integer serialNoSeq = 1;
				for (Iterator<?> i = billableItemsTB.getItemIds().iterator(); i
						.hasNext();) {
					billableItemsTB.getItem(i.next())
							.getItemProperty(SERIAL_NO).setValue(serialNoSeq++);
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return new Action[] { actionDel };
			}
		});
		billingPanel.setWidth("100%");
		billingPanel.setHeight("100%");

		billingPanel.setContent(billableItemsTB);

		return billingPanel;
	}

	protected void updateBillingPayments() {
		double subTotal = 0.0;
		for (Iterator<?> i = billableItemsTB.getItemIds().iterator(); i
				.hasNext();) {
			subTotal += (Double) billableItemsTB.getItem(i.next())
					.getItemProperty(AMOUNT).getValue();
		}
		subTotalValueLB.setValue(String.valueOf(subTotal));
		taxValueLB
				.setValue(String.valueOf((taxPercentageMap.get(taxTypeCB
						.getValue()) * Double.parseDouble(subTotalValueLB
						.getValue())) / 100));
		totalValueLB.setValue(String.valueOf(Double.parseDouble(subTotalValueLB
				.getValue()) + Double.parseDouble(taxValueLB.getValue())));
	}

	private Component buildAddToCart() {
		HorizontalLayout addToCartLayout = new HorizontalLayout();
		Property.ValueChangeListener addToCartEnabler;

		addToCartEnabler = new Property.ValueChangeListener() {
			private static final long serialVersionUID = 8262792786781169162L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != productNameCB.getValue()
						&& null != productPriceCB.getValue()
						&& null != quantityTF && null != qtySuffixCB.getValue()) {
					addToCartBT.setEnabled(true);
					addToCartBT.focus();
				} else {
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
		productNameCB.setImmediate(true);
		productNameCB.setNewItemsAllowed(true);
		productNameCB.setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = -6121404108772061043L;

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
				productNameCB.addItem(camelCasePName.toString());
				productNameCB.setValue(camelCasePName.toString());
			}
		});
		productNameCB.addValueChangeListener(addToCartEnabler);

		productPriceCB.setInputPrompt(PROMPT_PRODUCT_RATE);
		productPriceCB.setFilteringMode(FilteringMode.STARTSWITH);
		productPriceCB.setWidth("100%");
		productPriceCB.setRequired(true);
		productPriceCB.setPageLength(10);
		productPriceCB.setNullSelectionAllowed(true);
		productPriceCB.setImmediate(true);
		productPriceCB.setNewItemsAllowed(true);
		productPriceCB.setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = 4239289366078811975L;

			@Override
			public void addNewItem(String newPrice) {
				Double parsedPrice = Double.parseDouble(newPrice.trim());
				productPriceCB.addItem(parsedPrice);
				productPriceCB.setValue(parsedPrice);
			}
		});
		productPriceCB.addValueChangeListener(addToCartEnabler);

		quantityTF.setInputPrompt(PROMPT_QUANTITY);
		quantityTF.setRequired(true);
		quantityTF.setWidth("100%");
		quantityTF.setImmediate(true);
		quantityTF.addValueChangeListener(addToCartEnabler);

		qtySuffixCB.setInputPrompt(PROMPT_QTY_SUFFIX);
		qtySuffixCB.setFilteringMode(FilteringMode.STARTSWITH);
		qtySuffixCB.setWidth("100%");
		qtySuffixCB.setRequired(true);
		qtySuffixCB.setPageLength(10);
		qtySuffixCB.setNullSelectionAllowed(true);
		qtySuffixCB.setImmediate(true);
		qtySuffixCB.addValueChangeListener(addToCartEnabler);

		addToCartBT.setCaption(ADD_TO_CART);
		addToCartBT.setWidth("100%");
		addToCartBT.setImmediate(true);
		addToCartBT.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -4011081703632078210L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				final Object[] productInfo = new Object[5];

				if (ADD_TO_CART.equals(addToCartBT.getCaption())) {
					productInfo[0] = billableItemsTB.size() + 1;
					productInfo[1] = productNameCB.getValue();
					productInfo[2] = productPriceCB.getValue();
					productInfo[3] = quantityTF.getValue() + " "
							+ qtySuffixCB.getValue();
					productInfo[4] = (Double) productPriceCB.getValue()
							* Double.parseDouble(quantityTF.getValue());
					billableItemsTB.addItem(productInfo, billableItemIdSeq);
					billableItemsTB
							.setCurrentPageFirstItemId(billableItemIdSeq++);
				} else {
					Item selectedItemId = billableItemsTB
							.getItem(billableItemsTB.getValue());
					selectedItemId.getItemProperty(PRODUCT_DESC).setValue(
							productNameCB.getValue());
					selectedItemId.getItemProperty(UNIT_RATE).setValue(
							productPriceCB.getValue());
					selectedItemId.getItemProperty(QUANTITY).setValue(
							quantityTF.getValue() + " "
									+ qtySuffixCB.getValue());
					selectedItemId.getItemProperty(AMOUNT)
							.setValue(
									(Double) productPriceCB.getValue()
											* Double.parseDouble(quantityTF
													.getValue()));
				}
				updateBillingPayments();
				resetAddToCart();
			}

		});

		resetAddToCart();

		addToCartLayout.setImmediate(false);
		addToCartLayout.setWidth("100%");
		addToCartLayout.setHeight("100%");
		addToCartLayout.setMargin(false);
		addToCartLayout.setSpacing(true);

		addToCartLayout.addComponent(productNameCB);
		addToCartLayout.addComponent(productPriceCB);
		addToCartLayout.addComponent(quantityTF);
		addToCartLayout.addComponent(qtySuffixCB);
		addToCartLayout.addComponent(addToCartBT);

		addToCartLayout.setExpandRatio(productNameCB, 4);
		addToCartLayout.setExpandRatio(productPriceCB, 1.5f);
		addToCartLayout.setExpandRatio(quantityTF, 1f);
		addToCartLayout.setExpandRatio(qtySuffixCB, 1f);
		addToCartLayout.setExpandRatio(addToCartBT, 1.5f);

		return addToCartLayout;
	}

	protected void resetAddToCart() {
		productNameCB.setValue(null);
		productPriceCB.setValue(null);
		quantityTF.setValue(EMPTY);
		qtySuffixCB.setValue(null);
		addToCartBT.setEnabled(false);
		addToCartBT.setCaption(ADD_TO_CART);
		productNameCB.focus();
	}

	private Component buildBillingHeader() {
		HorizontalLayout billingHeaderLayout = new HorizontalLayout();
		HorizontalLayout billNoLayout = new HorizontalLayout();
		HorizontalLayout billDateLayout = new HorizontalLayout();
		Label billNoCaptionLB = new Label();
		Label billDateCaptionLB = new Label();

		billingHeaderLayout.setImmediate(false);
		billingHeaderLayout.setWidth("100%");
		billingHeaderLayout.setHeight("100%");
		billingHeaderLayout.setMargin(false);
		billingHeaderLayout.setSpacing(false);
		billNoLayout.setSpacing(true);
		billDateLayout.setSpacing(true);

		billNoCaptionLB.setValue(BILL_NO);
		billNoLB.setValue(String.valueOf(billNoSeq++));
		billDateCaptionLB.setValue(DATE);
		billDateDF.setValue(new Date());
		billDateDF.setReadOnly(true);
		billDateDF.setDateFormat(DATE_FORMAT);

		billingHeaderLayout.addComponent(billNoLayout);
		billingHeaderLayout.setComponentAlignment(billNoLayout,
				Alignment.MIDDLE_LEFT);
		billingHeaderLayout.addComponent(billDateLayout);
		billingHeaderLayout.setComponentAlignment(billDateLayout,
				Alignment.MIDDLE_RIGHT);

		billNoLayout.addComponent(billNoCaptionLB);
		billNoLayout.addComponent(billNoLB);

		billDateLayout.addComponent(billDateCaptionLB);
		billDateLayout.addComponent(billDateDF);

		return billingHeaderLayout;
	}
}
