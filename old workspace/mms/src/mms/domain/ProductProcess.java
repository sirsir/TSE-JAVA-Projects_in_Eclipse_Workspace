package mms.domain;

import java.util.List;
import java.util.ArrayList;

import mms.db.SQLCommand;
import mms.util.Date;

public class ProductProcess extends Lookup {
	private static final String PRODUCT_PROCESS = "select pp.*, (select p.name from process p where p.prcId=pp.prcId) name from product_process pp where pp.ppId='#!-'";
	private static final String BY_FIRST_PROCESS = "select * from product_process where mpId=#!- and prcId=1";
	private static final String BY_MPID_PRCID =  "select pp.*, (select p.name from process p where p.prcId=pp.prcId) name from product_process pp where pp.mpId=#!- and pp.prcId=#!-";
	private static final String ALL = "select * from product_process";
	private static final String BY_MPID = "select pp.*, (select p.name from process p where p.prcId=pp.prcId) name from product_process pp where pp.mpId='#!-' order by ppId";
	private static final String STORE = "insert into product_process(mpId,prcId,planQty,resultQty,defectiveQty,startActual,finishActual) values('#!-','#!-','#!-','#!-','#!-','#!-','#!-')";
	private static final String STORE_INIT = "insert into product_process(mpId,prcId,planQty) values('#!-','#!-','#!-')";
	private static final String UPDATE = "update product_process set mpId='#!-',prcId='#!-',planQty='#!-',resultQty='#!-',defectiveQty='#!-',startActual='#!-',finishActual='#!-' where ppId='#!-'";
	private static final String DELETE = "delete from product_process where ppId='#!-'";

	private int ppId;
	private int mpId;
	private int prcId;
	private int planQty;
	private int resultQty;
	private int defectiveQty;
	private Date startActual;
	private Date finishActual;
	
	private String prcName;

	public String toString() {
		return ppId + "\t" + mpId + "\t" + prcId + "\t" + planQty + "\t" + resultQty + "\t" + defectiveQty + "\t" + (startActual==null?"":startActual.getFullDateTimeString()) + "\t" + (finishActual==null?"":finishActual.getFullDateTimeString()) + "\t" + prcName;
	}
	public static ProductProcess get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(PRODUCT_PROCESS, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static ProductProcess getByFirstProcess(String mpId) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_FIRST_PROCESS, mpId);
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static ProductProcess getByMpIdPrcId(String mpId, String prcId) {
		List<String> values = new ArrayList<String>();
		values.add(mpId);
		values.add(prcId);
		
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_MPID_PRCID, values);
		if(!"null".equals(result) && result.length() > 0)
			return getObject(result);
		return null;
	}
	
	public static List<ProductProcess> getAll() {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(ALL);
		if(result.length() > 0){
			List<ProductProcess> list = new ArrayList<ProductProcess>();
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}
	
	public static List<ProductProcess> getByMpId(int mpId) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_MPID, String.valueOf(mpId));
		if(result.length() > 0){
			List<ProductProcess> list = new ArrayList<ProductProcess>();
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}

	private static ProductProcess getObject(String row) {
		String[] cols = row.split("\t");

		ProductProcess obj = new ProductProcess();
		obj.setPpId(Integer.valueOf(cols[0]));
		obj.setMpId(Integer.valueOf(cols[1]));
		obj.setPrcId(Integer.valueOf(cols[2]));
		obj.setPlanQty(Integer.valueOf(cols[3]));
		if(!"null".equals(cols[4]))
			obj.setResultQty(Integer.valueOf(cols[4]));
		if(!"null".equals(cols[5]))
			obj.setDefectiveQty(Integer.valueOf(cols[5]));
		if(!"null".equals(cols[6]))
			obj.setStartActual(new Date(cols[6]));
		if(!"null".equals(cols[7]))
			obj.setFinishActual(new Date(cols[7]));
		if(cols.length > 8)
			obj.setPrcName(cols[8]);
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(mpId));
		values.add(String.valueOf(prcId));
		values.add(String.valueOf(planQty));
		if(resultQty == 0)
			values.add("");
		else values.add(String.valueOf(resultQty));
		if(defectiveQty == 0)
			values.add("");
		else values.add(String.valueOf(defectiveQty));
		if(startActual == null)
			values.add("");
		else values.add(startActual.getFullDateTimeString());
		if(finishActual == null)
			values.add("");
		else values.add(finishActual.getFullDateTimeString());
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
	
	public static boolean store(SQLCommand cmd, int mpId, int prcId, int planQty) {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(mpId));
		values.add(String.valueOf(prcId));
		values.add(String.valueOf(planQty));
		
		return cmd.executeUpdate(STORE_INIT, values); 
	}

	public boolean update() {
		List<String> values = getValues();
		values.add(String.valueOf(ppId));

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
		if(cmd.executeUpdate(DELETE, String.valueOf(ppId))) {
			message = "success";
			return true;
		}
		else {
			message = cmd.getMessage();
			return false;
		}
	}

	public int getPpId() {
		return ppId;
	}

	public void setPpId(int ppId) {
		this.ppId = ppId;
	}

	public int getMpId() {
		return mpId;
	}

	public void setMpId(int mpId) {
		this.mpId = mpId;
	}

	public int getPrcId() {
		return prcId;
	}

	public void setPrcId(int prcId) {
		this.prcId = prcId;
	}

	public int getPlanQty() {
		return planQty;
	}

	public void setPlanQty(int planQty) {
		this.planQty = planQty;
	}

	public int getResultQty() {
		return resultQty;
	}

	public void setResultQty(int resultQty) {
		this.resultQty = resultQty;
	}

	public int getDefectiveQty() {
		return defectiveQty;
	}

	public void setDefectiveQty(int defectiveQty) {
		this.defectiveQty = defectiveQty;
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

	public String getPrcName() {
		return prcName;
	}

	public void setPrcName(String prcName) {
		this.prcName = prcName;
	}

}
