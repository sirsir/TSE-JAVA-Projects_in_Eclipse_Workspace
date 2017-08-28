package mms.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mms.db.SQLCommand;
import mms.util.Date;

public class Production extends Lookup {
	private static final String PRODUCTION = "select * from production where pdtId='#!-'";
	private static final String BY_BARCODE = "select p.* from production p where p.barcode='#!-' or p.serialNo='#!-' order by ppId limit 0,1";
	private static final String BY_MPID_BARCODE = "select p.*,'',pp.mpId from production p,product_process pp where pp.ppId=p.ppId and pp.mpId=#!- and (p.barcode='#!-' or p.serialNo='#!-') order by ppId desc limit 0,1";
	private static final String ALL = "select * from production";
	private static final String COUNT = "select count(*) from production where ppId=#!-";
	private static final String RESULT_COUNT = "select result,count(*) from production where ppId=#!- group by result";
	private static final String SN = "select distinct p.serialNo from production p where p.serialNo is not null and p.barcode='#!-' and p.ppId in (select pp.ppId from product_process pp where pp.mpId=#!-)";
	
	private static final String BY_PPID = "select p.*, (select name from subcategory s where s.scatId=p.result) rname from production p where p.ppId='#!-' order by barcode";
	private static final String STORE = "insert into production(ppId,serialNo,barcode,startActual,finishActual,result) values('#!-','#!-','#!-','#!-','#!-','#!-')";
	private static final String UPDATE = "update production set ppId='#!-',serialNo='#!-',barcode='#!-',startActual='#!-',finishActual='#!-',result='#!-' where pdtId='#!-'";
	private static final String DELETE = "delete from production where pdtId='#!-'";

	private int pdtId;
	private int ppId;
	private String serialNo;
	private String barcode;
	private Date startActual;
	private Date finishActual;
	private int result;
	
	private String resultName;
	private int mpId;

	public String toString() {
		return pdtId + "\t" + ppId + "\t" + (serialNo == null?"":serialNo) + "\t" + barcode + "\t" + (startActual==null?"":startActual.getFullDateTimeString()) + "\t" + (finishActual==null?"":finishActual.getFullDateTimeString()) + "\t" + result + "\t" + getResultName() + "\t" + mpId;
	}
	public static Production get(int id) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(PRODUCTION, String.valueOf(id));
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static String getCount(int ppId) {
		SQLCommand cmd = new SQLCommand();
		return cmd.executeQuery(COUNT, String.valueOf(ppId));
	}
	
	public static Map<Integer, Integer> getResultCount(int ppId) {
		Map<Integer, Integer> resultCntMap = new HashMap<Integer, Integer>();
		
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(RESULT_COUNT, String.valueOf(ppId));
		if(result.length() > 0) {
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				String[] cols = rows[i].split("\t");
				resultCntMap.put(Integer.valueOf(cols[0]), Integer.valueOf(cols[1]));
			}
		}
		return resultCntMap;
	}
	
	public static String getSn(String barcode, String mpId) {
		List<String> values = new ArrayList<String>();
		values.add(barcode);
		values.add(mpId);
		
		SQLCommand cmd = new SQLCommand();
		return cmd.executeQuery(RESULT_COUNT, values);
	}
	
	public static Production getByBarcode(String barcode) {
		List<String> values = new ArrayList<String>();
		values.add(barcode);
		values.add(barcode);
		
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_BARCODE, values);
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static Production getByMpIdBarcode(String mpId, String barcode) {
		List<String> values = new ArrayList<String>();
		values.add(mpId);
		values.add(barcode);
		values.add(barcode);
		
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_MPID_BARCODE, values);
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<Production> getAll() {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(ALL);
		if(result.length() > 0){
			List<Production> list = new ArrayList<Production>();
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}

	public static List<Production> getByPpId(int ppId) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(BY_PPID, String.valueOf(ppId));
		if(result.length() > 0){
			List<Production> list = new ArrayList<Production>();
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}

	private static Production getObject(String row) {
		String[] cols = row.split("\t");
		Production obj = new Production();
		obj.setPdtId(Integer.valueOf(cols[0]));
		obj.setPpId(Integer.valueOf(cols[1]));
		if(!"null".equals(cols[2]))
			obj.setSerialNo(cols[2]);
		obj.setBarcode(cols[3]);
		if(!"null".equals(cols[4]))
			obj.setStartActual(new Date(cols[4]));
		if(!"null".equals(cols[5]))
			obj.setFinishActual(new Date(cols[5]));
		obj.setResult(Integer.valueOf(cols[6]));
		if(cols.length > 7)
			obj.setResultName(cols[7]);
		if(cols.length > 8)
				obj.setMpId(Integer.valueOf(cols[8]));
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(ppId));
		values.add(serialNo);
		values.add(barcode);
		if(startActual == null)
			values.add("");
		else values.add(startActual.getFullDateTimeString());
		if(finishActual == null)
			values.add("");
		else values.add(finishActual.getFullDateTimeString());
		values.add(String.valueOf(result));
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
		values.add(String.valueOf(pdtId));

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
		if(cmd.executeUpdate(DELETE, String.valueOf(pdtId))) {
			message = "success";
			return true;
		}
		else {
			message = cmd.getMessage();
			return false;
		}
	}

	public int getPdtId() {
		return pdtId;
	}

	public void setPdtId(int pdtId) {
		this.pdtId = pdtId;
	}

	public int getPpId() {
		return ppId;
	}

	public void setPpId(int ppId) {
		this.ppId = ppId;
	}

	public String getSerialNo() {
		if(serialNo == null)
			serialNo = "";
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getResultName() {
		if(resultName == null || "null".equals(resultName))
			return "";
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}
	public int getMpId() {
		return mpId;
	}
	public void setMpId(int mpId) {
		this.mpId = mpId;
	}

}
