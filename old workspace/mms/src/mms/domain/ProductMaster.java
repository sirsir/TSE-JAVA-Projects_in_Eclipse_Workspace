package mms.domain;

import java.util.List;
import java.util.ArrayList;

import mms.db.SQLCommand;
import mms.util.Date;

public class ProductMaster extends Lookup {
	private static final String PRODUCT_MASTER = "select * from product_master where prdId='#!-'";
	private static final String PRODUCT_MASTER_QUERY = "select pm.*,(select sc.name from subcategory sc where pm.scatId=sc.scatId) scatName from product_master pm where prdId='#!-'";
	private static final String ALL = "select pm.*,(select sc.name from subcategory sc where pm.scatId=sc.scatId) scatName from product_master pm";
	private static final String STORE = "insert into product_master(code,name,scatId) values('#!-','#!-','#!-')";
	private static final String UPDATE = "update product_master set code='#!-',name='#!-',scatId='#!-' where prdId='#!-'";
	private static final String DELETE = "delete from product_master where prdId='#!-'";

	private int prdId;
	private String code;
	private String name;
	private int scatId;

	private String scatName;
	
	public String toString(){
		return prdId+"/t"+code+"/t"+name+"/t"+scatId+"/t"+scatName;
	}
	
	public static ProductMaster get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(PRODUCT_MASTER, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static ProductMaster getQuery(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(PRODUCT_MASTER_QUERY, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static List<ProductMaster> getAll() {
		List<ProductMaster> list = new ArrayList<ProductMaster>();

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

	private static ProductMaster getObject(String row) {
		String[] cols = row.split("\t");

		ProductMaster obj = new ProductMaster();
		obj.setPrdId(Integer.valueOf(cols[0]));
		obj.setCode(cols[1]);
		obj.setName(cols[2]);
		obj.setScatId(Integer.valueOf(cols[3]));
		if(cols.length > 4)
			obj.setScatName(cols[4]);
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(code);
		values.add(name);
		values.add(String.valueOf(scatId));
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
		values.add(String.valueOf(prdId));

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
		values.add(code);
		values.add(name);
		values.add(String.valueOf(scatId));

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

	public static boolean delete(String prdId) {
		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(DELETE, String.valueOf(prdId)))
			return true;
		else return false;
	}
	
	public int getPrdId() {
		return prdId;
	}

	public void setPrdId(int prdId) {
		this.prdId = prdId;
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

	public int getScatId() {
		return scatId;
	}

	public void setScatId(int scatId) {
		this.scatId = scatId;
	}

	public String getScatName() {
		return scatName;
	}

	public void setScatName(String scatName) {
		this.scatName = scatName;
	}
}
