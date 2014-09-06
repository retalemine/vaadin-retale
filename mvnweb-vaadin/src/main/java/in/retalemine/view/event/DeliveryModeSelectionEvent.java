package in.retalemine.view.event;

public class DeliveryModeSelectionEvent {

	private final Boolean isDoorDelivery;

	public DeliveryModeSelectionEvent(Boolean isDoorDelivery) {
		this.isDoorDelivery = isDoorDelivery;
	}

	public Boolean getIsDoorDelivery() {
		return isDoorDelivery;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<DeliveryModeSelectionEvent><isDoorDelivery>");
		builder.append(isDoorDelivery);
		builder.append("</isDoorDelivery></DeliveryModeSelectionEvent>");
		return builder.toString();
	}

}
