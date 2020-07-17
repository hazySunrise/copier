package com.jimi.mes_dbcopier.util;

import com.jimi.mes_dbcopier.exception.ExceptionManager;
import org.apache.logging.log4j.Logger;

/**
 * 帮助记录不同级别的错误
 */
public class ErrorLogger {
	
	public static void logError(Logger logger, Exception e) {
		//获取错误码
		int code = ExceptionManager.getResultCode(e);
		//如果是5XX，要记录错误栈
		if(String.valueOf(code).startsWith("5")) {
			String stackString = getErrorStackString(e);
			logger.error(e.getClass().getSimpleName() + " : " + e.getMessage() + stackString);
		}else if(String.valueOf(code).startsWith("2")){ //用户的错误记录info级别
			logger.info(e.getClass().getSimpleName() + " : " + e.getMessage());
		}else {
			logger.warn(e.getClass().getSimpleName() + " : " + e.getMessage());
		}
	}
	
	
	private static String getErrorStackString(Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			sb.append("\n");
			sb.append(stackTraceElement.toString());
		}
		return sb.toString();
	}


}
