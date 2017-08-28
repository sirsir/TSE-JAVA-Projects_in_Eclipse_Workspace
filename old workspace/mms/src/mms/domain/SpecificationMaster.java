package mms.domain;

import java.util.List;
import java.util.ArrayList;

import mms.db.SQLCommand;

public class SpecificationMaster extends Lookup {
	private static final String SPECIFICATION_MASTER = "select * from specification_master where specId='#!-'";
	private static final String ALL = "select * from specification_master";
	private static final String STORE = "insert into specification_master(name,attribute,part0,part1,part2,part3,part4,part5,part6,part7,part8,part9) values('#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-')";
	private static final String UPDATE = "update specification_master set name='#!-',attribute='#!-',part0='#!-',part1='#!-',part2='#!-',part3='#!-',part4='#!-',part5='#!-',part6='#!-',part7='#!-',part8='#!-',part9='#!-' where specId='#!-'";
	private static final String DELETE = "delete from specification_master where specId='#!-'";
	
	public static final String[] NAME_ATTRIBUTE = {"Special","Character","Button","Pull-down","Numeric"};
	
	private int specId;
	private String name;
	private int attribute;
	private String part0;
	private String part1;
	private String part2;
	private String part3;
	private String part4;
	private String part5;
	private String part6;
	private String part7;
	private String part8;
	private String part9;

	public static SpecificationMaster get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(SPECIFICATION_MASTER, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<SpecificationMaster> getAll() {
		List<SpecificationMaster> list = new ArrayList<SpecificationMaster>();

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

	private static SpecificationMaster getObject(String row) {
		String[] cols = row.split("\t");

		SpecificationMaster obj = new SpecificationMaster();
		obj.setSpecId(Integer.valueOf(cols[0]));
		obj.setName(cols[1]);
		obj.setAttribute(Integer.valueOf(cols[2]));
		obj.setPart0(cols[3]);
		obj.setPart1(cols[4]);
		obj.setPart2(cols[5]);
		obj.setPart3(cols[6]);
		obj.setPart4(cols[7]);
		obj.setPart5(cols[8]);
		obj.setPart6(cols[9]);
		obj.setPart7(cols[10]);
		obj.setPart8(cols[11]);
		obj.setPart9(cols[12]);
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(name);
		values.add(String.valueOf(attribute));
		if(part0 == null)
			values.add("");
		else
			values.add(part0);
		if(part1 == null)
			values.add("");
		else
			values.add(part1);
		if(part2 == null)
			values.add("");
		else
			values.add(part2);
		if(part3 == null)
			values.add("");
		else
			values.add(part3);
		if(part4 == null)
			values.add("");
		else
			values.add(part4);		
		if(part5 == null)
			values.add("");
		else
			values.add(part5);
		if(part6 == null)
			values.add("");
		else
			values.add(part6);
		if(part7 == null)
			values.add("");
		else
			values.add(part7);
		if(part8 == null)
			values.add("");
		else
			values.add(part8);
		if(part9 == null)
			values.add("");
		else
			values.add(part9);
		
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
		values.add(String.valueOf(attribute));
		values.add(part0);
		values.add(part1);
		values.add(part2);
		values.add(part3);
		values.add(part4);
		values.add(part5);
		values.add(part6);
		values.add(part7);
		values.add(part8);
		values.add(part9);

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
		if(cmd.executeUpdate(DELETE, String.valueOf(specId))) {
			message = "success";
			return true;
		}
		else {
			message = cmd.getMessage();
			return false;
		}
	}
	
	public static boolean delete(int specId) {
		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(DELETE, String.valueOf(specId))) 
			return true;
		else return false;
	}
	
	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public String getPart0() {
		return part0;
	}

	public void setPart0(String part0) {
		if(part0 == null || "null".equals(part0))
			part0 = "";
		this.part0 = part0;
	}

	public String getPart1() {
		return part1;
	}

	public void setPart1(String part1) {
		if(part1 == null || "null".equals(part1))
			part1 = "";
		this.part1 = part1;
	}

	public String getPart2() {
		return part2;
	}

	public void setPart2(String part2) {
		if(part2 == null || "null".equals(part2))
			part2 = "";
		this.part2 = part2;
	}

	public String getPart3() {
		return part3;
	}

	public void setPart3(String part3) {
		if(part3 == null || "null".equals(part3))
			part3 = "";
		this.part3 = part3;
	}

	public String getPart4() {
		return part4;
	}

	public void setPart4(String part4) {
		if(part4 == null || "null".equals(part4))
			part4 = "";
		this.part4 = part4;
	}

	public String getPart5() {
		return part5;
	}

	public void setPart5(String part5) {
		if(part5 == null || "null".equals(part5))
			part5 = "";
		this.part5 = part5;
	}

	public String getPart6() {
		return part6;
	}

	public void setPart6(String part6) {
		if(part6 == null || "null".equals(part6))
			part6 = "";
		this.part6 = part6;
	}

	public String getPart7() {
		return part7;
	}

	public void setPart7(String part7) {
		if(part7 == null || "null".equals(part7))
			part7 = "";
		this.part7 = part7;
	}

	public String getPart8() {
		return part8;
	}

	public void setPart8(String part8) {
		if(part8 == null || "null".equals(part8))
			part8 = "";
		this.part8 = part8;
	}

	public String getPart9() {
		return part9;
	}

	public void setPart9(String part9) {
		if(part9 == null || "null".equals(part9))
			part9 = "";
		this.part9 = part9;
	}

}
