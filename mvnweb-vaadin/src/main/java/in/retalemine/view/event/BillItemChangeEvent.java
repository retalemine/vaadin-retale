package in.retalemine.view.event;

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

public class BillItemChangeEvent {

	private final Amount<Money> subTotal;

	public BillItemChangeEvent(Amount<Money> subTotal) {
		this.subTotal = subTotal;
	}

	public Amount<Money> getSubTotal() {
		return subTotal;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<BillItemChangeEvent><subTotal>");
		builder.append(subTotal);
		builder.append("</subTotal></BillItemChangeEvent>");
		return builder.toString();
	}

}
