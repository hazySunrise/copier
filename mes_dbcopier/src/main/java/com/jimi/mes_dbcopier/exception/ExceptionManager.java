package com.jimi.mes_dbcopier.exception;


import com.jimi.mes_dbcopier.constant.ResultCode;
import com.jimi.mes_dbcopier.util.ErrorLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 异常管理器
 */
public class ExceptionManager {

	private static final Logger logger = LogManager.getRootLogger();

	private static final Map<Class, Integer> exceptionMap = new HashMap<>();

	/**
	 * 根据异常实例获取返回码
	 */
	public static int getResultCode(Exception e) {
		try {
			return exceptionMap.get(e.getClass());
		}catch (NullPointerException npe) {
			//如果异常对象找不到类型，就当作500
			return ResultCode.OTHER_SERVER_EXCEPTION_CODE;
		}
	}


	/**
	 * 根据返回码获取异常实例
	 */
	public static Exception getException(int code, String message) {
		for (Entry<Class, Integer> entry : exceptionMap.entrySet()) {
			if(entry.getValue().intValue() == code) {
				try {
					return (Exception) entry.getKey().getConstructor(String.class).newInstance(message);
				} catch (Exception e) {
					ErrorLogger.logError(logger, e);
				}
			}
		}
		//如果异常码无法匹配，就返回运行时异常实例
		return new RuntimeException(message);
	}

}
