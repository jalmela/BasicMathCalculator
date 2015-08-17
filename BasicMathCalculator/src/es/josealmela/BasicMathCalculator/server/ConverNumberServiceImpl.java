package es.josealmela.BasicMathCalculator.server;

import es.josealmela.BasicMathCalculator.client.ConverNumberService;
import es.josealmela.BasicMathCalculator.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ConverNumberServiceImpl extends RemoteServiceServlet implements
    ConverNumberService {

  public String convertNumbertServer(String input) throws IllegalArgumentException {
    // Verify that the input is valid. 
    if (!FieldVerifier.isValidNumber(input)) {
      // If the input is not valid, throw an IllegalArgumentException back to
      // the client.
      throw new IllegalArgumentException(
          "Name must be a positive integer");
    }

     return  Integer.toBinaryString(Integer.parseInt(input));
  }

}
