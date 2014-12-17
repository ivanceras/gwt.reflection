package com.ivanceras.gwt.reflection.factory;

import com.ivanceras.gwt.reflection.exception.InstanceFactoryException;


/**
 * Will be inherited from a generated class from {@link DAOGenerator#generateModelMetaData}This is only useful for GWT classes, since GWT doesn't support reflection, we will have to make a way to create
 * an instance of the classes.
 * @author lee
 *
 */
public interface BeanFactory {
	
	
	public <T> T getInstance(Class<T> mClass) throws InstanceFactoryException;

	public <T> T[] getArrayInstance(Class<T> mClass, int n) throws InstanceFactoryException;
	
	public void setValue(Object instance, String property, Object value);
	
	public Object getValue(Object instance, String property);

	/**
	 * Get the fields of this Model, easy when using reflection, GWT is supported so this has a separate implementation
	 * @param instance
	 * @return
	 * @throws InstanceProviderException
	 */
	String[] getModelFields(Class<?> clazz) throws InstanceFactoryException;

	Class<?>[] getFieldDataTypes(Class<?> clazz) throws InstanceFactoryException;

	
}
