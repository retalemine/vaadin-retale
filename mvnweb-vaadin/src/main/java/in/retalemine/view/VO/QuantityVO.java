package in.retalemine.view.VO;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;

public class QuantityVO {

	private Measure<Double, ? extends Quantity> quantity;

	public Measure<Double, ? extends Quantity> getQuantity() {
		return quantity;
	}

	public void setQuantity(Measure<Double, ? extends Quantity> quantity) {
		this.quantity = quantity;
	}

}
