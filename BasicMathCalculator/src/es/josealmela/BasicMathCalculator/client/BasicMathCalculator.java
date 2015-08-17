package es.josealmela.BasicMathCalculator.client;

import es.josealmela.BasicMathCalculator.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BasicMathCalculator implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side convert number
	 * service.
	 */
	private final ConverNumberServiceAsync convertNumberService = (ConverNumberServiceAsync) GWT
			.create(ConverNumberService.class);

	// Create a handler for the operations in the client side.
	class MyCalcHandlers implements ClickHandler, KeyUpHandler, SelectHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {

		}

		/**
		 * Fired when the user types in the nameField.
		 */
		public void onKeyUp(KeyUpEvent event) {
			TextButton btn = (TextButton) event.getSource();
			String txt = btn.getText();

			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				sendNumberToServer();
			}
		}

		/**
		 * Send the name from the nameField to the server and wait for a
		 * response.
		 */
		private void sendNumberToServer() {
			// First, we validate the input.
			errorLabel.setText("");
			String textToServer = calcOpField.getText();
			if (!FieldVerifier.isValidNumber(textToServer)) {
				errorLabel.setText("Please enter a positive integer number.");
				return;
			}

			// Then, we send the input to the server.
			convertNumberButton.setEnabled(false);
			convertNumberService.convertNumbertServer(textToServer, new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					AlertMessageBox messageBox = new AlertMessageBox("An error ocurred", SERVER_ERROR);
					messageBox.show();
					messageBox.center();
					convertNumberButton.setEnabled(true);
				}

				public void onSuccess(String result) {
					calcOpField.setText(result);
					convertNumberButton.setEnabled(true);
				}
			});
		}

		public void onSelect(SelectEvent event) {
			TextButton btn = (TextButton) event.getSource();
			String txt = btn.getText();
			if (txt.equals("Convert to binary")) {

			} else if (txt.equals("C")) {

			} else if (txt.equals("CE")) {

			} else if (txt.equals("X")) {

			} else if (txt.equals("+")) {

			} else if (txt.equals("-")) {

			} else if (txt.equals("+/-")) {

			} else if (txt.equals("0")) {

			} else if (txt.equals("1")) {

			} else if (txt.equals("2")) {

			} else if (txt.equals("3")) {

			} else {

			}

			sendNumberToServer();

		}
	}

	final TextButton convertNumberButton = new TextButton("Convert to binary");
	final TextField calcOpField = new TextField();
	final Label errorLabel = new Label();
	public float firstOperand;
	public float secondOperand;
	public String operator;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		MyCalcHandlers calcHandler = new MyCalcHandlers();
		String implentedOp[] = {"X", "+", "-", "/", "C", "CE", "+/-"};
		final BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
		FlexTable table = new FlexTable();
		ContentPanel cpNorth = new ContentPanel();
	    ContentPanel cpWest = new ContentPanel();
	    ContentPanel cpCenter = new ContentPanel();

		calcOpField.setReadOnly(true);
		convertNumberButton.setId("convertNumberOp");
						
		// North panel
		BorderLayoutData northBorderLayOutData = new BorderLayoutData(.03);
		
		cpNorth.setHeadingText("Result");
		cpNorth.setHeaderVisible(false);
		cpNorth.add(calcOpField);
			
		//northBorderLayOutData.setMargins(new Margins(0, 5, 0, 0));
		northBorderLayOutData.setCollapsible(false);
		northBorderLayOutData.setSplit(false);
		
		// West panel
		cpWest.setHeaderVisible(false);
		
		BorderLayoutData westBorderLayOutData = new BorderLayoutData(.07);
		VerticalLayoutContainer vp = new VerticalLayoutContainer();
		VerticalLayoutData vldOp = new VerticalLayoutData(-1, -1, new Margins(0));

		for (String op : implentedOp) {
			TextButton auxButton = new TextButton(op, calcHandler);
			auxButton.setWidth("100%");
			vp.add(auxButton, vldOp);
		}
		
		convertNumberButton.setWidth("100%");
		vp.add(convertNumberButton, vldOp);
		
		cpWest.add(vp);
		

		// Center Panel
		
		cpCenter.add(table);
		cpCenter.setHeaderVisible(false);
		BorderLayoutData centerBorderLayOutData = new BorderLayoutData(.01);
		centerBorderLayOutData.setMargins(new Margins(0));
		centerBorderLayOutData.setCollapsible(false);
		centerBorderLayOutData.setSplit(false);
		
		table.getElement().getStyle().setProperty("color", "red");
		
		table.setCellSpacing(0);
		table.setCellPadding(0);	

		for (int i = 1; i < 10; i++) {
			int numRow = ((i-1)/3);
			int numCol = ((i % 3 == 0)? 2 : (i% 3) - 1);
			TextButton auxButtonNumber = new TextButton(String.valueOf(i), calcHandler);
			
			table.setWidget(numRow, numCol, auxButtonNumber);
		}
		
		table.setWidget(4, 0, new TextButton("0", calcHandler));
		table.setWidget(4, 1, new TextButton(".", calcHandler));
		cpCenter.add(table);
		
		
		
		borderLayoutContainer.setWestWidget(cpWest, westBorderLayOutData);
		borderLayoutContainer.setNorthWidget(cpNorth, northBorderLayOutData);
		borderLayoutContainer.setCenterWidget(cpCenter, centerBorderLayOutData);

		Viewport v = new Viewport();
		v.add(borderLayoutContainer);

		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("calcContainer").add(v);

		// Add a handler to send the number to the server		
		convertNumberButton.addSelectHandler((SelectHandler) calcHandler);
				
	}
}
