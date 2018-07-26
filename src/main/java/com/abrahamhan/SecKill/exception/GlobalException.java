package com.abrahamhan.SecKill.exception;

import com.abrahamhan.SecKill.result.CodeMsg;

/**
 * 
 * 全局异常
 * @author abrahamhan
 *
 */
public class GlobalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CodeMsg cm;
	
	public GlobalException(CodeMsg cm)
	{
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() {
		return cm;
	}

	
}
