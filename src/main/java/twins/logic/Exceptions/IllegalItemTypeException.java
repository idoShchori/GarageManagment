package twins.logic.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class IllegalItemTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5411128486603437487L;

	public IllegalItemTypeException() {
		super();
	}

	public IllegalItemTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalItemTypeException(String message) {
		super(message);
	}

	public IllegalItemTypeException(Throwable cause) {
		super(cause);
	}
	
}

