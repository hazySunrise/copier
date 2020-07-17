package com.jimi.mes_dbcopier.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jimi.mes_dbcopier.exception.ExceptionManager;
import com.jimi.mes_dbcopier.util.ErrorLogger;
import com.jimi.mes_dbcopier.util.ResultFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 错误Logger拦截器
 * <br>
 * <b>2018年5月29日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class ErrorLogInterceptor implements Interceptor {

	private static final Logger logger = LogManager.getRootLogger();
	

	@Override
	public void intercept(Invocation invocation) {
		try {
			invocation.invoke();
		}catch (Exception e) {
			ErrorLogger.logError(logger, e);
			invocation.getController().renderJson(ResultFactory.failed(ExceptionManager.getResultCode(e), e.getMessage()));
		}
	}

}
