package mms.domain;

import java.util.List;
import java.util.ArrayList;
import mms.db.SQLCommand;
import mms.util.Date;

public class Category extends Lookup {
	public static final String PRODUCT_TYPE = "Product Type";
	public static final String MANUFACTURING_STATUS = "Manufacturing Status";
	public static final String PROCESS_RESULT = "Proccess Result";
	
	private static final String CATEGORY = "select * from category where catId='#!-'";
	private static final String ALL = "select * from category";
	private static final String STORE = "insert into category(name,expired) values('#!-','#!-')";
	private static final String UPDATE = "update category set name='#!-',expired='#!-' where catId='#!-'";
	private static final String DELETE = "delete from category where catId='#!-'";

	private int catId;
	private String name;
	private int expired;

	public static Category get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(CATEGORY, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<Category> getAll() {
		List<Category> list = new ArrayList<Category>();

		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(ALL);
		if(result.length() > 0){
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}

	private static Category getObject(String row) {
		String[] cols = row.split("\t");

		Category obj = new Category();
		obj.setCatId(Integer.valueOf(cols[0]));
		obj.setName(cols[1]);
		obj.setExpired(Integer.valueOf(cols[2]));
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(name);
		values.add(String.valueOf(expired));
		return values;
	}

	public int store() {
		List<String> values = getValues();

		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(STORE, values)) {
			String lastId = cmd.executeQuery(SQLCommand.LAST_ID);
			message = "success";
			return Integer.valueOf(lastId);
		}
		else {
			message = cmd.getMessage();
			return 0;
		}
	}

	public boolean update() {
		List<String> values = getValues();
		values.add(name);
		values.add(String.valueOf(expired));

		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(UPDATE, values)) {
			message = "success";
			return true;
		}
		else {
			message = cmd.getMessage();
			return false;
		}
	}

	public boolean delete() {
		List<String> values = getValues();
		values.add(name);
		values.add(String.valueOf(expired));

		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(DELETE, values)) {
			message = "success";
			return true;
		}
		else {
			message = cmd.getMessage();
			return false;
		}
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getExpired() {
		return expired;
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

}
