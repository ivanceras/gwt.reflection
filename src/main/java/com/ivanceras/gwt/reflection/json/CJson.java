package com.ivanceras.gwt.reflection.json;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.ivanceras.commons.client.Console;
import com.ivanceras.commons.types.FieldTypes;

/**
 * As a replacement of JSONObject since it is buggy, especially on the array
 * Stands for Custom JSON
 * @author lee
 *
 */
public final class CJson extends JavaScriptObject {

	protected CJson(){

	}
	
	private final native JavaScriptObject getNativeProperties()/*-{
		if(typeof this == "undefined"){
			return null;
		}
		var keys = Object.keys(this);
		return keys;
	}-*/;
	

	private final native Object getNativeValue(Object property)/*-{
		var val = this[property];
		if(typeof val == "undefined"){
			return null;
		}
		return val;
	}-*/;

	public String[] getProperties() {
		JavaScriptObject obj = getNativeProperties();
		JsArrayString array = obj.cast();
		int len = array.length();
		String[] properties = new String[len];
		for(int i = 0; i < len; i++){
			properties[i] = array.get(i);
		}
		return properties;
	}

	public Object getValue(int index){
		Object ret = getNativeValue(index);
//		Console.log("getValue("+index+") = "+ret+" "+(ret!=null?ret.getClass():null));
		return ret;
	}

	public Object getValue(String property){
		Object ret = getNativeValue(property);
//		Console.log("getValue("+property+") = "+ret+" "+(ret!=null?ret.getClass():null));
		return ret;
	}
	
	public final native CJson getValueAsCJson(Object property)/*-{
		var val = this[property];
		if(typeof val == "undefined"){
			return null;
		}
		return val;
	}-*/;
	
	public final native boolean isArray()/*-{
	if (this instanceof Array) {
			return true;
		} else {
			return false;
		}
	}-*/;
	
	private final native void setNativeValue(Object property, Object value)/*-{
		this[property] = value;
	}-*/;

	public final void setValue(String property, Object value){
		setNativeValue(property, value);
	}

	public final native String stringify()/*-{
		return JSON.stringify(this);
	}-*/;
	
	
	/**
	 * Converts CJSOn to HashMap
	 * @return
	 */
	public HashMap<String, Object> toHashMap(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] properties = getProperties();
		for(String prop : properties){
			Object value = getValue(prop);
			map.put(prop, value);
		}
		return map;
	}
	
	/**
	 * Creates a CJSON object from hashmap;
	 */
	public void fromHashMap(HashMap<String, Object> map){
		for(Entry<String, Object> entry : map.entrySet()){
			String property = entry.getKey();
			Object value = entry.getValue();
			setValue(property, value);
		}
	}
	
	private static native CJson createCJsonArray(int size)/*-{
		return new Array(size);
	}-*/;

	
	/**
	 * Most of the time when has different representation in Javascript
	 * @param record
	 * @return
	 */
	protected static Object clarifyNulls(Object record) {
		if(record == null){return null;}
		if(record.equals("null")){return null;}
		if(record.equals("(null)")){return null;}
		if(record.equals("undefined")){return null;}
		else{
			return record;
		}
	}
	
	public static CJson fromArray(Object[] parameters) {
		if(parameters == null){return null;}
		CJson array = (CJson) createArray(parameters.length);
		for(int i = 0; i < parameters.length; i++){
			Object param = parameters[i];
//			Console.log("now setting value: "+i+" : "+(param != null? param.toString():"null"));
			array.setNativeValue(i, param);
		}
//		Console.log("array now is: "+array.toHashMap());
		return array;
	}

}