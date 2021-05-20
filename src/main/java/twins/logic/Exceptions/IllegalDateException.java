package twins.logic.Exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IllegalDateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5719017138962348931L;

	public IllegalDateException() {
	}

	public IllegalDateException(String message, Throwable cause) {
		super(message, cause);

	}

	public IllegalDateException(String message) {
		super(message);

	}

	public IllegalDateException(Throwable cause) {
		super(cause);

	}
}
