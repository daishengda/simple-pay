package com.simple.pay.service.notify.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;

/**
 * GSON工具类
 * 
 * @author daishengda
 *
 */
public class GSONUtil {

	private static final Gson GSON = new Gson();

	public static <T> T fromJson(String json, Class<T> classOfT) {
		return GSON.fromJson(json, classOfT);
	}

	public static <T> T fromJson(String json, Type typeofT) {
		return GSON.fromJson(json, typeofT);
	}
	
	public static String toJson(Object obj)
	{
		return GSON.toJson(obj);
	}
}
