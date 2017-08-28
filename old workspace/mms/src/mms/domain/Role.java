package mms.domain;

import java.util.List;
import java.util.ArrayList;
import mms.db.SQLCommand;
import mms.util.Date;

public class Role extends Lookup {
	private static final String ROLE = "select * from role where roId='#!-'";
	private static final String ALL = "select * from role";
	private static final String STORE = "insert into role(name) values('#!-')";
	private static final String UPDATE = "update role set name='#!-' where roId='#!-'";
	private static final String DELETE = "delete from role where roId='#!-'";

	private int roId;
	private String name;

	public static Role get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(ROLE, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<Role> getAll() {
		List<Role> list = new ArrayList<Role>();

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

	private static Role getObject(String row) {
		String[] cols = row.split("\t");

		Role obj = new Role();
		obj.setRoId(Integer.valueOf(cols[0]));
		obj.setName(cols[1]);
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(name);
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

	public int getRoId() {
		return roId;
	}

	public void setRoId(int roId) {
		this.roId = roId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
