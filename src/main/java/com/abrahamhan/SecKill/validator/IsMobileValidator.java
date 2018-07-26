package com.abrahamhan.SecKill.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.abrahamhan.SecKill.util.ValidatorUtil;

/**
 * 使用属性验证器实现的判断手机号是否符合规范
 * @author abrahamhan
 *
 */

public class IsMobileValidator implements ConstraintValidator<IsMobile,String>{

	private boolean required = false;
	/**
	 * 获取是否需要这个参数
	 */
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
		
	}
	/**
	 * 进行可用性检查
	 */
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required)
		{
			return ValidatorUtil.isMobile(value);
		}
		else
		{
			if(StringUtils.isEmpty(value))
			{
				return true;
			}
			else
			{
				return ValidatorUtil.isMobile(value);
			}
			
		}
	}
	
}
