package twins.logic.Exceptions;

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
