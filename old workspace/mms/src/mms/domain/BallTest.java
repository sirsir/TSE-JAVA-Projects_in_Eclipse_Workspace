package mms.domain;

import java.util.List;
import java.util.ArrayList;
import mms.db.SQLCommand;
import mms.util.Date;

public class BallTest extends Lookup {
	private static final String ARRIVAL_PLAN = "select * from arrival_plan where arrId='#!-'";
	private static final String ALL = "select ap.*, (select pm.name from product_master pm where pm.prdId=ap.prdId) prdName from arrival_plan ap";
	private static final String BY_PERIOD = "select ap.*, (select pm.name from product_master pm where pm.prdId=ap.prdId) prdName from arrival_plan ap where ap.arrDate >= '#!-' and ap.arrDate <= '#!-'";
	private static final String MAX_DATE_CODE = "select max(no) from arrival_plan where no like '#!-%'";
	
	private static final String STORE = "insert into arrival_plan(arrDate,no,prdId,planQty,arrQty) values('#!-','#!-','#!-','#!-','#!-')";
	private static final String UPDATE = "update arrival_plan set arrDate='#!-',no='#!-',prdId='#!-',planQty='#!-',arrQty='#!-' where arrId='#!-'";
	private static final String DELETE = "delete from arrival_plan where arrId='#!-'";

	private int arrId;
	private Date arrDate;
	private String no;
	private int prdId;
	private int planQty;
	private int arrQty;
	
	private String prdName;

	public static ArrivalPlan get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(ARRIVAL_PLAN, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<ArrivalPlan> getAll() {
		List<ArrivalPlan> list = new ArrayList<ArrivalPlan>();

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
	
	public static List<ArrivalPlan> getByPeriod(String start, String end) {
		List<String> values = new ArrayList<String>();
		values.add(start);
		values.add(end);
		
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_PERIOD, values);
		if(result.length() > 0){
			List<ArrivalPlan> list = new ArrayList<ArrivalPlan>();
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

	private static ArrivalPlan getObject(String row) {
		String[] cols = row.split("\t");

		ArrivalPlan obj = new ArrivalPlan();
		obj.setArrId(Integer.valueOf(cols[0]));
		obj.setArrDate(new Date(cols[1]));
		obj.setNo(cols[2]);
		obj.setPrdId(Integer.valueOf(cols[3]));
		obj.setPlanQty(Integer.valueOf(cols[4]));
		if(!"null".equals(cols[5]))
			obj.setArrQty(Integer.valueOf(cols[5]));
		if(cols.length > 6)
			obj.setPrdName(cols[6]);
		
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(arrDate.getFullDateTimeString());
		values.add(no);
		values.add(String.valueOf(prdId));
		values.add(String.valueOf(planQty));
		if(arrQty == 0)
			values.add("");
		else values.add(String.valueOf(arrQty));
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
		values.add(String.valueOf(arrId));

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
		if(cmd.executeUpdate(DELETE, String.valueOf(arrId))) {
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

	public int getArrId() {
		return arrId;
	}

	public void setArrId(int arrId) {
		this.arrId = arrId;
	}

	public Date getArrDate() {
		return arrDate;
	}

	public void setArrDate(Date arrDate) {
		this.arrDate = arrDate;
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

	public int getArrQty() {
		return arrQty;
	}

	public void setArrQty(int arrQty) {
		this.arrQty = arrQty;
	}

	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

}
