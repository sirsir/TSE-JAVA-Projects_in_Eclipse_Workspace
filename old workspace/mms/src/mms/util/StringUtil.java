package mms.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
	public static String replace(String str, String var, String val) {
		int idx = str.indexOf(var);
		if(idx >= 0)
			str = str.substring(0, idx) + val + str.substring(idx+var.length(), str.length());			
		return str;
	}
	
	public static String getAddedLeftSpaceString(String value, int fixedLen) {
		if(value == null)
			value = "";
		
		int len = value.length();
		
		for(;len < fixedLen; len++) {
			value = " " + value;
		}
		
		return value;
	}
	
	public static String getAddedLeftSpaceString(int value, int fixedLen) {
		return getAddedLeftSpaceString(String.valueOf(value), fixedLen);
	}
	
	public static String getAddedLeftSpaceString(double value, int fixedLen) {
		String string = String.valueOf(value);
		String tmp = string.substring(string.indexOf(".") + 1, string.length());
		for(int i=0; i<tmp.length(); i++) {
			string += "0";
		}
		
		
		
		return getAddedLeftSpaceString(string, fixedLen);
	}
	
	public static String getAddedRightSpaceString(String value, int fixedLen) {
		if(value == null)
			value = "";
		
		int len = value.length();
		for(;len < fixedLen; len++) {
			value += " ";
		}
		
		return value;
	}
	
	public static String getSpace(int fixedLen) {
		String value = "";
		
		for(int i=0; i<fixedLen; i++) {
			value += " ";
		}
		
		return value;
	}
	
	public static String getAddedLeftDotString(int value) {
		String string = String.valueOf(value);
		
		for(int i=0; i<value; i++) {
			string = "." + string;
		}
		
		return string;
	}
	
	public static List<String> split(String string, String separator) {
		List<String> list = new ArrayList<String>();
		
		int index;		
		while((index = string.indexOf(separator)) >= 0) {
			list.add(string.substring(0, index));
			string = string.substring(index + separator.length(), string.length());
		}
		list.add(string);
		
		return list;
	}
		
	public static boolean matching(String value, int position, String cond) {
		boolean match = true;
		value = value.toLowerCase();
		cond = cond.toLowerCase();
		String reducedCond = cond.replaceAll("\\?", "");
		reducedCond = reducedCond.replaceAll("#", "");
		
		if(position == 0 && value.indexOf(reducedCond) != 0)
			return false;
		else if(position == 1 && value.indexOf(reducedCond) < 0)
			return false;
		int idx = value.indexOf(reducedCond);
		
		if(value.length() < idx+cond.length())
			return false;
		
		value = value.substring(idx, idx+cond.length());
		
		if(value.equals(reducedCond))
			return true;
		value = value.replaceAll(reducedCond, "");
		String symbol = cond.replaceAll(reducedCond, "");
		
		
		char[] cchar = value.toCharArray();
		char[] schar = symbol.toCharArray();
		
		for(int i=0; i<cchar.length; i++) {
			if(schar[i] == '?' && !Character.isDigit(cchar[i])) {}
			else if(schar[i] == '#' && Character.isDigit(cchar[i])) {}
			else if(schar[i] == cchar[i]) {}
			else {
				match = false;
				break;
			}
		}
		
		return match;
	}
	
	public static boolean contains(Object[] objects, String comp) {
		boolean contain = false;
		int size = objects.length;
		for(int i=0; i<size; i++) {
			String v = objects[i].toString();
			if(v.equals(comp)) {
				contain = true;
				break;
			}
		}
	
		return contain;
	}
	
	public static String capitalize(String line) {
	  return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
}
