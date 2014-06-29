package in.retalemine.view.tryout.print.jcprint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class JcPrintingExample extends VerticalLayout {

	private static final long serialVersionUID = -846352591779377841L;

	public JcPrintingExample() {

		final BrowserFrame pdfBrowserFrame = new BrowserFrame();
		pdfBrowserFrame.setSizeFull();

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(new Button("PDF in popup",
				new Button.ClickListener() {
					private static final long serialVersionUID = 4211563010910549273L;

					@Override
					public void buttonClick(ClickEvent event) {
						removeComponent(pdfBrowserFrame);
						displayPopup();
					}
				}));
		buttonLayout.addComponent(new Button("PDF in mainlayout",
				new Button.ClickListener() {
					private static final long serialVersionUID = 4279152346322733L;

					@Override
					public void buttonClick(ClickEvent event) {
						pdfBrowserFrame.setSource(getPDFStream());
						addComponent(pdfBrowserFrame);
						setExpandRatio(pdfBrowserFrame, 1f);
					}
				}));

		setSizeFull();
		addComponent(buttonLayout);
	}

	private Resource getPDFStream() {
		StreamResource resource = new StreamResource(new Pdf(),
				"TestPrintPDF.pdf?" + System.currentTimeMillis());
		resource.setMIMEType("application/pdf");
		return resource;
	}

	private void displayPopup() {
		Window window = new Window();
		window.setResizable(true);
		window.setWidth("800");
		window.setHeight("600");
		window.center();
		BrowserFrame pdfBrowserFrame = new BrowserFrame();
		pdfBrowserFrame.setSizeFull();
		pdfBrowserFrame.setSource(getPDFStream());
		window.setContent(pdfBrowserFrame);
		UI.getCurrent().addWindow(window);
	}

	public static class Pdf implements StreamSource {
		private static final long serialVersionUID = 7573690047945201893L;
		private final ByteArrayOutputStream os = new ByteArrayOutputStream();

		public Pdf() {
			Document document = null;

			try {
				document = new Document(PageSize.A4, 50, 50, 50, 50);
				PdfWriter.getInstance(document, os);
				document.open();

				document.add(new Paragraph(
						"This is some content for the sample PDF!"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (document != null) {
					document.close();
				}
			}
		}

		@Override
		public InputStream getStream() {
			return new ByteArrayInputStream(os.toByteArray());
		}
	}

}
