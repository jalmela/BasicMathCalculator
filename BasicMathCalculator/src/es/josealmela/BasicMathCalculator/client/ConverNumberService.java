package es.josealmela.BasicMathCalculator.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side  for the RPC convert service.
 */
@RemoteServiceRelativePath("convertNumber")
public interface ConverNumberService extends RemoteService {
  String convertNumbertServer(String number10) throws IllegalArgumentException;
}
