package in.retalemine.view.event;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

public class QuantitySelectionEvent {

	private final Measure<Double, ? extends Quantity> netQuantity;

	public QuantitySelectionEvent(
			Measure<Double, ? extends Quantity> netQuantity) {
		this.netQuantity = netQuantity;
	}

	public Measure<Double, ? extends Quantity> getNetQuantity() {
		return netQuantity;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<QuantitySelectionEvent><netQuantity>");
		builder.append(netQuantity);
		builder.append("</netQuantity></QuantitySelectionEvent>");
		return builder.toString();
	}

}
