package es.josealmela.BasicMathCalculator.server;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdGeneratorStrategy;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class BaseConversion {

	 @PrimaryKey
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	 private Key key;
	
	@Persistent
	String sourceNumber;
	
	@Persistent
	String result;
	
	 @Persistent
	 Date dateReq;

	public BaseConversion(String sourceNumber, String result, Date dateReq) {
		super();
		this.sourceNumber = sourceNumber;
		this.result = result;
		this.dateReq = dateReq;
	}

	public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getDateReq() {
		return dateReq;
	}

	public void setDateReq(Date dateReq) {
		this.dateReq = dateReq;
	}

	public Key getKey() {
		return key;
	}
	 
}
