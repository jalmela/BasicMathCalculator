package es.josealmela.BasicMathCalculator.shared;

/**
 * <p>
 * FieldVerifier validates that the number the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client-side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * 
	 * @param number10 the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidNumber(String number10) {
		int auxInt = -1;
		if (number10 == null) {
			return false;
		}
		else {
			try {
				auxInt = Integer.parseInt(number10);
			} catch (Exception e) {
				return false;
			}
			if(auxInt < 0) return false;		
		}
		return true;
	}
}
