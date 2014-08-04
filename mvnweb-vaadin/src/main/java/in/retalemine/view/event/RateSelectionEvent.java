package in.retalemine.view.event;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class RateSelectionEvent {

	private final Amount<Money> unitRate;
	private final Boolean isNew;

	public RateSelectionEvent(Amount<Money> unitRate, Boolean isNew) {
		this.unitRate = unitRate;
		this.isNew = isNew;
	}

	public Amount<Money> getUnitRate() {
		return unitRate;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<RateSelectionEvent><unitRate>");
		builder.append(unitRate);
		builder.append("</unitRate><isNew>");
		builder.append(isNew);
		builder.append("</isNew></RateSelectionEvent>");
		return builder.toString();
	}

}
