package in.retalemine.view.component;

import in.retalemine.util.BillingComputationUtil;
import in.retalemine.view.VO.BillItemVO;
import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.event.BillItemSelectionEvent;
import in.retalemine.view.event.CartSelectionEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Table;

public class BillingTable extends Table {

	private static final long serialVersionUID = 2804029178320803422L;
	private static final Logger logger = LoggerFactory
			.getLogger(BillingTable.class);
	private BeanItemContainer<BillItemVO<? extends Quantity, ? extends Quantity>> container = new BeanItemContainer<BillItemVO<? extends Quantity, ? extends Quantity>>(
			BillItemVO.class);

	public BillingTable(final EventBus eventBus) {

		logger.info("Initializing {}", getClass().getSimpleName());

		addContainerProperty(BillingConstants.PID_PRODUCT_NAME, String.class,
				"", BillingConstants.PRODUCT_NAME, null, null);
		addContainerProperty(BillingConstants.PID_PRODUCT_UNIT, Measure.class,
				0.0, BillingConstants.PRODUCT_UNIT, null, null);
		addContainerProperty(BillingConstants.PID_UNIT_RATE, Amount.class, 0.0,
				BillingConstants.UNIT_RATE, null, Align.RIGHT);
		addContainerProperty(BillingConstants.PID_NET_QUANTITY,
				javax.measure.unit.Unit.class, "1", BillingConstants.QUANTITY,
				null, null);
		addContainerProperty(BillingConstants.PID_AMOUNT, Amount.class, 0.0,
				BillingConstants.AMOUNT, null, Align.RIGHT);

		setContainerDataSource(container);

		setVisibleColumns(new Object[] { BillingConstants.PID_PRODUCT_NAME,
				BillingConstants.PID_PRODUCT_UNIT,
				BillingConstants.PID_UNIT_RATE,
				BillingConstants.PID_NET_QUANTITY, BillingConstants.PID_AMOUNT });

		setColumnExpandRatio(BillingConstants.PID_PRODUCT_NAME, 18);
		setColumnExpandRatio(BillingConstants.PID_PRODUCT_UNIT, 4);
		setColumnExpandRatio(BillingConstants.PID_UNIT_RATE, 4);
		setColumnExpandRatio(BillingConstants.PID_NET_QUANTITY, 4);
		setColumnExpandRatio(BillingConstants.PID_AMOUNT, 6);

		setSizeFull();
		setSelectable(true);
		setMultiSelect(false);
		setImmediate(true);

		addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = -1033825452638233522L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				logger.info("{} valueChange", getClass().getSimpleName());
				if (null != event.getProperty().getValue()) {
					@SuppressWarnings("unchecked")
					BillItemVO<? extends Quantity, ? extends Quantity> billItemVO = (BillItemVO<? extends Quantity, ? extends Quantity>) event
							.getProperty().getValue();
					logger.info("{} posts BillItemSelectionEvent", getClass()
							.getSimpleName());
					eventBus.post(new BillItemSelectionEvent(billItemVO));
					focus();
					logger.info("{} table in focus", getClass().getSimpleName());
				} else {
					logger.info("{} posts BillItemSelectionEvent", getClass()
							.getSimpleName());
					eventBus.post(new BillItemSelectionEvent(null));
				}
			}
		});

		addItemSetChangeListener(new Container.ItemSetChangeListener() {

			private static final long serialVersionUID = -8974969742374909756L;

			@Override
			public void containerItemSetChange(
					Container.ItemSetChangeEvent event) {
				logger.info("{} ItemSetChange", getClass().getSimpleName());
				if (event.getContainer().size() > 0) {
					// enable billMeBT, resetBT
				} else {
					// disable billMeBT, resetBT
				}
			}
		});

		addActionHandler(new Handler() {

			private static final long serialVersionUID = -7714736209726905242L;
			Action actionDel = new ShortcutAction("Delete Item",
					ShortcutAction.KeyCode.DELETE, null);

			@Override
			public void handleAction(Action action, Object sender, Object target) {
				logger.info(
						"Action Handler : Action - {} : sender - {} : target - {}",
						action.getClass().getSimpleName(), sender.getClass()
								.getSimpleName(), target.getClass()
								.getSimpleName());
				if (sender instanceof Table) {
					if (action == actionDel) {
						container.removeItem(getValue());
						eventBus.post(new BillItemSelectionEvent(null));
					}
				}
			}

			@Override
			public Action[] getActions(Object target, Object sender) {
				return new Action[] { actionDel };
			}
		});

	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		logger.info("getSortableContainerPropertyIds");
		// return super.getSortableContainerPropertyIds();
		LinkedList<Object> sortables = new LinkedList<Object>();
		sortables.addAll(Arrays.asList(BillingConstants.PID_PRODUCT_NAME,
				BillingConstants.PID_UNIT_RATE, BillingConstants.PID_AMOUNT));
		return sortables;
	}

	@Override
	public Object getSortContainerPropertyId() {
		logger.info("getSortContainerPropertyId");
		return super.getSortContainerPropertyId();
	}

	@Override
	public void setSortContainerPropertyId(Object propertyId) {
		logger.info("setSortContainerPropertyId");
		super.setSortContainerPropertyId(propertyId);
	}

	@Override
	public void sort(Object[] propertyId, boolean[] ascending)
			throws UnsupportedOperationException {
		logger.info("sort");
		super.sort(propertyId, ascending);
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		logger.info("changeVariables");
		super.changeVariables(source, variables);
	}

	@Override
	public void refreshRowCache() {
		logger.info("refreshRowCache");
		super.refreshRowCache();
	}

	@Override
	protected void resetPageBuffer() {
		logger.info("resetPageBuffer");
		super.resetPageBuffer();
	}

	@Override
	protected void refreshRenderedCells() {
		logger.info("refreshRenderedCells");
		super.refreshRenderedCells();
	}

	private Object getItemValue(Object itemId, String pId) {
		return container.getItem(itemId).getItemProperty(pId).getValue();
	}

	@SuppressWarnings("unchecked")
	private void setItemValue(Object itemId, String pId, Object value) {
		container.getItem(itemId).getItemProperty(pId).setValue(value);
	}

	@SuppressWarnings("unchecked")
	private void updateItem(Object itemId,
			BillItemVO<? extends Quantity, ? extends Quantity> eventItem,
			Boolean replaceQty, Boolean replaceUnitRate) {

		if (replaceQty) {
			setItemValue(itemId, BillingConstants.PID_NET_QUANTITY,
					eventItem.getNetQuantity());
			setItemValue(
					itemId,
					BillingConstants.PID_AMOUNT,
					BillingComputationUtil.computeAmount(
							eventItem.getProductUnit(),
							eventItem.getUnitRate(), eventItem.getNetQuantity()));
		} else {
			Measure<Double, ? extends Quantity> oldNetQuantity = (Measure<Double, ? extends Quantity>) getItemValue(
					itemId, BillingConstants.PID_NET_QUANTITY);
			Measure<Double, ? extends Quantity> finalNetQuantity = BillingComputationUtil
					.computeNetQuantity(eventItem.getProductUnit(),
							oldNetQuantity, eventItem.getNetQuantity());
			setItemValue(itemId, BillingConstants.PID_NET_QUANTITY,
					finalNetQuantity);
			setItemValue(itemId, BillingConstants.PID_AMOUNT,
					BillingComputationUtil.computeAmount(
							eventItem.getProductUnit(),
							eventItem.getUnitRate(), finalNetQuantity));
		}

		if (replaceUnitRate) {
			setItemValue(itemId, BillingConstants.PID_UNIT_RATE,
					eventItem.getUnitRate());
			logger.info("$");
			refreshRowCache();
			logger.info("${}", container.getItemIds());
		}
	}

	@Subscribe
	public void listenCartSelectionEvent(final CartSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (event.getIsNew()) {
			if (container.containsId(event.getBillItemVO())) {
				updateItem(event.getBillItemVO(), event.getBillItemVO(), false,
						false);
			} else {
				container.addItem(event.getBillItemVO());
			}
		} else {
			if (event.getBillItemVO().equals(getValue())) {
				// UnitRate not modified
				updateItem(getValue(), event.getBillItemVO(), true, false);
			} else {
				// UnitRate modified
				if (container.containsId(event.getBillItemVO())) {
					container.removeItem(getValue());
					updateItem(event.getBillItemVO(), event.getBillItemVO(),
							false, false);
				} else {
					updateItem(getValue(), event.getBillItemVO(), true, true);
				}
			}
		}
	}

}
