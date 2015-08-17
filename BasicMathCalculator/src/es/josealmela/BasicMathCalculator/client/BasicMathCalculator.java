package es.josealmela.BasicMathCalculator.client;

import java.util.Arrays;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
	 * service.
	 */
	private final ConverNumberServiceAsync convertNumberService = (ConverNumberServiceAsync) GWT
			.create(ConverNumberService.class);

	final TextButton convertNumberButton = new TextButton("Convert to binary");
	final TextField calcOpField = new TextField();
	final Label errorLabel = new Label();
	public String firstOperand;
	public String secondOperand;
	public String operator;

	String buttonsOp[] = { "C", "CE", "X", "+", "-", "/", "%", "+/-", "=" };
	String implementedOp[] = { "X", "+", "-", "/", "%" };

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private void clearFirstOp() {
		this.firstOperand = null;
	}

	private void clearSecondOp() {
		this.secondOperand = null;
	}

	private boolean isSetSecondOp() {
		return (this.secondOperand != null && !this.secondOperand.isEmpty());
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
		this.calcOpField.setText(number);
	}

	private String getDisplay() {
		return this.calcOpField.getText();
	}

	private void appendToDisplay(String number) {
		this.calcOpField.setText(this.calcOpField.getText() + number);
	}

	private boolean isAnImplementedOperation(String opeator) {
		return Arrays.asList(this.implementedOp).contains(opeator);
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
			String txt = btn.getText();
			if (txt.equals("Convert to binary")) {
				if (!getDisplay().equals(""))
					sendNumberToServer();
			} else if (txt.equals("C")) {
				resetCurrentOp();
				clearDisplay();
			} else if (txt.equals("CE")) {

				clearDisplay();

			} else if (txt.equals("+/-")) {
				if (getDisplay().equals("") || getDisplay().charAt(0) != '-')
					setDisplay("-" + getDisplay());
				else
					setDisplay(getDisplay().replace("-", ""));
			} else if (txt.equals("=")) {

				if (isSetFirstOp() && isSetOperator() && isNumeric(getDisplay())) {
					secondOperand = getDisplay();
					setDisplay(String.valueOf(evaluateExpresion()));
					operator = null;
					secondOperand = null;
					firstOperand = null;

				}

			} else if (txt.equals(".")) {
				if (getDisplay().equals(""))
					setDisplay("0.");
				else if (!getDisplay().contains("."))
					appendToDisplay(".");
			} else if (Character.isDigit(txt.charAt(0))) {
				appendToDisplay(txt);
			} else {

				if (isAnImplementedOperation(txt) && isNumeric(getDisplay()) && isSetFirstOp()) {
					secondOperand = getDisplay();
					operator = txt;
					acumulateInfirstOp();
					clearDisplay();

				} else if (isAnImplementedOperation(txt) && isNumeric(getDisplay())) {
					firstOperand = getDisplay();
					operator = txt;
					clearDisplay();
				}

			}
			errorLabel.setText("ACTION: " + txt + " op1: " + firstOperand + " op2: " + secondOperand + " operation: "
					+ operator + "isNumericDisplay: " + isNumeric(getDisplay()));

		}
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		MyCalcHandlers calcHandler = new MyCalcHandlers();

		final BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
		FlexTable tableNumbers = new FlexTable();
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

		// northBorderLayOutData.setMargins(new Margins(0, 5, 0, 0));
		northBorderLayOutData.setCollapsible(false);
		northBorderLayOutData.setSplit(false);

		// West panel
		cpWest.setHeaderVisible(false);

		BorderLayoutData westBorderLayOutData = new BorderLayoutData(.07);
		VerticalLayoutContainer vp = new VerticalLayoutContainer();
		VerticalLayoutData vldOp = new VerticalLayoutData(-1, -1, new Margins(0));

		for (String op : buttonsOp) {
			TextButton auxButton = new TextButton(op, calcHandler);
			auxButton.setWidth("100%");
			vp.add(auxButton, vldOp);
		}

		convertNumberButton.setWidth("100%");
		vp.add(convertNumberButton, vldOp);
		cpWest.add(vp);

		// Center Panel
		cpCenter.add(tableNumbers);
		cpCenter.setHeaderVisible(false);
		BorderLayoutData centerBorderLayOutData = new BorderLayoutData(.01);
		centerBorderLayOutData.setMargins(new Margins(0));
		centerBorderLayOutData.setCollapsible(false);
		centerBorderLayOutData.setSplit(false);

		tableNumbers.getElement().getStyle().setProperty("color", "red");

		tableNumbers.setCellSpacing(0);
		tableNumbers.setCellPadding(0);

		for (int i = 1; i < 10; i++) {
			int numRow = ((i - 1) / 3);
			int numCol = ((i % 3 == 0) ? 2 : (i % 3) - 1);
			TextButton auxButtonNumber = new TextButton(String.valueOf(i), calcHandler);

			tableNumbers.setWidget(numRow, numCol, auxButtonNumber);
		}

		tableNumbers.setWidget(4, 0, new TextButton("0", calcHandler));
		tableNumbers.setWidget(4, 1, new TextButton(".", calcHandler));
		cpCenter.add(tableNumbers);

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
