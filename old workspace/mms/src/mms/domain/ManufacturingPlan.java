package mms.domain;

import java.util.List;
import java.util.ArrayList;

import mms.db.SQLCommand;
import mms.util.Date;

public class ManufacturingPlan extends Lookup {
	public static final String IN_PRODUCTION = "In production";
	public static final String FINISHED = "Finished";
	public static final String BREAK = "Break";
	public static final String CANCELED = "Canceled";
	
	private static final String MANUFACTURING_PLAN = "select mp.*, (select pm.name from product_master pm where pm.prdId=mp.prdId) prdName from manufacturing_plan mp where mp.mpId='#!-'";
	private static final String RUNNING = "select * from manufacturing_plan mp where mp.startActual is not null and mp.finishActual is null";
	private static final String ALL = "select * from manufacturing_plan";
	private static final String BY_PERIOD = "select mp.*, (select pm.name from product_master pm where pm.prdId=mp.prdId) prdName, (select scat.name from subcategory scat where scat.scatId=mp.status) status from manufacturing_plan mp where mp.mpDate >= '#!-' and mp.mpDate <= '#!-'";
	private static final String MAX_DATE_CODE = "select max(no) from manufacturing_plan where no like '#!-%'";

	private static final String STORE = "insert into manufacturing_plan(mpDate,no,prdId,qty,startPlan,finishPlan,startActual,finishActual,status) values('#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-')";
	private static final String UPDATE = "update manufacturing_plan set mpDate='#!-',no='#!-',prdId='#!-',qty='#!-',startPlan='#!-',finishPlan='#!-',startActual='#!-',finishActual='#!-',status='#!-' where mpId='#!-'";
	private static final String DELETE = "delete from manufacturing_plan where mpId='#!-'";

	private int mpId;
	private Date mpDate;
	private String no;
	private int prdId;
	private int qty;
	private Date startPlan;
	private Date finishPlan;
	private Date startActual;
	private Date finishActual;
	private int status;
	
	private String prdName;
	private String statusName;

	public static ManufacturingPlan get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(MANUFACTURING_PLAN, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<ManufacturingPlan> getAll() {
		List<ManufacturingPlan> list = new ArrayList<ManufacturingPlan>();

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
	
	public static List<ManufacturingPlan> getRunning() {
		List<ManufacturingPlan> list = new ArrayList<ManufacturingPlan>();

		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(RUNNING);
		if(result.length() > 0){
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}
	
	public static List<ManufacturingPlan> getByPeriod(String start, String end) {
		List<String> values = new ArrayList<String>();
		values.add(start);
		values.add(end);
		
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_PERIOD, values);
		if(result.length() > 0){
			List<ManufacturingPlan> list = new ArrayList<ManufacturingPlan>();
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

	private static ManufacturingPlan getObject(String row) {
		String[] cols = row.split("\t");
		ManufacturingPlan obj = new ManufacturingPlan();
		obj.setMpId(Integer.valueOf(cols[0]));
		obj.setMpDate(new Date(cols[1]));
		obj.setNo(cols[2]);
		obj.setPrdId(Integer.valueOf(cols[3]));
		obj.setQty(Integer.valueOf(cols[4]));
		obj.setStartPlan(new Date(cols[5]));
		obj.setFinishPlan(new Date(cols[6]));
		if(!"null".equals(cols[7]))
			obj.setStartActual(new Date(cols[7]));
		if(!"null".equals(cols[8]))
			obj.setFinishActual(new Date(cols[8]));
		obj.setStatus(Integer.valueOf(cols[9]));
		if(cols.length > 10)
			obj.setPrdName(cols[10]);
		if(cols.length > 11)
			obj.setStatusName(cols[11]);
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(mpDate.getFullDateTimeString());
		values.add(no);
		values.add(String.valueOf(prdId));
		values.add(String.valueOf(qty));
		values.add(startPlan.getFullDateTimeString());
		values.add(finishPlan.getFullDateTimeString());
		if(startActual == null)
			values.add("");
		else values.add(startActual.getFullDateTimeString());
		if(finishActual == null)
			values.add("");
		else values.add(finishActual.getFullDateTimeString());
		values.add(String.valueOf(status));
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
		values.add(String.valueOf(mpId));

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
		if(cmd.executeUpdate(DELETE, String.valueOf(mpId))) {
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

	public int getMpId() {
		return mpId;
	}

	public void setMpId(int mpId) {
		this.mpId = mpId;
	}

	public Date getMpDate() {
		return mpDate;
	}

	public void setMpDate(Date mpDate) {
		this.mpDate = mpDate;
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

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Date getStartPlan() {
		return startPlan;
	}

	public void setStartPlan(Date startPlan) {
		this.startPlan = startPlan;
	}

	public Date getFinishPlan() {
		return finishPlan;
	}

	public void setFinishPlan(Date finishPlan) {
		this.finishPlan = finishPlan;
	}

	public Date getStartActual() {
		return startActual;
	}

	public void setStartActual(Date startActual) {
		this.startActual = startActual;
	}

	public Date getFinishActual() {
		return finishActual;
	}

	public void setFinishActual(Date finishActual) {
		this.finishActual = finishActual;
	}

	public int getStatus() {
		return status;
	}

	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		if(status == 0)
			return "";
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
