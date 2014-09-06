package in.retalemine.view.event;


public class PaymentModeSelectionEvent {

	private final String paymentMode;

	public PaymentModeSelectionEvent(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<PaymentModeSelectionEvent><paymentMode>");
		builder.append(paymentMode);
		builder.append("</paymentMode></PaymentModeSelectionEvent>");
		return builder.toString();
	}

}
