package com.necor.log.exception;

/**
 * 业务异常Exception
 *
 * @author lixiang
 * @Version 1.0
 * @Time 2024年6月11日 上午11:37:03
 */
public class LogMaxHistoryException extends RuntimeException{

	/** serialVersionUID */
	private static final long serialVersionUID = 3410322618502339938L;

	public LogMaxHistoryException(){
		super();
	}

	public LogMaxHistoryException(String message) {
		super(message);
	}

	public static void maxHistoryException(){
		throw new LogMaxHistoryException("The value of the 'maxHistory' parameter cannot exceed 3650.");
	}
}
