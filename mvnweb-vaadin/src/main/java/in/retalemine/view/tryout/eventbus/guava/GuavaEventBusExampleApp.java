package in.retalemine.view.tryout.eventbus.guava;

import com.google.common.eventbus.EventBus;

public class GuavaEventBusExampleApp {

	private EventBus eventBus;

	public GuavaEventBusExampleApp() {
		eventBus = new EventBus();
		eventBus.register(new MessageConsumer(1));
		eventBus.register(new MessageConsumer(2));

		System.out.println("before 1");
		sendString("Hello listeners");
		System.out.println("after 1");
		System.out.println("before 2");
		sendString("How are you doing?");
		System.out.println("after 2");	
	}

	private void sendString(final String message) {
		eventBus.post(new TestMessageEvent(message));
	}
}
