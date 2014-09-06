package in.retalemine.view.event;

import in.retalemine.view.VO.TaxVO;

public class TaxSelectionEvent {

	private final TaxVO taxVO;

	public TaxSelectionEvent(TaxVO taxVO) {
		this.taxVO = taxVO;
	}

	public TaxVO getTaxVO() {
		return taxVO;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<TaxSelectionEvent><taxVO>");
		builder.append(taxVO);
		builder.append("</taxVO></TaxSelectionEvent>");
		return builder.toString();
	}
}
