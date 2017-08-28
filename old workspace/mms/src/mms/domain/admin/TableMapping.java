package mms.domain.admin;

import java.util.ArrayList;
import java.util.List;

import java.util.TreeMap;

import mms.db.SQLCommand;

public class TableMapping {

	private static final String GET_FIELD_DETAIL = "select column_name,data_type,column_key from information_schema.columns where table_name='#!-' and table_schema ='mms'  order by ordinal_position";

	private String fieldName;
	private String fieldKey;
	private String dataType;
	private List<String> fieldValue = new ArrayList<String>();

	public static List<TableMapping> getAll(String tableName) {
		List<TableMapping> tableData = new ArrayList<TableMapping>();
		TreeMap<String, TreeMap<String, String>> fieldList = new TreeMap<String, TreeMap<String, String>>();
		SQLCommand cmd = new SQLCommand();
		StringBuffer colsCollecting = new StringBuffer();
		boolean depIdInUser = false;
		
		String[] colsDetail =cmd.executeQuery("select column_name,column_key from information_schema.columns where table_name='#!-' and table_schema ='mms'  order by ordinal_position",tableName).split("_n-");

		if (colsDetail.length > 0) {
			String prefix = "";
			for (int i = 0; i < colsDetail.length; i++) {
				String[] cols = colsDetail[i].split("\t");
				
				colsCollecting.append(prefix);
				colsCollecting.append(cols[0]);
				prefix = "\t";
				if (cols.length > 1) {
					if ("MUL".equals(cols[1])) {
						String tableKey = "";
						if("tId".equals(cols[0]))
							tableKey = "team";
						else if ("uId".equals(cols[0]))
							tableKey = "user";
						else tableKey = cmd.executeQuery("select table_name from information_schema.columns where column_name = '#!-' and column_key='pri';",cols[0]).split("_n-")[0];
						String[] fkDetail = null;
						if("user".equals(tableKey))
							fkDetail = cmd.executeQuery("select " + cols[0] + ",concat(fname,' ',sname) from user").split("_n-");
						else fkDetail = cmd.executeQuery("select " + cols[0] + ", name from #!-",tableKey).split("_n-");
						if (fkDetail.length > 0) {
							if (fkDetail[0].length() > 0) {
								TreeMap<String, String> colsFieldName = new TreeMap<String, String>();
								for (int j = 0; j < fkDetail.length; j++) {
									String[] colsField = fkDetail[j].split("\t");
									colsFieldName.put(colsField[0], colsField[1]);
								}
								fieldList.put(cols[0], colsFieldName);
							}
						}
					}
				}
				if(("depId").equals(cols[0]) && tableName.equals("user")){
					depIdInUser = true;
					String[] fkDetail = cmd.executeQuery("select coId, name from company").split("_n-");
					if (fkDetail.length > 0) {
						if (fkDetail[0].length() > 0) {
							TreeMap<String, String> colsFieldName = new TreeMap<String, String>();
							for (int j = 0; j < fkDetail.length; j++) {
								String[] colsField = fkDetail[j].split("\t");
								colsFieldName.put(colsField[0], colsField[1]);
							}
							fieldList.put("coId", colsFieldName);
						}
					}
				}
			}	
			
			TreeMap<String, String> coIdfromDepId = new TreeMap<String, String>();
			if(depIdInUser){
				String[] rs = cmd.executeQuery("select depId,coId from department").split("_n-");
				for(int i=0;i<rs.length;i++){
					String[] cols = rs[i].split("\t");
					coIdfromDepId.put(cols[0], cols[1]);
				}
			}
			
			
			String[] fieldName = colsCollecting.toString().split("\t");
			String[] fieldData = cmd.executeQuery("select * from #!-", tableName).split("_n-");
			for (int i = 0; i < fieldData.length; i++) {
				TableMapping td = new TableMapping();
				List<String> tableFieldData = new ArrayList<String>();
				String[] cols = fieldData[i].split("\t");
				for (int j = 0; j < (cols.length); j++) {
					if (fieldList.get(fieldName[j]) != null) {
						tableFieldData.add(fieldList.get(fieldName[j]).get(cols[j]));
						if(fieldName[j].equals("depId") && tableName.equals("user")){
							String id = coIdfromDepId.get(cols[j]);
							tableFieldData.add(fieldList.get("coId").get(id));
						}
						
					} else {
						tableFieldData.add(cols[j]);
					}
				}
				td.setFieldValue(tableFieldData);
				tableData.add(td);

			}
			return tableData;
		} else
			return null;

	}

	public static List<TableMapping> getTableField(String tableName) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(GET_FIELD_DETAIL, tableName);
		if (result.length() > 0) {
			List<String> queryValue = new ArrayList<String>();
			String[] fieldDetail = result.split("_n-");
			queryValue.add(tableName);
			queryValue.add(fieldDetail[0].split("\t")[0]);
			List<TableMapping> table = new ArrayList<TableMapping>();
			for (int i = 0; i < fieldDetail.length; i++) {
				TableMapping tf = new TableMapping();
				String[] cols = fieldDetail[i].split("\t");
				
							
				tf.setFieldName(cols[0]);
				tf.setDataType(TableMapping.getHtml5InputType(cols[1]));
				if (cols.length > 2) {
					tf.setFieldKey(cols[2]);
				}
				table.add(tf);
				
				if(("depId").equals(cols[0]) && tableName.equals("user")){
					TableMapping tc = new TableMapping();
					tc.setFieldName("coId");
					table.add(tc);
				} 	
			}
			
			return table;
		} else
			return null;
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

		return fieldName + "\t" + dataType + "\t" + fieldKey + "\n"
				+ fieldValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public List<String> getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(List<String> fieldValue) {
		this.fieldValue = fieldValue;
	}

}
