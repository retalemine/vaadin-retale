package in.retalemine.view.component;

import in.retalemine.util.BillingComputationUtil;
import in.retalemine.util.BillingUnits;
import in.retalemine.view.VO.TaxVO;
import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.converter.AmountConverter;
import in.retalemine.view.event.BillItemChangeEvent;
import in.retalemine.view.event.TaxSelectionEvent;

import java.util.Collection;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class BillingAmountPanel extends VerticalLayout {
	private static final long serialVersionUID = -2590349203393239246L;
	private static final Logger logger = LoggerFactory
			.getLogger(BillingAmountPanel.class);
	private final EventBus eventBus;
	private final Property<Amount<Money>> subTotalProperty = new ObjectProperty<Amount<Money>>(
			Amount.valueOf(0.0, BillingUnits.INR));
	private final Property<Amount<Money>> taxAmountProperty = new ObjectProperty<Amount<Money>>(
			Amount.valueOf(0.0, BillingUnits.INR));
	private final Property<Amount<Money>> totalAmountProperty = new ObjectProperty<Amount<Money>>(
			Amount.valueOf(0.0, BillingUnits.INR));
	private AmountComponent subTotal;
	private TaxAmountComponent taxAmount;
	private AmountComponent totalAmount;
	private double taxPercent = 0.0;

	public BillingAmountPanel(final EventBus eventBus) {
		logger.info("Initializing {}", getClass().getSimpleName());

		setImmediate(false);
		setWidth("100%");
		setMargin(false);
		setSpacing(true);

		this.eventBus = eventBus;
		subTotal = new AmountComponent(BillingConstants.SUB_TOTAL,
				subTotalProperty);
		taxAmount = new TaxAmountComponent(taxAmountProperty);
		totalAmount = new AmountComponent(BillingConstants.TOTAL,
				totalAmountProperty);

		addComponent(subTotal);
		addComponent(taxAmount);
		addComponent(totalAmount);
	}

	@Subscribe
	public void listenBillItemChangeEvent(final BillItemChangeEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (null != event.getBillItems()) {
			subTotalProperty.setValue(BillingComputationUtil
					.computeSubAmount(event.getBillItems()));
			taxAmountProperty.setValue(subTotalProperty.getValue()
					.times(taxPercent).divide(100));
			totalAmountProperty.setValue(subTotalProperty.getValue().plus(
					taxAmountProperty.getValue()));
		} else {
			subTotalProperty.setValue(subTotalProperty.getValue().times(0));
			taxAmountProperty.setValue(taxAmountProperty.getValue().times(0));
			totalAmountProperty.setValue(totalAmountProperty.getValue()
					.times(0));
		}
	}

	@Subscribe
	public void listenTaxSelectionEvent(final TaxSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (null != event.getTaxVO()) {
			taxPercent = event.getTaxVO().getTaxPercent();
			taxAmountProperty.setValue(subTotalProperty.getValue()
					.times(taxPercent).divide(100));
			totalAmountProperty.setValue(subTotalProperty.getValue().plus(
					taxAmountProperty.getValue()));
		} else {
			taxPercent = 0;
			taxAmountProperty.setValue(subTotalProperty.getValue().times(
					taxPercent));
			totalAmountProperty.setValue(subTotalProperty.getValue());
		}

	}

	class AmountComponent extends HorizontalLayout {
		private static final long serialVersionUID = 990401277001584257L;
		private Label label;
		private AmountLabel value;

		public AmountComponent(String caption,
				Property<Amount<Money>> contentSource) {
			logger.info("Initializing {}", getClass().getSimpleName());
			setWidth("100%");
			setImmediate(false);
			setSpacing(true);
			setMargin(false);

			label = new Label(caption);
			value = new AmountLabel(contentSource);

			addComponent(label);
			addComponent(value);

		}
	}

	class TaxAmountComponent extends HorizontalLayout {
		private static final long serialVersionUID = -6788717389544041898L;
		private TaxComboBox taxComboBox;
		private AmountLabel value;

		public TaxAmountComponent(final Property<Amount<Money>> contentSource) {
			logger.info("Initializing {}", getClass().getSimpleName());
			setImmediate(false);
			setWidth("100%");
			setMargin(false);
			setSpacing(true);

			taxComboBox = new TaxComboBox(
					BillingComputationUtil.getBillingTaxSet());
			value = new AmountLabel(contentSource);

			addComponent(taxComboBox);
			addComponent(value);

		}
	}

	class TaxComboBox extends ComboBox {
		private static final long serialVersionUID = 6485035526499229475L;
		private final BeanContainer<String, TaxVO> container = new BeanContainer<String, TaxVO>(
				TaxVO.class);

		public TaxComboBox(Collection<?> options) {
			logger.info("Initializing {}", getClass().getSimpleName());
			setImmediate(true);
			setWidth("100%");
			setPageLength(4);
			setNewItemsAllowed(false);
			setInputPrompt(BillingConstants.PROMPT_TAX);
			setFilteringMode(FilteringMode.CONTAINS);
			setNullSelectionAllowed(true);
			container.setBeanIdProperty(BillingConstants.PID_TAX_TYPE);
			setContainerDataSource(BillingComputationUtil.getContainerSource(
					options, container));

			addValueChangeListener(new Property.ValueChangeListener() {

				private static final long serialVersionUID = 2793918800928950398L;

				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					Object value = event.getProperty().getValue();
					logger.info("{} posts TaxSelectionEvent", getClass()
							.getSimpleName());
					if (null != value) {
						eventBus.post(new TaxSelectionEvent(container.getItem(
								value).getBean()));
					} else {
						eventBus.post(new TaxSelectionEvent(null));
					}
				}
			});
		}
	}

	class AmountLabel extends Label {

		private static final long serialVersionUID = -1176251237331368412L;

		public AmountLabel(Property<Amount<Money>> contentSource) {
			logger.info("Initializing {}", getClass().getSimpleName());
			setConverter(new AmountConverter());
			setPropertyDataSource(contentSource);
			setStyleName("v-align-right");
			setWidth("100%");
		}
	}
}
