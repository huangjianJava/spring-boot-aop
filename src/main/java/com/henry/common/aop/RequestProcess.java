package com.henry.common.aop;

import java.lang.annotation.*;

/**
 * @author code
 * @version V1.0
 * @Title: 基础代码
 * @Description: 描述
 **/

@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME)    
@Documented
public @interface RequestProcess {
    public boolean checkLogin() default true; //是否检测用户
    
	public String logName() default "";  //日志记录的名称,如为空取 
	
	public String logUrl() default ""; //日志记录的url
	
	public boolean saveRequest() default true;  //请求的参数
	
	public boolean saveResult()  default false; //请求的结果

	public boolean checkDataPopedom()  default false; //校验数据权限
	
	public String popedomCode()  default ""; //功能权限名称
	
	public PopedomType popedomType() default PopedomType.None; //功能权限类型
	
    public int groupFlag() default 0; //分组标志

	public boolean checkSign() default false; //是否需要签名校验
}
