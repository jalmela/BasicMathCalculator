package es.josealmela.BasicMathCalculator.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>convertNumberService</code>.
 */
public interface ConverNumberServiceAsync {
  void convertNumbertServer(String input, AsyncCallback<String> callback)
      throws IllegalArgumentException;
}
