package mms.domain;

import java.util.List;
import java.util.ArrayList;
import mms.db.SQLCommand;
import mms.util.Date;

public class UserAuthProcess extends Lookup {
	private static final String USER_AUTH_PROCESS = "select * from user_auth_process where prcId='#!-' and usrId='#!-'";
	private static final String USER_AUTH_PROCESS_BY_USERID = "select * from user_auth_process where usrId='#!-'";
	private static final String ALL = "select * from user_auth_process";
	private static final String STORE = "insert into user_auth_process(lastAccess) values('#!-')";
	private static final String UPDATE = "update user_auth_process set lastAccess='#!-' where prcId='#!-' and usrId='#!-'";
	private static final String DELETE = "delete from user_auth_process where prcId='#!-' and usrId='#!-'";

	private int usrId;
	private int prcId;
	private Date lastAccess;

	public static UserAuthProcess get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(USER_AUTH_PROCESS, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static List<UserAuthProcess> getByUserId(int uId) {
		List<UserAuthProcess> list = new ArrayList<UserAuthProcess>();
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(USER_AUTH_PROCESS_BY_USERID, String.valueOf(uId));
		if(result.length() > 0){
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}
	
	public static List<UserAuthProcess> getAll() {
		List<UserAuthProcess> list = new ArrayList<UserAuthProcess>();

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

	private static UserAuthProcess getObject(String row) {
		String[] cols = row.split("\t");

		UserAuthProcess obj = new UserAuthProcess();
		obj.setUsrId(Integer.valueOf(cols[0]));
		obj.setPrcId(Integer.valueOf(cols[1]));
		obj.setLastAccess(new Date(cols[2]));
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(lastAccess.getFullDateTimeString());
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
		values.add(lastAccess.getFullDateTimeString());

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
		values.add(lastAccess.getFullDateTimeString());

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

	public int getUsrId() {
		return usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public int getPrcId() {
		return prcId;
	}

	public void setPrcId(int prcId) {
		this.prcId = prcId;
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

}
