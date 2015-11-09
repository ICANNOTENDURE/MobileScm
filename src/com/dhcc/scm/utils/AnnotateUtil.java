package com.dhcc.scm.utils;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.dhcc.scm.ui.base.FindView;

/**
 * 
 * @ClassName: AnnotateUtil
 * @Description: TODO(注解工具类)
 * @author zhouxin
 * @date 2015年10月28日 下午7:10:51
 * 
 */
public class AnnotateUtil {
	/**
	 * @param currentClass
	 *            当前类，一般为Activity或Fragment
	 * @param sourceView
	 *            待绑定控件的直接或间接父控件
	 */
	public static void initBindView(Object currentClass, View sourceView) {
		// 通过反射获取到全部属性，反射的字段可能是一个类（静态）字段或实例字段
		Field[] fields = currentClass.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				// 返回BindView类型的注解内容
				FindView findView = field.getAnnotation(FindView.class);
				if (findView != null) {
					int viewId = findView.id();
					boolean clickLis = findView.click();
					try {
						field.setAccessible(true);
						if (clickLis) {
							sourceView.findViewById(viewId).setOnClickListener((OnClickListener) currentClass);
						}
						// 将currentClass的field赋值为sourceView.findViewById(viewId)
						field.set(currentClass, sourceView.findViewById(viewId));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 必须在setContentView之后调用
	 * 
	 * @param aty
	 *            Activity对象
	 */
	public static void initBindView(Activity aty) {
		initBindView(aty, aty.getWindow().getDecorView());
	}

	/**
	 * 必须在setContentView之后调用
	 * 
	 * @param view
	 *            侵入式的view，例如使用inflater载入的view
	 */
	public static void initBindView(View view) {
		Context cxt = view.getContext();
		if (cxt instanceof Activity) {
			initBindView((Activity) cxt);
		} else {
			throw new RuntimeException("view must into Activity");
		}
	}

	/**
	 * 必须在setContentView之后调用
	 * 
	 * @param frag
	 *            要初始化的Fragment
	 */
	public static void initBindView(Fragment frag) {
		initBindView(frag, frag.getActivity().getWindow().getDecorView());
	}
}
