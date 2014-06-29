package in.retalemine.view.tryout.print;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class PrintingExample extends CustomComponent {
	private static final long serialVersionUID = 97529549237L;

	public PrintingExample(String context) {
		init(context);
	}

	public void init(String context) {
		VerticalLayout layout = new VerticalLayout();

		if ("this".equals(context))
			printThisPage();
		else if ("open".equals(context))
			printOpenedPage();
		else if ("nonblocking".equals(context))
			printNonblockingPage();
		else if ("pdfgeneration".equals(context))
			pdfgeneration(layout);
		else
			setCompositionRoot(new Label("Invalid Context"));

		if (getCompositionRoot() == null)
			setCompositionRoot(layout);
	}

	void printThisPage() {
		Button print = new Button("Print This Page");
		print.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 15335453452L;

			public void buttonClick(ClickEvent event) {
				JavaScript.getCurrent().execute("print();");
			}
		});
		setCompositionRoot(print);
	}

	public static class PrintUI extends UI {
		private static final long serialVersionUID = -4265213983602980250L;

		@Override
		protected void init(VaadinRequest request) {
			setContent(new Label("<h1>Here's some dynamic content</h1>\n"
					+ "<p>This is to be printed.</p>", ContentMode.HTML));

			JavaScript.getCurrent().execute(
					"setTimeout(function() {"
							+ "  print(); self.close();}, 2000);");
		}
	}

	void printOpenedPage() {
		BrowserWindowOpener opener = new BrowserWindowOpener(PrintUI.class);
		opener.setFeatures("height=200,width=400,resizable");

		Button print = new Button("Click to Print");
		opener.extend(print);

		setCompositionRoot(print);
	}

	// TODO: This actually blocks also.
	void printNonblockingPage() {
		final Button print = new Button("Click to Print");

		print.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 349852897523897L;

			public void buttonClick(ClickEvent event) {
				String content = "<h1>Stuff to Print</h1>\\n"
						+ "<p>Important stuff</p>\\n";

				// The code to print and close the window
				content += "<SCRIPT language=\"JavaScript\">" + "  print();"
						+ "  close();" + "</SCRIPT>";

				String js = "popup = window.open('', 'mywindow','status=1,width=350,height=150');\n"
						+ "popup.document.write('" + content + "');\n";
				JavaScript.getCurrent().execute(js);
			}
		});

		setCompositionRoot(print);
	}

	public final static String pdfgenerationDescription = "<h1>Generating a Printable PDF</h1>"
			+ "<p>You can generate a PDF file dynamically using a <b>StreamResource</b>. The following example does it using the Apache FOP.</p>";

	/** Generates the PDF dynamically when requested by HTTP. */
	class MyPdfSource implements StreamSource {
		private static final long serialVersionUID = 6580720404794033932L;

		String name;

		public MyPdfSource(String name) {
			this.name = name;
		}

		@Override
		public InputStream getStream() {
			String fo = "<?xml version='1.0' encoding='ISO-8859-1'?>\n"
					+ "<fo:root xmlns:fo='http://www.w3.org/1999/XSL/Format'>\n"
					+ "<fo:layout-master-set>"
					+ "  <fo:simple-page-master master-name='A4' margin='2cm'>"
					+ "    <fo:region-body />" + "  </fo:simple-page-master>"
					+ "</fo:layout-master-set>"
					+ "<fo:page-sequence master-reference='A4'>"
					+ "    <fo:flow flow-name='xsl-region-body'>"
					+ "    <fo:block text-align='center'>" + "Hello There, "
					+ name + "!</fo:block>" + "  </fo:flow>"
					+ "</fo:page-sequence>" + "</fo:root>\n";
			ByteArrayInputStream foStream = new ByteArrayInputStream(
					fo.getBytes());

			// Basic FOP configuration. You could create this object
			// just once and keep it.
			FopFactory fopFactory = FopFactory.newInstance();
			fopFactory.setStrictValidation(false); // For an example

			FOUserAgent userAgent = fopFactory.newFOUserAgent();
			userAgent.setProducer("My Vaadin Application");
			userAgent.setCreator("Me, Myself and I");
			userAgent.setAuthor("Da Author");
			userAgent.setCreationDate(new Date());
			userAgent.setTitle("Hello to " + name);
			userAgent.setKeywords("PDF Vaadin example");
			userAgent.setTargetResolution(300); // DPI

			// Transform to PDF
			ByteArrayOutputStream fopOut = new ByteArrayOutputStream();
			try {
				Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent,
						fopOut);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();
				Source src = new javax.xml.transform.stream.StreamSource(
						foStream);
				Result res = new SAXResult(fop.getDefaultHandler());
				transformer.transform(src, res);
				fopOut.close();
				return new ByteArrayInputStream(fopOut.toByteArray());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	void pdfgeneration(VerticalLayout layout) {
		final TextField name = new TextField("Name");
		name.setValue("Slartibartfast");

		final Button ok = new Button("OK");

		final Button print = new Button("Open PDF");
		print.setEnabled(false);

		ok.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -2989367937320174397L;

			@Override
			public void buttonClick(ClickEvent event) {
				StreamSource source = new MyPdfSource((String) name.getValue());

				String filename = "pdf_printing_example.pdf";
				StreamResource resource = new StreamResource(source, filename);

				resource.setMIMEType("application/pdf");
				resource.getStream().setParameter("Content-Disposition",
						"attachment; filename=" + filename);

				BrowserWindowOpener opener = new BrowserWindowOpener(resource);
				opener.extend(print);

				name.setEnabled(false);
				ok.setEnabled(false);
				print.setEnabled(true);
			}
		});

		print.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5413419737626607326L;

			@Override
			public void buttonClick(ClickEvent event) {
				name.setEnabled(true);
				ok.setEnabled(true);
				print.setEnabled(false);
			}
		});

		layout.addComponent(name);
		layout.addComponent(ok);
		layout.addComponent(print);
	}
}
