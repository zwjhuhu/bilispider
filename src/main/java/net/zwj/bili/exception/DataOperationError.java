package net.zwj.bili.exception;

@SuppressWarnings("serial")
public class DataOperationError extends RuntimeException {

	public DataOperationError(String message) {
		super(message);
	}
	
	public DataOperationError(Throwable e) {
		super(e);
	}
	public DataOperationError(String message, Throwable e) {
		super(message, e);
	}

}
