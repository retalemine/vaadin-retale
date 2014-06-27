package in.retalemine.view.tryout.browser;

import java.text.DateFormat;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

public class BrowserInformationExample extends VerticalLayout {

	private static final long serialVersionUID = 7795644124357723329L;
	private boolean populated = false;

	public BrowserInformationExample() {
	}

	@Override
	public void attach() {
		if (populated) {
			return;
		}

		WebBrowser webBrowser = Page.getCurrent().getWebBrowser();

		String browserText = getBrowserAndVersion(webBrowser);
		browserText = browserText + " in " + getOperatingSystem(webBrowser);
		Label ipAddresslabel = new Label("Hello user from <b>"
				+ webBrowser.getAddress() + "</b>.", ContentMode.HTML);
		String touchDeviceString = "it is <b>"
				+ (webBrowser.isTouchDevice() ? "" : "not ")
				+ "a touch device</b>";
		Label browser = new Label("You are running <b>" + browserText
				+ "</b> (" + touchDeviceString + ")", ContentMode.HTML);
		Label screenSize = new Label("Your screen resolution is <b>"
				+ webBrowser.getScreenWidth() + "x"
				+ webBrowser.getScreenHeight() + "</b>.", ContentMode.HTML);
		Label locale = new Label("Your browser is set to primarily use the <b>"
				+ webBrowser.getLocale() + "</b> locale.", ContentMode.HTML);

		// Client timezone offset w/o possible DST:
		int rtzOffset = webBrowser.getRawTimezoneOffset();
		// DST:
		int dst = webBrowser.getDSTSavings();
		// use raw offset to get possible TZ:
		String[] tzs = TimeZone.getAvailableIDs(rtzOffset);
		NativeSelect timeZones = new NativeSelect();
		for (String id : tzs) {
			TimeZone tz = TimeZone.getTimeZone(id);
			if (dst == tz.getDSTSavings()) {
				// only include zones w/ DST if we know we have DST
				String caption = id + " (" + tz.getDisplayName() + ")";
				timeZones.addItem(caption);
				if (timeZones.getValue() == null) {
					// select first
					timeZones.setValue(caption);
				}
			}
		}
		timeZones.setImmediate(true);
		timeZones.setNullSelectionAllowed(false);
		timeZones.setCaption(getTimeZoneInfoString(webBrowser));

		DateFormat timeFormatter = DateFormat.getDateTimeInstance();
		Label yourTimeLabel = new Label("Your browser says right now is <b>"
				+ timeFormatter.format(webBrowser.getCurrentDate()) + "</b>",
				ContentMode.HTML);

		SimpleTimeZone timezone = new SimpleTimeZone(
				webBrowser.getTimezoneOffset(), "fake client time zone");
		DateFormat format = DateFormat.getDateTimeInstance();
		format.setTimeZone(timezone);

		addComponent(ipAddresslabel);
		addComponent(browser);
		addComponent(screenSize);
		addComponent(locale);
		addComponent(yourTimeLabel);
		addComponent(timeZones);
		addComponent(new Label(format.format(webBrowser.getCurrentDate())));

		populated = true;
	}

	private String getTimeZoneInfoString(WebBrowser webBrowser) {
		int tzOffset = webBrowser.getTimezoneOffset();
		String infoStr = String.format("Your browser indicates GMT%s%d",
				(tzOffset < 0 ? "-" : "+"), Math.abs(tzoToHours(tzOffset)));
		if (webBrowser.isDSTInEffect()) {
			infoStr += String.format(" and DST %d",
					tzoToHours(webBrowser.getDSTSavings()));
		}
		return infoStr + ", which could mean:";
	}

	private static int tzoToHours(int ms) {
		return ms / 1000 / 60 / 60;
	}

	private String getOperatingSystem(WebBrowser webBrowser) {
		if (webBrowser.isWindows()) {
			return "Windows";
		} else if (webBrowser.isMacOSX()) {
			return "Mac OSX";
		} else if (webBrowser.isLinux()) {
			return "Linux";
		} else {
			return "an unknown operating system";
		}
	}

	private String getBrowserAndVersion(WebBrowser webBrowser) {
		if (webBrowser.isChrome()) {
			return "Chrome " + webBrowser.getBrowserMajorVersion() + "."
					+ webBrowser.getBrowserMinorVersion();
		} else if (webBrowser.isOpera()) {
			return "Opera " + webBrowser.getBrowserMajorVersion() + "."
					+ webBrowser.getBrowserMinorVersion();
		} else if (webBrowser.isFirefox()) {
			return "Firefox " + webBrowser.getBrowserMajorVersion() + "."
					+ webBrowser.getBrowserMinorVersion();
		} else if (webBrowser.isSafari()) {
			return "Safari " + webBrowser.getBrowserMajorVersion() + "."
					+ webBrowser.getBrowserMinorVersion();
		} else if (webBrowser.isIE()) {
			return "Internet Explorer " + webBrowser.getBrowserMajorVersion();
		} else {
			return "an unknown browser";
		}
	}
}