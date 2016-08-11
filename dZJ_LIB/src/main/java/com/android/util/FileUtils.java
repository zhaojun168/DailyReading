package com.android.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class FileUtils {

	public static <T> T ReadFromJsonData(byte[] buffer, Class<T> cls) {
		if (null == buffer || cls == null)
			return null;
		String data;
		try {
			data = new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		Gson gson = new Gson();
		T t;

		try {
			t = gson.fromJson(data, cls);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}

		return t;
	}
	
	public static <T> T ReadFromJsonFile(String fn, Class<T> cls) {
		if (null == fn || cls == null)
			return null;
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(fn));
		} catch (FileNotFoundException e1) {
			return null;
		}
		
		Gson gson = new Gson();
		T t;
		
		try {
			t = gson.fromJson(reader, cls);
		} catch (JsonIOException e) {
			e.printStackTrace();
			return null;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		
		return t;
	}

	/**
	 * 获取SD卡的路径
	 */
	public static String getSDPath(){ 
		File sdDir = null;
		//�ж�sd���Ƿ����
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if(sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼
		}
		return sdDir == null ? null : sdDir.toString();
	}
	
	/**
	 * 将数据缓存到文件
	 * 
	 * @param context
	 * @param name
	 *            缓存文件名
	 * @param data
	 *            数据
	 */
	public static void saveDataToFile(Context context, String name, String data) {
		String path = toCachePath(context, name);
		MLog.e("test", "缓存路径path----->" + path);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			fos.write(data.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取缓存数据
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param context
	 * @param name
	 *            缓存文件名称
	 * @param entity
	 *            需要转换为的实体类
	 * @return
	 */
	public static <T>T getCacheData(Context context, String name, Class<T> entity) {
		String path = toCachePath(context, name);
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			return null;
		}

		Gson gson = new Gson();
		T obj;

		try {
			obj = gson.fromJson(reader, entity);
		} catch (JsonIOException e) {
			e.printStackTrace();
			return null;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		return obj;
	}

	/**
	 * 转换缓存文件路径
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static String toCachePath(Context context, String name) {
		return context.getFilesDir().getAbsolutePath() + "/" + name + ".txt";
	}
}
