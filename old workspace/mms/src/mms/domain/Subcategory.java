package mms.domain;

import java.util.List;
import java.util.ArrayList;
import mms.db.SQLCommand;
import mms.util.Date;

public class Subcategory extends Lookup {
	private static final String SUBCATEGORY = "select * from subcategory where scatId='#!-'";
	private static final String ALL = "select * from subcategory";
	private static final String ID_BY_NAME = "select scatId from subcategory where catId=(select catId from category where name='#!-') and name='#!-'";
	private static final String LIST_BY_NAME = "select * from subcategory where catId=(select catId from category where name='#!-')";
	private static final String STORE = "insert into subcategory(catId,name,expired) values('#!-','#!-','#!-')";
	private static final String UPDATE = "update subcategory set catId='#!-',name='#!-',expired='#!-' where scatId='#!-'";
	private static final String DELETE = "delete from subcategory where scatId='#!-'";

	private int scatId;
	private int catId;
	private String name;
	private int expired;

	public static Subcategory get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(SUBCATEGORY, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static String getIdByName(String catName, String scatName) {
		List<String> values = new ArrayList<String>();
		values.add(catName);
		values.add(scatName);
		
		SQLCommand cmd = new SQLCommand();
		return cmd.executeQuery(ID_BY_NAME, values);
	}
	
	public static List<Subcategory> getListByName(String catName) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(LIST_BY_NAME, catName);
		if(result.length() > 0){
			List<Subcategory> list = new ArrayList<Subcategory>();
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}

	public static List<Subcategory> getAll() {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(ALL);
		if(result.length() > 0){
			List<Subcategory> list = new ArrayList<Subcategory>();
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}

	private static Subcategory getObject(String row) {
		String[] cols = row.split("\t");

		Subcategory obj = new Subcategory();
		obj.setScatId(Integer.valueOf(cols[0]));
		obj.setCatId(Integer.valueOf(cols[1]));
		obj.setName(cols[2]);
		obj.setExpired(Integer.valueOf(cols[3]));
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(catId));
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
		values.add(String.valueOf(catId));
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
		values.add(String.valueOf(catId));
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

	public int getScatId() {
		return scatId;
	}

	public void setScatId(int scatId) {
		this.scatId = scatId;
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
