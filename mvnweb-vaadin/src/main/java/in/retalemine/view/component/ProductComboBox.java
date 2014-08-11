package in.retalemine.view.component;

import in.retalemine.util.BillingComputationUtil;
import in.retalemine.util.BillingRegExUtil;
import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.event.BillItemSelectionEvent;
import in.retalemine.view.event.CartSelectionEvent;
import in.retalemine.view.event.ProductSelectionEvent;
import in.retalemine.view.event.RateSelectionEvent;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ProductComboBox extends ComboBox {

	private static final long serialVersionUID = -6622095818630084233L;
	private static final Logger logger = LoggerFactory
			.getLogger(ProductComboBox.class);
	private BeanContainer<String, ProductVO<? extends Quantity>> container = new BeanContainer<String, ProductVO<? extends Quantity>>(
			ProductVO.class);

	public ProductComboBox(final EventBus eventBus) {

		logger.info("Initializing {}", getClass().getSimpleName());
		setWidth("100%");
		setInputPrompt(BillingConstants.PROMPT_PRODUCT_NAME);
		container.setBeanIdProperty(BillingConstants.PID_PRODUCT_DESCRIPTION);
		setContainerDataSource(container);
		setPageLength(15);
		setFilteringMode(FilteringMode.CONTAINS);
		setNullSelectionAllowed(true);
		setImmediate(true);
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {

			private static final long serialVersionUID = -4427049310919210040L;

			@Override
			public void addNewItem(String newProductName) {
				logger.info("{} addNewItem starts", getClass().getSimpleName());
				String camelCasePName = BillingRegExUtil
						.getCamelCaseString(newProductName);
				String[] result = BillingRegExUtil
						.resolveProductUnit(camelCasePName);
				if (null != result) {
					String validUnit = null;
					if (null != (validUnit = BillingComputationUtil
							.getValidUnit(result[2]))) {
						result[2] = validUnit;
						Measure<Double, ? extends Quantity> productUnit = Measure
								.valueOf(Double.parseDouble(result[1]),
										javax.measure.unit.Unit
												.valueOf(result[2]));
						String productDescription = result[0]
								+ BillingConstants.PRODUCT_DESC_DIVIDER
								+ productUnit;
						if (!container.containsId(productDescription)) {
							ProductVO<? extends Quantity> productVO = ProductVO
									.valueOf(result[0], productUnit, null);
							container.addBean(productVO);
						}
						setValue(productDescription);
					} else {
						productModalWindow(result[0], result[1]);
					}
				} else {
					productModalWindow(camelCasePName, "1");
				}
				logger.info("{} addNewItem ends", getClass().getSimpleName());
			}
		});
		addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = -7225953784159663762L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				logger.info("{} valueChange", getClass().getSimpleName());
				if (null != event.getProperty().getValue()) {
					ProductVO<? extends Quantity> productVO = (ProductVO<? extends Quantity>) container
							.getItem(event.getProperty().getValue()).getBean();
					logger.info("{} posts ProductSelectionEvent", getClass()
							.getSimpleName());
					eventBus.post(new ProductSelectionEvent(productVO));
				}
			}
		});

	}

	private void productModalWindow(String productName, String quantity) {
		final Window modalWindow = new Window(
				BillingConstants.BOX_LABEL_PRODUCT);

		FormLayout mailLayout = new FormLayout();
		final TextField pName = new TextField(
				BillingConstants.BOX_LABEL_PRODUCT_NAME);
		final QuantityComponent qty = new QuantityComponent(
				BillingConstants.BOX_LABEL_UNIT_QUANTITY, quantity,
				BillingComputationUtil.getValidUnits());
		Button submit = new Button(BillingConstants.BOX_LABEL_SUBMIT);

		pName.setInputPrompt(BillingConstants.BOX_PROMPT_PRODUCT_NAME);
		pName.setWidth("100%");
		pName.setValue(productName.replaceAll("[0-9]*$", ""));

		submit.setImmediate(true);
		submit.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 8518609553777583947L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (null == pName.getValue()) {
					// TODO validation
				} else if (null == qty.getValue()) {
					// TODO validation
				} else {
					modalWindow.close();
					getNewItemHandler().addNewItem(
							pName.getValue() + qty.getValue());
				}
			}
		});

		mailLayout.addComponent(pName);
		mailLayout.addComponent(qty);
		mailLayout.addComponent(submit);

		mailLayout.setImmediate(false);
		mailLayout.setWidth("100%");
		mailLayout.setMargin(true);
		mailLayout.setSpacing(true);
		mailLayout.setComponentAlignment(submit, Alignment.MIDDLE_RIGHT);

		modalWindow.setContent(mailLayout);
		modalWindow.setModal(true);
		modalWindow.setWidth("30%");
		modalWindow.setResizable(false);
		modalWindow.addActionHandler(new Handler() {

			private static final long serialVersionUID = 4470126167093872862L;
			Action actionEsc = new ShortcutAction(
					BillingConstants.BOX_HINT_CLOSE,
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
		UI.getCurrent().addWindow(modalWindow);
		pName.focus();

	}

	private void resetProductComboBox() {
		setValue(null);
		setEnabled(true);
		focus();
	}

	@Subscribe
	public void listenRateSelectionEvent(final RateSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (event.getIsNew()) {
			ProductVO<? extends Quantity> productVO = (ProductVO<? extends Quantity>) container
					.getItem(getValue()).getBean();
			productVO.getUnitRates().add(event.getUnitRate());
		}
	}

	@Subscribe
	public void listenCartSelectionEvent(final CartSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		resetProductComboBox();
	}

	@Subscribe
	public void listenBillItemSelectionEvent(final BillItemSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (null != event.getBillItemVO()) {
			setValue(event.getBillItemVO().getProductDescription());
			setEnabled(false);
		} else {
			resetProductComboBox();
		}
	}

}
