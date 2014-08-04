package in.retalemine.view.component;

import in.retalemine.util.BillingComputationUtil;
import in.retalemine.util.BillingRegExUtil;
import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.event.BillItemSelectionEvent;
import in.retalemine.view.event.CartSelectionEvent;
import in.retalemine.view.event.ProductSelectionEvent;
import in.retalemine.view.event.QuantitySelectionEvent;
import in.retalemine.view.event.RateSelectionEvent;

import java.util.Map;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class QuantityComboBox extends ComboBox {

	private static final long serialVersionUID = -3126268465155964123L;

	private static final Logger logger = LoggerFactory
			.getLogger(QuantityComboBox.class);
	private BeanItemContainer<Measure<Double, ? extends Quantity>> container = new BeanItemContainer<Measure<Double, ? extends Quantity>>(
			Measure.class);
	private javax.measure.unit.Unit<? extends Quantity> unit;

	public QuantityComboBox(final EventBus eventBus) {

		logger.info("Initializing {}", getClass().getSimpleName());
		setWidth("100%");
		setInputPrompt(BillingConstants.PROMPT_QUANTITY);
		setContainerDataSource(container);
		setItemCaptionMode(AbstractSelect.ItemCaptionMode.ID);
		setPageLength(6);
		setFilteringMode(FilteringMode.STARTSWITH);
		setNullSelectionAllowed(true);
		setEnabled(false);
		setImmediate(true);
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {

			private static final long serialVersionUID = -6127216183827277131L;

			@Override
			public void addNewItem(String newQuantity) {
				logger.info("{} addNewItem starts", getClass().getSimpleName());
				String[] quantitySplit = null;
				javax.measure.unit.Unit<?> newUnit = null;
				Double parsedQuantity = 0.0;
				Measure<Double, ? extends Quantity> quantity = null;
				try {
					parsedQuantity = Double.parseDouble(newQuantity.trim());
					if (parsedQuantity > 0) {
						quantity = Measure.valueOf(parsedQuantity, unit);
						addItem(quantity);
						setValue(quantity);
					} else {
						quantityModalWindow(1);
					}
				} catch (NumberFormatException e) {
					quantitySplit = BillingRegExUtil
							.resolveQuantity(newQuantity);
					if (null != quantitySplit) {
						String validUnit = null;
						parsedQuantity = Double.parseDouble(quantitySplit[0]);
						if (null != (validUnit = BillingComputationUtil
								.getValidUnit(quantitySplit[1]))
								&& parsedQuantity > 0) {
							newUnit = javax.measure.unit.Unit
									.valueOf(validUnit);
							quantity = Measure.valueOf(parsedQuantity, newUnit);
							addItem(quantity);
							setValue(quantity);
						} else {
							quantityModalWindow(parsedQuantity > 0 ? parsedQuantity
									: 1);
						}
					} else {
						quantityModalWindow(1);
					}
				}
				logger.info("{} addNewItem ends", getClass().getSimpleName());
			}

		});
		addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 5358448286492727212L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				logger.info("{} valueChange", getClass().getSimpleName());
				if (null != event.getProperty().getValue()) {
					Measure<Double, ? extends Quantity> quantity = (Measure<Double, ? extends Quantity>) container
							.getItem(event.getProperty().getValue()).getBean();
					logger.info("{} posts QuantitySelectionEvent", getClass()
							.getSimpleName());
					eventBus.post(new QuantitySelectionEvent(quantity));
				}
			}
		});
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		String newFilter = ((String) variables.get("filter"));
		if (null != this.unit && null != newFilter
				&& !"".equals(newFilter.trim()) && !newFilter.endsWith(" ")) {
			try {
				container.removeAllItems();
				container.addItem(Measure.valueOf(
						Double.parseDouble(newFilter), this.unit));
			} catch (NumberFormatException e) {
				container.removeAllItems();
			}
		}
		super.changeVariables(source, variables);
	}

	protected void quantityModalWindow(double quantity) {

		final Window modalWindow = new Window("Enter Net Quantity");
		FormLayout mailLayout = new FormLayout();
		final QuantityComponent qty = new QuantityComponent(
				BillingConstants.BOX_LABEL_UNIT_QUANTITY,
				String.valueOf(quantity),
				BillingComputationUtil.getValidUnits(unit.toString()));
		Button submit = new Button(BillingConstants.BOX_LABEL_SUBMIT);

		submit.setImmediate(true);
		submit.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 8518609553777583947L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (null == qty.getValue()) {
					// TODO validation
				} else {
					modalWindow.close();
					getNewItemHandler().addNewItem("" + qty.getValue());

				}
			}
		});

		mailLayout.setImmediate(false);
		mailLayout.setWidth("100%");
		mailLayout.setMargin(true);
		mailLayout.setSpacing(true);
		mailLayout.addComponent(qty);
		mailLayout.addComponent(submit);
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
		qty.focus();
	}

	private void resetQuantityComboBox() {
		container.removeAllItems();
		setValue(null);
		setEnabled(false);
		unit = null;
	}

	@Subscribe
	public void listenProductSelectionEvent(final ProductSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		unit = event.getProductVO().getProductUnit().getUnit();
	}

	@Subscribe
	public void listenRateSelectionEvent(final RateSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		setEnabled(true);
		focus();
	}

	@Subscribe
	public void listenCartSelectionEvent(final CartSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		resetQuantityComboBox();
	}

	@Subscribe
	public void listenBillItemSelectionEvent(final BillItemSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (null != event.getBillItemVO()) {
			container.removeAllItems();
			container.addItem(event.getBillItemVO().getNetQuantity());
			setValue(event.getBillItemVO().getNetQuantity());
		} else {
			resetQuantityComboBox();
		}
	}

}
