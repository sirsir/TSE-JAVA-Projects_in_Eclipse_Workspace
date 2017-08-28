package mms.domain;

import java.util.List;
import java.util.ArrayList;

import mms.db.SQLCommand;
import mms.util.Date;

public class ProductProcessSpec extends Lookup {
	private static final String PRODUCT_PROCESS_SPEC = "select * from product_process_spec  where prdId='#!-' and prcId='#!-'";
	private static final String PRODUCT_PROCESS_SPEC_QUERY = "select pps.*,(select prc.name from process prc where prc.prcId=pps.prcId) prcName,(select spec.name from specification_master spec where spec.specId=pps.specId) specName from product_process_spec pps where pps.prdId='#!-' and pps.prcId='#!-'";
	private static final String PRODUCT_PROCESS_SPEC_PRCID = "select pps.*,(select prc.name from process prc where prc.prcId=pps.prcId) prcName,(select spec.name from specification_master spec where spec.specId=pps.specId) specName from product_process_spec pps where pps.prdId='#!-'";
	private static final String ALL = "select  pps.*,(select prc.name from process prc where prc.prcId=pps.prcId) prdName,(select spec.name from specification_master spec where spec.specId=pps.specId) specName from product_process_spec pps";
	private static final String STORE = "insert into product_process_spec(content,standardTime,manPower,unitCount,printLabel,unitSize,specId,prdId,prcId) values('#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-')";
	private static final String STORE_INIT = "insert into product_process_spec(prdId,prcId) values('#!-','#!-')";
	private static final String UPDATE = "update product_process_spec set content='#!-',standardTime='#!-',manPower='#!-',unitCount='#!-',printLabel='#!-',unitSize='#!-',specId='#!-' where prcId='#!-' and prdId='#!-'";
	private static final String DELETE = "delete from product_process_spec where prcId='#!-' and prdId='#!-'";

	private int prdId;
	private int prcId;
	private String content;
	private int standardTime;
	private int manPower;
	private int unitCount;
	private int printLabel;
	private int unitSize;
	private int specId;
	
	private String prdName ;
	private String specName;
	
	public String toString(){
		return prcId+"/t"+prdName+"/t"+content+"/t"+standardTime+"/t"+manPower+"/t"+unitCount+"/t"+printLabel+"/t"+unitSize+"/t"+specName;
	}
	
	public static ProductProcessSpec get(int prdId,int prcId) {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(prdId));
		values.add(String.valueOf(prcId));
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(PRODUCT_PROCESS_SPEC, values);
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static ProductProcessSpec getQuery(int prdId,int prcId) {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(prdId));
		values.add(String.valueOf(prcId));
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(PRODUCT_PROCESS_SPEC_QUERY, values);
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}
	
	public static List<ProductProcessSpec> getByProductMasterId(int prdId) {
		List<ProductProcessSpec> list = new ArrayList<ProductProcessSpec>();
		
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(PRODUCT_PROCESS_SPEC_PRCID, String.valueOf(prdId));
		if(result.length() > 0){
			String[] rows = result.split("_n-");
			for(int i=0; i<rows.length; i++) {
				list.add(getObject(rows[i]));
			}
			return list;
		}
		else return null;
	}

	public static List<ProductProcessSpec> getAll() {
		List<ProductProcessSpec> list = new ArrayList<ProductProcessSpec>();

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

	private static ProductProcessSpec getObject(String row) {
		String[] cols = row.split("\t");

		ProductProcessSpec obj = new ProductProcessSpec();
		obj.setPrdId(Integer.valueOf(cols[0]));
		obj.setPrcId(Integer.valueOf(cols[1]));
		obj.setContent(cols[2]);
		if(!"null".equals(cols[3]))
			obj.setStandardTime(Integer.valueOf(cols[3]));
		if(!"null".equals(cols[4]))
			obj.setManPower(Integer.valueOf(cols[4]));
		if(!"null".equals(cols[5]))
			obj.setUnitCount(Integer.valueOf(cols[5]));
		if(!"null".equals(cols[6]))
			obj.setPrintLabel(Integer.valueOf(cols[6]));
		if(!"null".equals(cols[7]))
			obj.setUnitSize(Integer.valueOf(cols[7]));
		if(!"null".equals(cols[8]))
			obj.setSpecId(Integer.valueOf(cols[8]));
		if(cols.length>9){
			obj.setPrdName(cols[9]);
			obj.setSpecName(cols[10]);
		}
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(content);
		values.add(String.valueOf(standardTime));
		values.add(String.valueOf(manPower));
		values.add(String.valueOf(unitCount));
		values.add(String.valueOf(printLabel));
		values.add(String.valueOf(unitSize));
		if(specId == 0)
			values.add("");
		else values.add(String.valueOf(specId));
		return values;
	}

	public int store() {
		List<String> values = getValues();
		values.add(String.valueOf(prdId));
		values.add(String.valueOf(prcId));
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
	public int store(SQLCommand cmd){
		List<String> values = getValues();
		values.add(String.valueOf(prdId));
		values.add(String.valueOf(prcId));
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
	public static boolean store(SQLCommand cmd, int prdId, int prcId) {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(prdId));
		values.add(String.valueOf(prcId));
		
		return cmd.executeUpdate(STORE_INIT, values); 
	}

	public boolean update() {
		List<String> values = getValues();
		values.add(String.valueOf(prcId));
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
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(prcId));
		values.add(String.valueOf(prdId));

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

	public int getPrdId() {
		return prdId;
	}

	public void setPrdId(int prdId) {
		this.prdId = prdId;
	}

	public int getPrcId() {
		return prcId;
	}

	public void setPrcId(int prcId) {
		this.prcId = prcId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if(content == null || "null".equals(content))
			content = "";
		this.content = content;
	}

	public int getStandardTime() {
		return standardTime;
	}

	public void setStandardTime(int standardTime) {
		this.standardTime = standardTime;
	}

	public int getManPower() {
		return manPower;
	}

	public void setManPower(int manPower) {
		this.manPower = manPower;
	}

	public int getUnitCount() {
		return unitCount;
	}

	public void setUnitCount(int unitCount) {
		this.unitCount = unitCount;
	}

	public int getPrintLabel() {
		return printLabel;
	}

	public void setPrintLabel(int printLabel) {
		this.printLabel = printLabel;
	}

	public int getUnitSize() {
		return unitSize;
	}

	public void setUnitSize(int unitSize) {
		this.unitSize = unitSize;
	}

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}
	
	public String getPrdName() {
		return prdName;
	}

	public void setPrdName(String prdName) {
		this.prdName = prdName;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		if(specName == null || "null".equals(specName))
			specName = "";
		this.specName = specName;
	}
}
