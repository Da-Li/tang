package com.migudm.tang.code_generate.utils;


import java.sql.Types;
import java.util.HashMap;


public class DatabaseDataTypesUtils {

	private final static IntStringMap _preferredJavaTypeForSqlType = new IntStringMap();

	public static String getPreferredJavaType(int sqlType, int size,int decimalDigits) {
		if ((sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) && decimalDigits == 0) {
			if (size == 1) {
				// https://sourceforge.net/tracker/?func=detail&atid=415993&aid=662953&group_id=36044
				return "Boolean";
			} else if (size < 10) {
				return "Integer";
			} else if (size < 19) {
				return "Long";
			} else if (size == 131089) { // 可能是视图sum时
				return "Double";
			} else {
				return "java.math.BigDecimal";
			}
		}
		String result = _preferredJavaTypeForSqlType.getString(sqlType);
		if (result == null) {
			result = "Object";
		}
		return result;
	}
		   
   static {
	   //TODO liyu 待完善
      _preferredJavaTypeForSqlType.put(Types.TINYINT, "Integer");
      _preferredJavaTypeForSqlType.put(Types.SMALLINT, "Integer");
      _preferredJavaTypeForSqlType.put(Types.INTEGER, "Integer");
      _preferredJavaTypeForSqlType.put(Types.BIGINT, "Long");
      _preferredJavaTypeForSqlType.put(Types.REAL, "Float");
      _preferredJavaTypeForSqlType.put(Types.FLOAT, "Double");
      _preferredJavaTypeForSqlType.put(Types.DOUBLE, "Double");
      _preferredJavaTypeForSqlType.put(Types.DECIMAL, "java.math.BigDecimal");
      _preferredJavaTypeForSqlType.put(Types.NUMERIC, "Double");
      _preferredJavaTypeForSqlType.put(Types.BIT, "Boolean");
      _preferredJavaTypeForSqlType.put(Types.BOOLEAN, "Boolean");
      _preferredJavaTypeForSqlType.put(Types.CHAR, "String");
      _preferredJavaTypeForSqlType.put(Types.VARCHAR, "String");
      _preferredJavaTypeForSqlType.put(Types.DATE, "java.util.Date");
      _preferredJavaTypeForSqlType.put(Types.TIME, "java.util.Date");
      _preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "java.util.Date");
      _preferredJavaTypeForSqlType.put(Types.CLOB, "java.sql.Clob");
      _preferredJavaTypeForSqlType.put(Types.BLOB, "java.sql.Blob");
      _preferredJavaTypeForSqlType.put(Types.ARRAY, "java.sql.Array");
      _preferredJavaTypeForSqlType.put(Types.REF, "java.sql.Ref");
      _preferredJavaTypeForSqlType.put(Types.STRUCT, "Object");
      _preferredJavaTypeForSqlType.put(Types.JAVA_OBJECT, "Object");
   }
		
   private static class IntStringMap extends HashMap {

		public String getString(int i) {
			return (String) get(new Integer(i));
		}

		public String[] getStrings(int i) {
			return (String[]) get(new Integer(i));
		}

		public void put(int i, String s) {
			put(new Integer(i), s);
		}

		public void put(int i, String[] sa) {
			put(new Integer(i), sa);
		}
	}
	   
}
