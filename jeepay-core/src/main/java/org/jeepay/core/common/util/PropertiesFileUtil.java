package org.jeepay.core.common.util;

import java.util.ResourceBundle;

/**
 * @Description: 属性文件工具类
 * @author aragom qq194088539
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
public class PropertiesFileUtil {

	private ResourceBundle rb = null;

	public PropertiesFileUtil(String bundleFile) {
		rb = ResourceBundle.getBundle(bundleFile);
	}

	public String getValue(String key) {
		return rb.getString(key);
	}

	public static void main(String[] args) {


	}
}
