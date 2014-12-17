package com.ivanceras.gwt.reflection.json;

import com.ivanceras.commons.client.Console;
import com.ivanceras.commons.types.FieldTypes;
import com.ivanceras.gwt.reflection.exception.InstanceFactoryException;
import com.ivanceras.gwt.reflection.factory.BeanFactory;

/**
 * Transform CJSon back to the Model using the beans generated from gwt.reflection
 * @author lee
 *
 */
public class CJsonBean {

	@SuppressWarnings("unchecked")
	public <M> M convert(Class<?> clazz, CJson json, BeanFactory beans) throws InstanceFactoryException{
		if(json.isArray()){
//			Console.log("--->> LOOK an array of HashMap: "+json.toHashMap());
			String[] indexes  = json.getProperties();
			Object[] instances = beans.getArrayInstance(clazz, indexes.length);
			for(int i = 0; i < indexes.length; i++){

				CJson array = json.getValueAsCJson(i);
				instances[i] = beans.getInstance(clazz);
				Object convertedValue = convert(clazz, array, beans);
				instances[i] = convertedValue;
			}
			return (M) instances;
		}
		else{
			
//			Console.log("HashMap Value: "+json.toHashMap());
			Object instance = null;
			String[] fields = null;
			Class<?>[] dataTypes = null;
			instance = beans.getInstance(clazz);
			fields = beans.getModelFields(clazz);
			dataTypes = beans.getFieldDataTypes(clazz);
			for(int i = 0; i < fields.length; i++){
				String property = fields[i];
				Class<?> dt = dataTypes[i];
				Object cvalue = convertSimpleTypes(dt, json, property, beans);
				beans.setValue(instance, property, cvalue);
			}
			return (M) instance;
		}
	}


	/**
	 * Most of the time when has different representation in Javascript
	 * @param record
	 * @return
	 */
	protected Object clarifyNulls(Object record) {
		if(record == null){return null;}
		if(record.equals("null")){return null;}
		if(record.equals("(null)")){return null;}
		if(record.equals("undefined")){return null;}
		else{
			return record;
		}
	}
	


	/**
	 * 
	 * @param dt
	 * @param json
	 * @param property
	 * @param beans
	 * @return
	 * @throws InstanceFactoryException
	 */

	public Object convertSimpleTypes(Class<?> dt, CJson json, String property, BeanFactory beans) throws InstanceFactoryException {
		Object orig = json.getValue(property);
		orig = clarifyNulls(orig);
		if(orig == null){return null;}
		if(FieldTypes.isSimple(dt)){
			Object value = FieldTypes.getValue(dt, orig);
			return value;
		}
		else{
			Console.debug("Threating datatype: "+dt+" as complex.. If you think this is incorrect, check "+FieldTypes.class+" getBasicTypes() and add the class "+dt);
			CJson complexValue = json.getValueAsCJson(property);
			Object ret = convert(dt, complexValue, beans);
			return ret;
		}
	}


}
