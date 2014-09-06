package in.retalemine.view.component;

import in.retalemine.view.VO.BillItemVO;
import in.retalemine.view.VO.ProductVO;
import in.retalemine.view.constants.BillingConstants;
import in.retalemine.view.event.BillItemSelectionEvent;
import in.retalemine.view.event.CartSelectionEvent;
import in.retalemine.view.event.ProductSelectionEvent;
import in.retalemine.view.event.QuantitySelectionEvent;
import in.retalemine.view.event.RateSelectionEvent;
import in.retalemine.view.event.ResetBillingEvent;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.ui.Button;

public class CartButton extends Button {

	private static final long serialVersionUID = 6926950900398610883L;
	private static final Logger logger = LoggerFactory
			.getLogger(CartButton.class);
	private ProductVO<? extends Quantity> productVO;
	private Amount<Money> unitRate;
	private Measure<Double, ? extends Quantity> netQuantity;
	private BillItemVO<? extends Quantity, ? extends Quantity> billItemVO;

	public CartButton(final EventBus eventBus) {

		logger.info("Initializing {}", getClass().getSimpleName());

		setWidth("100%");
		setCaption(BillingConstants.ADD_TO_CART);
		setEnabled(false);
		setImmediate(true);
		addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -7098852580900652031L;

			@Override
			public void buttonClick(ClickEvent event) {
				logger.info("{} buttonClick", getClass().getSimpleName());
				logger.info("{} posts CartSelectionEvent", getClass()
						.getSimpleName());
				billItemVO = BillItemVO.valueOf(productVO, unitRate,
						netQuantity);
				if (BillingConstants.ADD_TO_CART.equals(getCaption())) {
					eventBus.post(new CartSelectionEvent(billItemVO, true));
				} else {
					eventBus.post(new CartSelectionEvent(billItemVO, false));
				}
				resetCartButton();
			}
		});
	}

	private void resetCartButton() {
		setCaption(BillingConstants.ADD_TO_CART);
		setEnabled(false);
		productVO = null;
		unitRate = null;
		netQuantity = null;
		billItemVO = null;
	}

	@Subscribe
	public void listenProductSelectionEvent(final ProductSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event.toString());
		productVO = ProductVO.valueOf(event.getProductVO().getProductName(),
				event.getProductVO().getProductUnit(), event.getProductVO()
						.getUnitRates());
	}

	@Subscribe
	public void listenRateSelectionEvent(final RateSelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		if (event.getIsNew()) {
			productVO.getUnitRates().add(event.getUnitRate());
			// TODO New/Old product with new rate get pushed to DB (Async)
		}
		unitRate = event.getUnitRate();
	}

	@Subscribe
	public void listenQuantitySelectionEvent(final QuantitySelectionEvent event) {
		logger.info("Event - {} : handler - {} : value - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName(), event);
		netQuantity = event.getNetQuantity();
		setEnabled(true);
		focus();
	}

	@Subscribe
	public void listenBillItemSelectionEvent(final BillItemSelectionEvent event) {
		logger.info("Event - {} : handler - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName());
		if (null != event.getBillItemVO()) {
			setCaption(BillingConstants.UPDATE_CART);
		} else {
			resetCartButton();
		}
	}

	@Subscribe
	public void listenResetBillingEvent(final ResetBillingEvent event) {
		logger.info("Event - {} : handler - {}", event.getClass()
				.getSimpleName(), getClass().getSimpleName());
		resetCartButton();
	}

}
