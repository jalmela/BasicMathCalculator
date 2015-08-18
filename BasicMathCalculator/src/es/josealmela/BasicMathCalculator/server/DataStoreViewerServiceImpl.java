package es.josealmela.BasicMathCalculator.server;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import es.josealmela.BasicMathCalculator.client.DataStoreViewerService;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DataStoreViewerServiceImpl extends RemoteServiceServlet implements DataStoreViewerService {

	public String retrieveDataStoreServer() {
		List<BaseConversion> listBaseConversion = null;// new ArrayList<BaseConversion>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(BaseConversion.class);
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>DateStore Information</h3>");
		
		try {
			listBaseConversion = (List<BaseConversion>) q.execute();
			if (!listBaseConversion.isEmpty()) {
				
			   
				for (BaseConversion bc : listBaseConversion) {
					 sb.append("<dl>");
					sb.append("<dd><dfn>Key</dfn></dd>");
					sb.append("<dt>" + bc.getKey().getId() + "</dt>");
					
					sb.append("<dd><dfn>Input number</dfn></dd>");
					sb.append("<dt>" + bc.getSourceNumber() + "</dt>");
					
					sb.append("<dd><dfn>Result</dfn></dd>");
					sb.append("<dt>" + bc.getResult() + "</dt>");
					
					sb.append("<dd><dfn>Request's date</dfn></dd>");
					sb.append("<dt>" + bc.getDateReq().toString() + "</dt>");
					sb.append("</dl><hr>");
			    }
				
			  } else {
			    sb.append("No data in the DataStore");
			  }	
		} finally {
			q.closeAll();
		}

		return sb.toString();
	}

}
