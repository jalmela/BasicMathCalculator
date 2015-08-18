package es.josealmela.BasicMathCalculator.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side  for the RPC convert service.
 */
@RemoteServiceRelativePath("retrieveDataStore")
public interface DataStoreViewerService extends RemoteService {
  String retrieveDataStoreServer();
}
