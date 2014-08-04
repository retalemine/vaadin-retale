package in.retalemine.view.event;

import in.retalemine.view.VO.ProductVO;

import javax.measure.quantity.Quantity;

public class ProductSelectionEvent {

	private final ProductVO<? extends Quantity> productVO;

	public ProductSelectionEvent(ProductVO<? extends Quantity> productVO) {
		this.productVO = productVO;
	}

	public ProductVO<? extends Quantity> getProductVO() {
		return productVO;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<ProductSelectionEvent><productVO>");
		builder.append(productVO);
		builder.append("</productVO></ProductSelectionEvent>");
		return builder.toString();
	}

}
