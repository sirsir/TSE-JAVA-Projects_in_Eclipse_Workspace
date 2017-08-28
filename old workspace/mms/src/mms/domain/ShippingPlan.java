package mms.domain;

import java.util.List;
import java.util.ArrayList;

import mms.db.SQLCommand;
import mms.util.Date;

public class ShippingPlan extends Lookup {
	private static final String SHIPPING_PLAN = "select * from shipping_plan where shpId='#!-'";
	private static final String ALL = "select sp.*, (select pm.name from product_master pm where pm.prdId=sp.prdId) prdName from shipping_plan sp";
	private static final String BY_PERIOD = "select sp.*, (select pm.name from product_master pm where pm.prdId=sp.prdId) prdName from shipping_plan sp where sp.shpDate >= '#!-' and sp.shpDate <= '#!-'";
	private static final String MAX_DATE_CODE = "select max(no) from shipping_plan where no like '#!-%'";
	
	private static final String STORE = "insert into shipping_plan(shpDate,no,prdId,planQty,shipQty) values('#!-','#!-','#!-','#!-','#!-')";
	private static final String UPDATE = "update shipping_plan set shpDate='#!-',no='#!-',prdId='#!-',planQty='#!-',shipQty='#!-' where shpId='#!-'";
	private static final String DELETE = "delete from shipping_plan where shpId='#!-'";

	private int shpId;
	private Date shpDate;
	private String no;
	private int prdId;
	private int planQty;
	private int shipQty;
	
	private String prdName;

	public static ShippingPlan get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(SHIPPING_PLAN, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<ShippingPlan> getAll() {
		List<ShippingPlan> list = new ArrayList<ShippingPlan>();

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
	
	public static List<ShippingPlan> getByPeriod(String start, String end) {
		List<String> values = new ArrayList<String>();
		values.add(start);
		values.add(end);
		
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_PERIOD, values);
		if(result.length() > 0){
			List<ShippingPlan> list = new ArrayList<ShippingPlan>();
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}
	
	public static String getDateCode(String dcode) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(MAX_DATE_CODE, dcode);
		if("null".equals(result))
			return dcode + "0001";
		else {
			String sno = result.substring(8, result.length());
			int no = Integer.valueOf(sno) + 1;
			if(no < 1000 && no > 99)
				sno = "0" + no;
			else if(no < 100 && no > 9)
				sno = "00" + no;
			else if(no < 10)
				sno = "000" + no;
			
			return dcode + sno;
		}
	}

	private static ShippingPlan getObject(String row) {
		String[] cols = row.split("\t");

		ShippingPlan obj = new ShippingPlan();
		obj.setShpId(Integer.valueOf(cols[0]));
		obj.setShpDate(new Date(cols[1]));
		obj.setNo(cols[2]);
		obj.setPrdId(Integer.valueOf(cols[3]));
		obj.setPlanQty(Integer.valueOf(cols[4]));

		if(!"null".equals(cols[5]))
			obj.setShipQty(Integer.valueOf(cols[5]));
		if(cols.length > 6)
			obj.setPrdName(cols[6]);
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(shpDate.getFullDateTimeString());
		values.add(no);
		values.add(String.valueOf(prdId));
		values.add(String.valueOf(planQty));
		if(shipQty == 0)
			values.add("");
		else values.add(String.valueOf(shipQty));
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
		values.add(String.valueOf(shpId));

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
		if(cmd.executeUpdate(DELETE, String.valueOf(shpId))) {
			message = "success";
			return true;
		}
		else {
			message = cmd.getMessage();
			return false;
		}
	}
	
	public static boolean delete(String id) {
		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(DELETE, String.valueOf(id)))
			return true;
		else return false;
	}

	public int getShpId() {
		return shpId;
	}

	public void setShpId(int shpId) {
		this.shpId = shpId;
	}

	public Date getShpDate() {
		return shpDate;
	}

	public void setShpDate(Date shpDate) {
		this.shpDate = shpDate;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getPrdId() {
		return prdId;
	}

	public void setPrdId(int prdId) {
		this.prdId = prdId;
	}

	public int getPlanQty() {
		return planQty;
	}

	public void setPlanQty(int planQty) {
		this.planQty = planQty;
	}

	public int getShipQty() {
		return shipQty;
	}

	public void setShipQty(int shipQty) {
		this.shipQty = shipQty;
	}

	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

}
