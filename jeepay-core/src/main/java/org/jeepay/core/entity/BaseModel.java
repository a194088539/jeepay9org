package org.jeepay.core.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p><b>Title: </b>BaseModel.java
 * <p><b>Description: </b>model基类
 * @author terrfly
 * @version V1.0
 * <p>
 */
public class BaseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Map<String,Object> ps ; //ps = 简化Properties
	public Map<String, Object> getPs() {
		return ps;
	}
	public void setPs(Map<String, Object> ps) {
		this.ps = ps;
	}
	
	public void setPsVal(String key, Object value){
		if(ps == null){
			ps = new HashMap<String, Object>();
		}
		ps.put(key, value);
	}
	
	
	public String getPsStringVal(String key){
		if(ps == null){
			return null;
		}
		return (String)ps.get(key);
	}
	
	public Integer getPsIntVal(String key){
		if(ps == null){
			return null;
		}
		return Integer.parseInt((String)ps.get(key));
	}
	
	public Boolean getPsBooleanVal(String key){
		if(ps == null){
			return null;
		}
		return (Boolean)ps.get(key);
	}
	
	public Long getPsLongVal(String key){
		if(ps == null){
			return null;
		}
		return (Long)ps.get(key);
	}
	
}
