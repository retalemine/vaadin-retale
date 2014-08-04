package in.retalemine.view.tryout.eventbus.guava;

public class TestMessageEvent {

	private final String message;

	public TestMessageEvent(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + message;
	}
}
