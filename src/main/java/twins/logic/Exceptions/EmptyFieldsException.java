package twins.logic.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class EmptyFieldsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1241549398487991206L;

	public EmptyFieldsException() {
		super();
	}

	public EmptyFieldsException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyFieldsException(String message) {
		super(message);
	}

	public EmptyFieldsException(Throwable cause) {
		super(cause);
	}
	
	

}
