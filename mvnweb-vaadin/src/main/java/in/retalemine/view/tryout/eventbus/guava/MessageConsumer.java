package in.retalemine.view.tryout.eventbus.guava;

import com.google.common.eventbus.Subscribe;

public class MessageConsumer {

	private final int id;

	public MessageConsumer(final int id) {
		this.id = id;
	}

	@Subscribe
	public void receiveTestMessage(final TestMessageEvent event) {
		final String message = event.getMessage();

		System.out.println("[MSG] " + this + " got the message \"" + message
				+ "\"");
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + id;
	}

}
