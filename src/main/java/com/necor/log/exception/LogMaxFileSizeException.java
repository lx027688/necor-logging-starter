package com.necor.log.exception;

/**
 * 业务异常Exception
 *
 * @author lixiang
 * @Version 1.0
 * @Time 2024年6月11日 上午11:37:03
 */
public class LogMaxFileSizeException extends RuntimeException{

	/** serialVersionUID */
	private static final long serialVersionUID = -2210268237399456929L;

	public LogMaxFileSizeException(){
		super();
	}

	public LogMaxFileSizeException(String message) {
		super(message);
	}

	public static void maxFileSizeException(){
		throw new LogMaxHistoryException("The value of the 'maxFileSize' parameter cannot exceed 3650.");
	}
}
