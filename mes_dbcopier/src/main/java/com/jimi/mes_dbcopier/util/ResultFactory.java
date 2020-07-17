package com.jimi.mes_dbcopier.util;

/**
 * 返回一个带result字段和data字段的json，result为succeed时，data为正常数据；result为failed时，data为错误信息
 * 
 */
public class ResultFactory {

	private int result;
	private Object data;


	public static ResultFactory succeed() {
		return succeed("操作成功");
	}


	public static ResultFactory succeed(Object data) {
		ResultFactory resultFactory = new ResultFactory();
		resultFactory.result = 200;
		resultFactory.data = data;
		return resultFactory;
	}


	public static ResultFactory failed() {
		return failed("操作失败");
	}


	public static ResultFactory failed(int result) {
		return failed(result, "操作失败");
	}


	public static ResultFactory failed(Object errorMsg) {
		return failed(500, errorMsg);
	}


	public static ResultFactory failed(int result, Object errorMsg) {
		ResultFactory resultFactory = new ResultFactory();
		resultFactory.result = result;
		resultFactory.data = errorMsg;
		return resultFactory;
	}


	public int getResult() {
		return result;
	}


	public void setResult(int result) {
		this.result = result;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}

}
