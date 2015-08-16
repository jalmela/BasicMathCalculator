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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
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
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side convert number service.
	 */
	private final ConverNumberServiceAsync convertNumberService = (ConverNumberServiceAsync) GWT.create(ConverNumberService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
    final TextButton convertNumberButton = new TextButton("Convert to binary");
    final TextField calcOpField = new TextField();
   
    final Label errorLabel = new Label();   
    calcOpField.setText("Enter basic op");
    convertNumberButton.setId("convertNumberOp");
    convertNumberButton.setStyleName("opButton", true);
    
    RootPanel.get("nameFieldContainer").add(calcOpField);
    RootPanel.get("sendButtonContainer").add(convertNumberButton);
    RootPanel.get("errorLabelContainer").add(errorLabel);

    // Focus the cursor on the name field when the app loads
    calcOpField.focus();
    calcOpField.selectAll();

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
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          sendNumberToServer();
        }
      }

      /**
       * Send the name from the nameField to the server and wait for a response.
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
        	  AlertMessageBox messageBox = new AlertMessageBox("An error ocurred",SERVER_ERROR);    	
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
		sendNumberToServer();
		
	}
    }

    // Add a handler to send the number to the server
    MyCalcHandlers handler = new MyCalcHandlers();
    convertNumberButton.addSelectHandler((SelectHandler) handler);
    calcOpField.addKeyUpHandler(handler);
  }
}
