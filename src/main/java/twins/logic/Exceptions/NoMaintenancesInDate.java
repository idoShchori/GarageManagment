package twins.logic.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT)
public class NoMaintenancesInDate extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2659573305827678382L;

	public NoMaintenancesInDate() {
		super();
	}

	public NoMaintenancesInDate(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoMaintenancesInDate(String message, Throwable cause) {
		super(message, cause);
	}

	public NoMaintenancesInDate(String message) {
		super(message);
	}

	public NoMaintenancesInDate(Throwable cause) {
		super(cause);
	}

}
