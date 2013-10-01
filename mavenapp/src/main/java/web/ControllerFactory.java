package web;

/**
 * Factory of controllers
 * @author harnyk
 *
 */
public class ControllerFactory {

	/**
	 * Method of routing
	 * @param uri - uri of request
	 * @return Controller
	 */
	public static Controller getController(String uri) {

		if (uri.contains("/hello")) {
			return new HelloController();
		}
		if (uri.contains("/redirect?url=")) { // 14
			return new RedirectController(uri.substring(14));
		}
		if (uri.contains("/status")) {
			return new StatusController();
		}
		return null;
	}

}
