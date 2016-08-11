package com.android.util;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StringUtils {
	
	 public static boolean isEmpty(CharSequence str) {
	        if (str == null || str.toString().trim().length() == 0)
	            return true;
	        else
	            return false;
	 }
	 
	 public static List<String> getGsonList(String json){
		 Gson gson = new Gson();
			List<String> list = gson.fromJson(json,
					new TypeToken<List<String>>() {
					}.getType());
			return list;
	 }

}
