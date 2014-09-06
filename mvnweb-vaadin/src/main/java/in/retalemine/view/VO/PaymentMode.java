package in.retalemine.view.VO;

public enum PaymentMode {
	CASH("Cash"), CHEQUE("Cheque");

	private String value;

	private PaymentMode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
