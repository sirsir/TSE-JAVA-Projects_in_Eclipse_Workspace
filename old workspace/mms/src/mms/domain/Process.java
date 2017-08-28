package mms.domain;

import java.util.List;
import java.util.ArrayList;
import mms.db.SQLCommand;

public class Process extends Lookup {
	private static final String PROCESS = "select * from process where prcId='#!-'";
	private static final String ALL = "select * from process";
	private static final String NEXT_PRCID = "select prcId from process where prcId > #!- order by prcId limit 0,1";
	private static final String STORE = "insert into process(no,name) values('#!-','#!-')";
	private static final String UPDATE = "update process set no='#!-',name='#!-' where prcId='#!-'";
	private static final String DELETE = "delete from process where prcId='#!-'";

	private int prcId;
	private int no;
	private String name;

	public static Process get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(PROCESS, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<Process> getAll() {
		List<Process> list = new ArrayList<Process>();

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
	
	public static String getNextPrcId(int prcId) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(NEXT_PRCID, String.valueOf(prcId));
		if(!"null".equals(result) && result.length() > 0)
			return result;
		else return null;
	}

	private static Process getObject(String row) {
		String[] cols = row.split("\t");

		Process obj = new Process();
		obj.setPrcId(Integer.valueOf(cols[0]));
		obj.setNo(Integer.valueOf(cols[1]));
		obj.setName(cols[2]);
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(no));
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
		values.add(String.valueOf(prcId));

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
		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(DELETE, String.valueOf(prcId))) {
			message = "success";
			return true;
		}
		else {
			message = cmd.getMessage();
			return false;
		}
	}

	public int getPrcId() {
		return prcId;
	}

	public void setPrcId(int prcId) {
		this.prcId = prcId;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
