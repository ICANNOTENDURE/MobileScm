
package com.dhcc.scm.ui.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
* @ClassName: FindView 
* @Description: TODO(注解式绑定控件) 
* @author zhouxin
* @date 2015年10月28日 下午7:08:28 
*
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindView {
    int id();
    boolean click() default false;
}