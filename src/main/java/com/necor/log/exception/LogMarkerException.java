package com.necor.log.exception;

/**
 * 业务异常Exception
 *
 * @author lixiang
 * @Version 1.0
 * @Time 2024年6月11日 上午11:37:03
 */
public class LogMarkerException extends RuntimeException{

	/** serialVersionUID */
	private static final long serialVersionUID = -2210268237399456929L;

	public LogMarkerException(){
		super();
	}

	public LogMarkerException(String message) {
		super(message);
	}

	public static void markerEmptyException(){
		throw new LogMarkerException("The 'marker' parameter in the log configuration cannot be empty.");
	}

	public static void markerLengthException(){
		throw new LogMarkerException("The length of the 'marker' parameter in the log configuration cannot exceed 30 characters.");
	}
}
