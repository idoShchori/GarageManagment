package twins.logic;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserAccessDeniedException extends RuntimeException{

	private static final long serialVersionUID = -1649599973035894997L;

	public UserAccessDeniedException() {
		super();
	}

	public UserAccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAccessDeniedException(String message) {
		super(message);
	}

	public UserAccessDeniedException(Throwable cause) {
		super(cause);
	}
}
