package mms.domain;

import java.util.List;
import java.util.ArrayList;

import mms.db.SQLCommand;
import mms.util.Date;

public class UserMaster extends Lookup {
	private static final String USER_MASTER = "select * from user_master where usrId='#!-'";
	private static final String USER_MASTER_BY_PASSWORD = "select * from user_master where usrId='#!-' and password='#!-'";
	private static final String ALL = "select um.*,(select ro.name from role ro where um.roId=ro.roId) roName from user_master um";
	private static final String STORE = "insert into user_master(roId,code,name,password) values('#!-','#!-','#!-','#!-')";
	private static final String UPDATE = "update user_master set roId='#!-',code='#!-',name='#!-',password='#!-' where usrId='#!-'";
	private static final String DELETE = "delete from user_master where usrId='#!-'";

	private int usrId;
	private int roId;
	private String code;
	private String name;
	private String password;
	
	private String roName;

	public static UserMaster get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(USER_MASTER, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static UserMaster getUserByPassword(int uid,String password) {
		SQLCommand cmd = new SQLCommand();
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(uid));
		values.add(password);
		String result = cmd.executeQuery(USER_MASTER_BY_PASSWORD, values);
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<UserMaster> getAll() {
		List<UserMaster> list = new ArrayList<UserMaster>();

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

	private static UserMaster getObject(String row) {
		String[] cols = row.split("\t");

		UserMaster obj = new UserMaster();
		obj.setUsrId(Integer.valueOf(cols[0]));
		obj.setRoId(Integer.valueOf(cols[1]));
		obj.setCode(cols[2]);
		obj.setName(cols[3]);
		obj.setPassword(cols[4]);
		if(cols.length > 5)
			obj.setRoName(cols[5]);
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(roId));
		values.add(code);
		values.add(name);
		values.add(password);
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
		values.add(String.valueOf(usrId));




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
		values.add(String.valueOf(roId));
		values.add(code);
		values.add(name);
		values.add(password);

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
	
	public static boolean delete(String usrId) {
		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(DELETE, String.valueOf(usrId)))
			return true;
		else return false;
	}

	public int getUsrId() {
		return usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public int getRoId() {
		return roId;
	}

	public void setRoId(int roId) {
		this.roId = roId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRoName() {
		return roName;
	}

	public void setRoName(String roName) {
		this.roName = roName;
	}

}
