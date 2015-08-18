package es.josealmela.BasicMathCalculator.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>retrieveDataStoreServer</code>.
 */
public interface DataStoreViewerServiceAsync {
  void retrieveDataStoreServer(AsyncCallback<String> callback);
}
