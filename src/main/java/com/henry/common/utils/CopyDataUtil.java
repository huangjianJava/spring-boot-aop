
package com.henry.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

public class CopyDataUtil {
	public static <T, V> V copyObject(T source, Class<V> clazz) {
		if (source == null) {
			return null;
		}
		V newObj = null;
		try {
			newObj = clazz.newInstance();
			org.springframework.beans.BeanUtils.copyProperties(source, newObj);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return newObj;
	}

	public static <T, V> List<V> copyList(List<T> list, Class<V> clazz) {
		List<V> data = Lists.newArrayList();
		if (list != null) {
			for (T obj : list) {
				V dto = null;
				try {
					dto = clazz.newInstance();
					org.springframework.beans.BeanUtils.copyProperties(obj, dto);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dto != null) {
					data.add(dto);
				}
			}
			return data;
		} else {
			return null;
		}
	}

	/**
	 * 对一个bean进行深度复制，所有的属性节点全部会被复制
	 * 
	 * @param source
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T deepCopyBean(T source) {
		if (source == null) {
			return null;
		}
		try {
			if (source instanceof Collection) {
				return (T) deepCopyCollection((Collection) source);
			}
			if (source.getClass().isArray()) {
				return (T) deepCopyArray(source);
			}
			if (source instanceof Map) {
				return (T) deepCopyMap((Map) source);
			}
			if (source instanceof Date) {
				return (T) new Date(((Date) source).getTime());
			}
			if (source instanceof Timestamp) {
				return (T) new Timestamp(((Timestamp) source).getTime());
			}
			// 基本类型直接返回原值
			if (source.getClass().isPrimitive() || source instanceof String || source instanceof Boolean
					|| Number.class.isAssignableFrom(source.getClass())) {
				return source;
			}
			if (source instanceof StringBuilder) {
				return (T) new StringBuilder(source.toString());
			}
			if (source instanceof StringBuffer) {
				return (T) new StringBuffer(source.toString());
			}
			Object dest = source.getClass().newInstance();
			BeanUtilsBean bean = BeanUtilsBean.getInstance();
			PropertyDescriptor[] origDescriptors = bean.getPropertyUtils().getPropertyDescriptors(source);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue;
				}

				if (bean.getPropertyUtils().isReadable(source, name)
						&& bean.getPropertyUtils().isWriteable(dest, name)) {
					try {
						Object value = deepCopyBean(bean.getPropertyUtils().getSimpleProperty(source, name));
						bean.getPropertyUtils().setSimpleProperty(dest, name, value);
					} catch (NoSuchMethodException e) {
					}
				}
			}
			return (T) dest;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Collection deepCopyCollection(Collection source)
			throws InstantiationException, IllegalAccessException {
		Collection dest = source.getClass().newInstance();
		for (Object o : source) {
			dest.add(deepCopyBean(o));
		}
		return dest;
	}

	private static Object deepCopyArray(Object source) throws InstantiationException, IllegalAccessException,
			ArrayIndexOutOfBoundsException, IllegalArgumentException {
		int length = Array.getLength(source);
		Object dest = Array.newInstance(source.getClass().getComponentType(), length);
		if (length == 0) {
			return dest;
		}
		for (int i = 0; i < length; i++) {
			Array.set(dest, i, deepCopyBean(Array.get(source, i)));
		}
		return dest;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map deepCopyMap(Map source) throws InstantiationException, IllegalAccessException {
		Map dest = source.getClass().newInstance();
		for (Object o : source.entrySet()) {
			Entry e = (Entry) o;
			dest.put(deepCopyBean(e.getKey()), deepCopyBean(e.getValue()));
		}
		return dest;
	}

	/**
	 * javaBean转Map
	 * 
	 * @param bean
	 * @return
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		if (bean == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(bean);
					if (value != null) {
						map.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("beanToMap Error " + e);
		}
		return map;
	}

	/**
	 * List javaBean转 List Map
	 * @param <T>
	 * 
	 * @param bean
	 * @return
	 */
	public static <T> List<Map<String, Object>> listBeanToMap(List<T> list) {
		if (list == null) {
			return null;
		}

		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		for (Object bean : list) {
			Map<String, Object> beanToMap = beanToMap(bean);
			mapList.add(beanToMap);
		}
		return mapList;
	}

	/**
	 * Map 转 lavaBean
	 * @param <V>
	 * 
	 * @param map
	 * @param bean
	 * @return
	 */
	public static <T, V> T mapToBean(Map<String, V> map, T bean) {
		try {
			BeanUtils.populate(bean, map);
		} catch (Exception e) {
			System.out.println("mapToBean Error " + e);
		}
		return bean;

	}
	
	
	/**
	 * List Map 转 List lavaBean
	 * @param <V>
	 * 
	 * @param map
	 * @param bean
	 * @return
	 */
	public static <T, V> List<T> listMapToBean(List<Map<String, V>> mapList, Class<T> bean) {
		List<T> arrayList = new ArrayList<T>();
		if (mapList != null) {
			for (Map<String, V> map : mapList) {
				T dto = null;
				try {
					dto = bean.newInstance();
					BeanUtils.populate(dto, map);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				arrayList.add(dto);
			}
		}
		return arrayList;

	}

}
