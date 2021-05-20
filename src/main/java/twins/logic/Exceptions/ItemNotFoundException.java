package twins.logic.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class ItemNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1534001621518790554L;
	
	public ItemNotFoundException() {
	}
	
	public ItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public ItemNotFoundException(String message) {
		super(message);
		
	}

	public ItemNotFoundException(Throwable cause) {
		super(cause);
		
	}
}
