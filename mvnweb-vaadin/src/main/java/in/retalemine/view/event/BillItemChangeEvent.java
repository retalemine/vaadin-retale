package in.retalemine.view.event;

import in.retalemine.view.VO.BillItemVO;

import java.util.List;

import javax.measure.quantity.Quantity;

public class BillItemChangeEvent {

	private final List<BillItemVO<? extends Quantity, ? extends Quantity>> billItems;

	public BillItemChangeEvent(
			List<BillItemVO<? extends Quantity, ? extends Quantity>> billItems) {
		this.billItems = billItems;
	}

	public List<BillItemVO<? extends Quantity, ? extends Quantity>> getBillItems() {
		return billItems;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<BillItemChangeEvent><billItems>");
		builder.append(billItems);
		builder.append("</billItems></BillItemChangeEvent>");
		return builder.toString();
	}

}
