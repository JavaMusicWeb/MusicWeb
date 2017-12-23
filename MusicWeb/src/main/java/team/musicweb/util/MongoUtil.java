package team.musicweb.util;

import static com.mongodb.client.model.Filters.eq;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mongodb.client.MongoCollection;

import team.musicweb.moudle.BaseModle;

public class MongoUtil {

	/**
	 * 将 javabean 转化为 map，适配 mongon.insert method
	 * 
	 * @param obj
	 * @return
	 */
	private static Map<String, Object> Obj2Map(Object obj) {
		Map<String, Object> reMap = new LinkedHashMap<String, Object>();
		if (obj == null)
			return null;
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				try {
					Field f = obj.getClass().getDeclaredField(fields[i].getName());
					f.setAccessible(true);
					Object o = f.get(obj);
					reMap.put(fields[i].getName(), o);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return reMap;
	}

	/***
	 * 向集合插入对象
	 * 
	 * @param collection,插入集合
	 * @param obj，插入对象
	 */
	public static boolean insert(MongoCollection<Document> collection, Object obj) {
		if(obj == null)
			return false;
		Document document = new Document();
		document.putAll(MongoUtil.Obj2Map(obj));
		collection.insertOne(document);
		return true;
	}

	/***
	 * 查询结合唯一一条记录
	 * 
	 * @param collection
	 * @param filter
	 * @return 返回结果对象
	 */
	public static <T> T findOne(MongoCollection<Document> collection, Bson filter, TypeReference<T> type) {
		Document document = collection.find(filter).first();
		return (T) JSON.parseObject(document.toJson(), type);
	}

	public static <T> List<T> findMany(MongoCollection<Document> collection, Bson filter, TypeReference<T> type) {
		List<T> anList = new ArrayList<T>();
		for (Document document : collection.find(filter)) {
			anList.add((T) JSON.parseObject(document.toJson(), type));
		}
		return anList;
	}

	/**
	 * 更新集合中对象 (使用反射实现向下转型，调用子类方法)
	 * 
	 * @param collection
	 * @param t
	 */
	public static <T> boolean update(MongoCollection<Document> collection, T t) {
		if(t== null) return false;
		Document document = new Document();
		document.append("$set", MongoUtil.Obj2Map(t));
		Class<?> clazz = t.getClass();
		Method getId;
		try {
			getId = clazz.getDeclaredMethod("get_id");
			return collection.updateOne(eq("_id", getId.invoke(t).toString()), document).getMatchedCount()>0?true:false;
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除对象 ，使用 Moudle 实现公共接口完成调用子类方法
	 * 
	 * @param collection
	 * @param T
	 */
	public static <T> boolean delete(MongoCollection<Document> collection, BaseModle t) {
		if (t == null)
			return false;
		
		return collection.deleteOne(eq("_id", t.get_id())).getDeletedCount()>0?true:false;
	}

}
