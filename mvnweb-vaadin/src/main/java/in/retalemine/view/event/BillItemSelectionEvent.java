package in.retalemine.view.event;

import in.retalemine.view.VO.BillItemVO;

import javax.measure.quantity.Quantity;

public class BillItemSelectionEvent {

	private final BillItemVO<? extends Quantity, ? extends Quantity> billItemVO;

	public BillItemSelectionEvent(
			BillItemVO<? extends Quantity, ? extends Quantity> billItemVO) {
		this.billItemVO = billItemVO;
	}

	public BillItemVO<? extends Quantity, ? extends Quantity> getBillItemVO() {
		return billItemVO;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<BillItemSelectionEvent><billItemVO>");
		builder.append(billItemVO);
		builder.append("</billItemVO></BillItemSelectionEvent>");
		return builder.toString();
	}

}
