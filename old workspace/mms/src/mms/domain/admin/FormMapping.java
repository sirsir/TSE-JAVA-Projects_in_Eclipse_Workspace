package mms.domain.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import mms.db.SQLCommand;

public class FormMapping {

	private static final String GET_FIELD_DETAIL = "select column_name,data_type,is_nullable,column_key from information_schema.columns where table_name='#!-' and table_schema ='mms'  order by ordinal_position";
	private static final String GET_FIELD_DATA = "select * from #!- where #!- = #!-;";


	private String fieldName;
	private String fieldValue;
	private String fieldKey;
	private String dataType;
	private String nullable;
	private TreeMap<String, String> list = new TreeMap<String, String>();

	public static List<FormMapping> getField(String tableName, String id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(GET_FIELD_DETAIL, tableName);
		if (result.length() > 0) {
			List<String> queryValue = new ArrayList<String>();
			String[] fieldDetail = result.split("_n-");
			queryValue.add(tableName);
			queryValue.add(fieldDetail[0].split("\t")[0]);
				queryValue.add(id);
			String[] fieldData = cmd.executeQuery(GET_FIELD_DATA, queryValue).split("\t");
					
			List<FormMapping> formMap = new ArrayList<FormMapping>();

			for (int i = 0; i < fieldDetail.length; i++) {
				FormMapping fm = new FormMapping();
				String[] cols = fieldDetail[i].split("\t");
				
				if(("depId").equals(cols[0]) && tableName.equals("user")){
					FormMapping fmIns = new FormMapping();
					String coName = cmd.executeQuery("select coId from department where depId = #!-",fieldData[i]);
					fmIns.setFieldName("coId");
					fmIns.setDataType("select");
					fmIns.setList(FormMapping.getSelectList("coId"));
					fmIns.setFieldValue(fmIns.getList().get(coName));
					formMap.add(fmIns);	
					
					fm.setFieldName(cols[0]);
					fm.setDataType("select");
					TreeMap<String, String> selectDetail = new TreeMap<String,String>();
					String[] rs = cmd.executeQuery("select depId, name from department where coId = "+coName).split("_n-");
					for (int j = 0; j < rs.length; j++) {
						if(rs[j].length()>0){
						String[] colsSelect = rs[j].split("\t");
						selectDetail.put(colsSelect[0], colsSelect[1]);
						}
					}
					fm.setList(selectDetail);
					fm.setFieldValue(fm.getList().get(fieldData[i]));
					formMap.add(fm);
				} else {
					fm.setFieldName(cols[0]);
					fm.setDataType(FormMapping.getHtml5InputType(cols[1]));
					if(!("null").equals(fieldData[i]))
						fm.setFieldValue(fieldData[i]);
					fm.setNullable(cols[2]);
					if (cols.length > 3) {
						fm.setFieldKey(cols[3]);
						if (fm.getFieldKey().equals("MUL")) {
							fm.setDataType("select");
							fm.setList(FormMapping.getSelectList(cols[0]));
							fm.setFieldValue(fm.getList().get(fieldData[i]));
						}
					}
					formMap.add(fm);
				}
			}

			return formMap;
		} else
			return null;
	}

	public static List<FormMapping> getField(String tableName) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(GET_FIELD_DETAIL, tableName);
		if (result.length() > 0) {
			List<String> queryValue = new ArrayList<String>();
			String[] fieldDetail = result.split("_n-");
			queryValue.add(tableName);
			queryValue.add(fieldDetail[0].split("\t")[0]);
			List<FormMapping> formMap = new ArrayList<FormMapping>();

			for (int i = 0; i < fieldDetail.length; i++) {
				FormMapping fm = new FormMapping();
				String[] cols = fieldDetail[i].split("\t");
				
				if(("depId").equals(cols[0]) && tableName.equals("user")){
					FormMapping fmIns = new FormMapping();
					String coName = cmd.executeQuery("select coId from company").split("_n-")[0];
					fmIns.setFieldName("coId");
					fmIns.setDataType("select");
					fmIns.setList(FormMapping.getSelectList("coId"));
					fmIns.setFieldValue(fmIns.getList().get(coName));
					formMap.add(fmIns);	
					
					fm.setFieldName(cols[0]);
					fm.setDataType("select");
					TreeMap<String, String> selectDetail = new TreeMap<String,String>();
					String[] rs = cmd.executeQuery("select depId, name from department where coId = "+coName).split("_n-");
					for (int j = 0; j < rs.length; j++) {
						if(rs[j].length()>0){
						String[] colsSelect = rs[j].split("\t");
						selectDetail.put(colsSelect[0], colsSelect[1]);
						}
					}
					fm.setList(selectDetail);
					formMap.add(fm);
					} else {
					fm.setFieldName(cols[0]);
					fm.setDataType(FormMapping.getHtml5InputType(cols[1]));
					fm.setNullable(cols[2]);
					if (cols.length > 3	) {
						fm.setFieldKey(cols[3]);
						if (fm.getFieldKey().equals("MUL")) {
							fm.setDataType("select");
							fm.setList(FormMapping.getSelectList(cols[0]));
						}
					}
					if(fm.getFieldName().equals("expired")){
						fm.setFieldValue("0");
					}
					formMap.add(fm);
				}
			}

			return formMap;
		} else
			return null;
	}
	private static TreeMap<String, String> getSelectList(String priKeyField) {
		SQLCommand cmd = new SQLCommand();
		TreeMap<String, String> selectDetail = new TreeMap<String,String>();
		String tableName = cmd.executeQuery("select table_name from information_schema.columns where column_name='#!-' and column_key='PRI'and table_schema ='mms' ",priKeyField);
		String[] rs = cmd.executeQuery("select " + priKeyField + ", name from " + tableName + " order by " + priKeyField).split("_n-");
		for (int i = 0; i < rs.length; i++) {
			if(rs[i].length()>0){
			String[] cols = rs[i].split("\t");
			selectDetail.put(cols[0], cols[1]);
			}
		}

		return selectDetail;

		
	}

	private static String getHtml5InputType(String dataType) {
		String type = "";

		if (dataType.equals("int") || dataType.equals("smallint") || dataType.equals("tinyint") || dataType.equals("mediumint") || dataType.equals("bigint")) {
			type = "number";
		} else if (dataType.equals("varchar")) {
			type = "text";
		}

		return type;
	}

	public String toString() {

		return fieldName + "\t" + dataType + "\t" + fieldValue + "\n";
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public TreeMap<String, String> getList() {
		return list;
	}

	public void setList(TreeMap<String, String> list) {
		this.list = list;
	}

}
