package in.retalemine.view.event;

public class TaxSelectionEvent {

	private final double percent;

	public TaxSelectionEvent(double percent) {
		this.percent = percent;
	}

	public double getPercent() {
		return percent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<TaxSelectionEvent><percent>");
		builder.append(percent);
		builder.append("</percent></TaxSelectionEvent>");
		return builder.toString();
	}

}
