package es.josealmela.BasicMathCalculator.client;

import java.util.Arrays;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
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
import es.josealmela.BasicMathCalculator.shared.FieldVerifier;

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
	 * service and view data store.
	 */
	private final ConverNumberServiceAsync convertNumberService = (ConverNumberServiceAsync) GWT
			.create(ConverNumberService.class);
	private final DataStoreViewerServiceAsync dataStoreViewerService = (DataStoreViewerServiceAsync) GWT
			.create(DataStoreViewerService.class);
	
	final TextButton convertNumberButton = new TextButton("Convert to bin");
	final TextField calcOpField = new TextField();
	final Label errorLabel = new Label();
	final Label debugInfo = new Label();
	final TextButton btnViewDataStore = new TextButton("View Datastore");
	
	final BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
	FlexTable tableNumbers = new FlexTable();
	ContentPanel cpNorth = new ContentPanel();
	ContentPanel cpCenter = new ContentPanel();
	Viewport v = new Viewport();
	
	public String firstOperand;
	public String secondOperand;
	public String operator;
	public boolean previousWasOp = false;
	private boolean isShowingPrevResult = false;
	String buttonsOp[] = { "C", "CE", "X", "+", "-", "/", "%", "+/-", "=" };
	String implementedOp[] = { "X", "+", "-", "/", "%" };

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private void resetCurrentOp() {
		this.firstOperand = this.secondOperand = this.operator = null;
	}

	private boolean isSetFirstOp() {
		return (this.firstOperand != null && !this.firstOperand.isEmpty());
	}

	private boolean isSetOperator() {
		return (this.operator != null && !this.operator.isEmpty());

	}

	private void clearDisplay() {
		this.calcOpField.setText("");
	}

	private void setDisplay(String number) {
		isShowingPrevResult = false;
		this.calcOpField.setText(number);
	}

	private String getDisplay() {
		return this.calcOpField.getText();
	}

	private void appendToDisplay(String number) {
		this.calcOpField.setText(this.calcOpField.getText() + number);
	}

	private boolean isAnImplementedOperation(String operator) {
		return Arrays.asList(this.implementedOp).contains(operator);
	}

	private void showMessage(String title, String body) {
		AlertMessageBox messageBox = new AlertMessageBox(title, body);
		
		messageBox.show();
		messageBox.center();
	}

	/**
	 * Evaluate the operation stored in the calculator fields.
	 * 
	 * @return The operation result.
	 */
	private float evaluateExpresion() {
		float op1 = Float.parseFloat(firstOperand);
		float op2 = Float.parseFloat(secondOperand);
		float result = -1;

		if (operator.equals("+")) {
			result = op1 + op2;
		} else if (operator.equals("-")) {
			result = op1 - op2;
		} else if (operator.equals("X")) {
			result = op1 * op2;
		} else if (operator.equals("/")) {
			result = op1 / op2;
		} else if (operator.equals("%")) {
			result = op1 % op2;
		} else {
			// throw new InvalidParameterException("Unknow operation");
			showMessage("Unknown operation", "The operator " + operator + " is not implemented");
		}
		return result;
	}

	/**
	 * Do the operation stored in the op field and store the result in the first
	 * operand.
	 * 
	 * @return the operation's result.
	 */
	private float acumulateInfirstOp() {
		float res = evaluateExpresion();
		secondOperand = null;
		firstOperand = String.valueOf(res);
		return res;
	}

	// Create a handler for the operations in the client side.
	class MyCalcHandlers implements SelectHandler {

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
					showMessage("An error ocurred", SERVER_ERROR);
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
			String lastInput = btn.getText();
			if (lastInput.equals("Convert to bin")) {
				if (!getDisplay().equals(""))
					sendNumberToServer();
				
			} else if (lastInput.equals("C")) {
				resetCurrentOp();
				clearDisplay();
			} else if (lastInput.equals("CE")) {

				clearDisplay();

			} else if (lastInput.equals("+/-")) {
				if (getDisplay().equals("") || getDisplay().charAt(0) != '-')
					setDisplay("-" + getDisplay());
				else
					setDisplay(getDisplay().replace("-", ""));
			} else if (lastInput.equals("=")) {

				if (isSetFirstOp() && isSetOperator() && isNumeric(getDisplay())) {
					secondOperand = getDisplay();
					setDisplay(String.valueOf(evaluateExpresion()));
					resetCurrentOp();
					isShowingPrevResult = true;
				}

			} else if (lastInput.equals(".")) {
				//Always preserve the display
				isShowingPrevResult = false;
				if (getDisplay().equals(""))
					setDisplay("0.");
				else if (!getDisplay().contains("."))
					appendToDisplay(".");
			} else if (Character.isDigit(lastInput.charAt(0))) {
				if(isShowingPrevResult) {setDisplay(lastInput);}
				else appendToDisplay(lastInput);
			} else {
				//If user click two operators we will change the operation to  perform
				if(previousWasOp) operator = lastInput;
				else
					if (isAnImplementedOperation(lastInput) && isNumeric(getDisplay()) && isSetFirstOp()) {
						
						secondOperand = getDisplay();
						 
						setDisplay(String.valueOf(acumulateInfirstOp()));
						isShowingPrevResult = true;
						operator = lastInput;
	
					} else if (isAnImplementedOperation(lastInput) && isNumeric(getDisplay())) {
						firstOperand = getDisplay();
						operator = lastInput;
						clearDisplay();
					}

			}
			debugInfo(lastInput);
			previousWasOp = isAnImplementedOperation(lastInput);
		}

		private void debugInfo(String lastInput) {
			debugInfo.setText("ACTION: " + lastInput + " op1: " + firstOperand + " op2: " + secondOperand + " operation: "
					+ operator + "isNumericDisplay: " + isNumeric(getDisplay()));
		}
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		MyCalcHandlers calcHandler = new MyCalcHandlers();

		calcOpField.setReadOnly(true);
		calcOpField.setId("calcOpField");
		convertNumberButton.setId("convertNumberOp");

		// North panel
		BorderLayoutData northBorderLayOutData = new BorderLayoutData();
		BorderLayoutData centerBorderLayOutData = new BorderLayoutData(.01);
		cpNorth.setHeadingText("Result");
		cpNorth.setHeaderVisible(false);
		cpNorth.setId("pnlNorth");
		cpNorth.add(calcOpField);

		// northBorderLayOutData.setMargins(new Margins(0, 5, 0, 0));
		northBorderLayOutData.setCollapsible(false);
		northBorderLayOutData.setSplit(false);

		// Center Panel
		cpCenter.add(tableNumbers);
		cpCenter.setHeaderVisible(false);
		
		centerBorderLayOutData.setMargins(new Margins(0));
		centerBorderLayOutData.setCollapsible(false);
		centerBorderLayOutData.setSplit(false);

		tableNumbers.getElement().getStyle().setProperty("color", "red");
		tableNumbers.setCellSpacing(4);
		tableNumbers.setCellPadding(0);

		for (int i = 1; i < 10; i++) {
			int numRow = ((i - 1) / 3);
			int numCol = ((i % 3 == 0) ? 2 : (i % 3) - 1);
			TextButton auxButtonNumber = new TextButton(String.valueOf(i), calcHandler);
			auxButtonNumber.setWidth(80);
			auxButtonNumber.setHeight(80);
			tableNumbers.setWidget(numRow, numCol, auxButtonNumber);
		}
		
		for (int i = 0; i < buttonsOp.length; i++) {
			String op = buttonsOp[i];
			int numRow = ((i) / 3);
			int numCol = ((i % 3 == 0) ? 3 : (i % 3) + 3);
			//{ "C", "CE", "X", "+", "-", "/", "%", "+/-", "=" }
			TextButton auxButton = new TextButton(op, calcHandler);
			auxButton.setWidth(80);
			auxButton.setHeight(80);
			
			tableNumbers.setWidget(numRow, numCol, auxButton);			
		}
		

		
		TextButton auxButton = new TextButton("0", calcHandler);
		auxButton.setWidth(80);
		auxButton.setHeight(80);
		tableNumbers.setWidget(3, 0,  auxButton);
		auxButton = new TextButton(".", calcHandler);
		auxButton.setWidth(80);
		auxButton.setHeight(80);
		tableNumbers.setWidget(3, 1,auxButton);
		btnViewDataStore.setWidth(80);
		btnViewDataStore.setHeight(80);
		tableNumbers.setWidget(3, 2, btnViewDataStore);
		convertNumberButton.setWidth(80);
		convertNumberButton.setHeight(80);
		tableNumbers.setWidget(3, 3, convertNumberButton);
		
		
		cpCenter.add(tableNumbers);

		borderLayoutContainer.setNorthWidget(cpNorth, northBorderLayOutData);
		borderLayoutContainer.setCenterWidget(cpCenter, centerBorderLayOutData);

		
		//v.setWidth(500);
		cpNorth.setPixelSize(200, 50);
		cpCenter.setPixelSize(200, 300);
		borderLayoutContainer.setPixelSize(200,300);
		v.setPixelSize(400, 300);
		v.add(borderLayoutContainer);
		
		RootPanel.get("errorLabelContainer").add(errorLabel);
		if(Window.Location.getParameter("debug") == "1") RootPanel.get("errorLabelContainer").add(debugInfo);
		RootPanel.get("calcContainer").add(v);
		//RootPanel.get("btnViewDataStoreContainer").add(btnViewDataStore);
		// Add a handler to send the number to the server
		convertNumberButton.addSelectHandler((SelectHandler) calcHandler);
		
		//Listen for mouse event. A Handler independent to calculator's events handler
		btnViewDataStore.addSelectHandler(new SelectHandler() {
			
			public void onSelect(SelectEvent event) {
				ObtainDataStore();				
			}

			private void ObtainDataStore() {

				errorLabel.setText("");

				// Then, we send the input to the server.
				btnViewDataStore.setEnabled(false);
				dataStoreViewerService.retrieveDataStoreServer(new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						showMessage("An error ocurred in RPC retrieveDataStore", SERVER_ERROR);
						btnViewDataStore.setEnabled(true);
					}

					public void onSuccess(String result) {					
						showMessage("Data store viewe", result);
						btnViewDataStore.setEnabled(true);
					}
					
				});
				
			}
		});

	}
}
