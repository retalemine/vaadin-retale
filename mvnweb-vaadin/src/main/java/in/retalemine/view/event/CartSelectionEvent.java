package in.retalemine.view.event;

import in.retalemine.view.VO.BillItemVO;

import javax.measure.quantity.Quantity;

public class CartSelectionEvent {

	private final BillItemVO<? extends Quantity, ? extends Quantity> billItemVO;
	private final Boolean isNew;

	public CartSelectionEvent(
			BillItemVO<? extends Quantity, ? extends Quantity> billItemVO,
			Boolean isNew) {
		this.billItemVO = billItemVO;
		this.isNew = isNew;
	}

	public BillItemVO<? extends Quantity, ? extends Quantity> getBillItemVO() {
		return billItemVO;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<CartSelectionEvent><billItemVO>");
		builder.append(billItemVO);
		builder.append("</billItemVO><isNew>");
		builder.append(isNew);
		builder.append("</isNew></CartSelectionEvent>");
		return builder.toString();
	}

}
