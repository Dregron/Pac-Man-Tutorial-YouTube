package com.dregronprogram.json_parse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class JsonParser {

	private Object object;
	private int index = 0, counter = 0;
	private char[] addCharToFilter = {'{', '['};
	private char[] removeCharFromFilters = {'}', ']'};
	private List<Character> characterFilter = new ArrayList<Character>();
	
	public <T extends Object> T readValue(InputStream inputStream, Class<T> object) {
		
		try {
			this.object = object.newInstance();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			
			String value = "";
			String string = "";
			while ((value = br.readLine()) != null) {
				string += value.replaceAll("\\s", "").replaceAll("\\r", "").replaceAll("\\t", "");
			}
			
			char[] cs = string.toCharArray();
			for (index = 0;  index< cs.length; index++) {
				char c = cs[index];
				String id = getId(c, cs, string, '\"', '\"');
				
				if (!id.isEmpty()) {
					System.err.println(id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return object.cast(getObject());
	}
	
	private void setFields(String value, char[] characters, char currentChar) throws Exception {
		
//		String v = getId(currentChar, characters, value, ':', ',');

//			String idAndValues = getIdAndValues('\"', characters, value,  ',');
//			System.err.println(idAndValues);
//			if (idAndValues.length() > 0 && idAndValues.replaceAll("\\s", "").length() > 0) {
//				String id = idAndValues.split(":")[0];
//				String val = idAndValues.split(":")[1];
//				
//				Method method = getMethod(object, id);
//				Field field = getField(object, id);
//				
//				if (method != null && field != null) {
//					method.invoke(object, getType(method, field, val));
////					System.err.println(id + "," + val);
////					System.err.println(idAndValues);
//				}
//				
//			}
	}
	
	private String getId(char currentChar, char[] characters, String value, char startChar, char endChar) {
		String id = "";
		boolean start = false;
		int beginIndex = 0, endIndex = 0;
		while(true) {
			
			if (start && characterFilter.size() == 1) {
				id += currentChar;
			} else if (startChar == currentChar && !start) {
				beginIndex = index;
				start = true;
			} 
			
			filterTags(currentChar, characters, value);
			
			if (value.length() > index+1) {
				index++;
				currentChar = value.charAt(index);
				if ((endChar == currentChar && start)) {
					endIndex = index;
					value = value.substring(beginIndex, endIndex);
					return id;
				}
			} else if (characterFilter.isEmpty()) {
				return "";
			}
		} 
	}
	
	private String getIdAndValues(char endIdValue, char[] characters, String value, char endUntil) throws Exception {
		char currentChar = ' ';
		String id = "";
		int counter = 0;
		do {
			if (characters[index] != endIdValue) {
				id += characters[index]; 
			} 
//			counter = filterTags(currentChar, characters, value, id, counter);
			
			index++;
			if (index >= characters.length && characterFilter.isEmpty()) {
				return id;
			} else if (index >= characters.length) {
				throw new InvalidFormatException("json invalid", value, null);
			}
			currentChar = characters[index]; 
		} while(currentChar != endUntil || !characterFilter.isEmpty());
		return id;
	}

	private void filterTags(char currentChar, char[] characters, String value) {
		for (char c : addCharToFilter) {
			if (c == currentChar) {
				characterFilter.add(c);
			}
		}
		if (!characterFilter.isEmpty()) {
			for (char c : removeCharFromFilters) {
				if (c == currentChar) {
					characterFilter.remove(characterFilter.size()-1);
				}
			}
		}
	}
	
	private Object getType(Method method, Field field, String val) throws Exception {
		if (method.getParameterTypes().length == 0) throw new IllegalStateException("pojo method does not contain parameters: " + method.getName());
		if (method.getParameterTypes().length > 1) throw new IllegalStateException("pojo method contains to many parameters: " + method.getName());
		
		if (field.getType().equals(Integer.TYPE)) {
			return Integer.valueOf(val);
		} else if (field.getType().equals(String.class)) {
			return val;
		} else if (field.getType().isArray()){
			return Array.newInstance(field.getType().getComponentType(), 0);
		}
		return null;
	}
	
	private Field getField(Object object, String methodName) {
		for (Field field : object.getClass().getDeclaredFields()) {
			if (field.getName().toLowerCase().equals(methodName.toLowerCase())) {
				return field;
			}
		}
		return null;
	}
	
	private Method getMethod(Object object, String methodName) {
		for (Method method : object.getClass().getDeclaredMethods()) {
			if (method.getName().toLowerCase().equals("set" + methodName.toLowerCase())) {
				return method;
			}
		}
		return null;
	}
	
	private Object getObject() {
		return object;
	}
}
