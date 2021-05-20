package twins.logic.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class IllegalOperationType extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2998162935454891437L;

	public IllegalOperationType() {
		super();
	}

	public IllegalOperationType(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalOperationType(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalOperationType(String message) {
		super(message);
	}

	public IllegalOperationType(Throwable cause) {
		super(cause);
	}

}
