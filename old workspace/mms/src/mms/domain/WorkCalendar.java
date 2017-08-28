package mms.domain;

import java.util.List;
import java.util.ArrayList;

import mms.db.SQLCommand;
import mms.util.Date;

public class WorkCalendar extends Lookup {
	private static final String WORK_CALENDAR = "select * from work_calendar where wcDate='#!-'";
	private static final String ALL = "select * from work_calendar";
	private static final String STORE = "insert into work_calendar(holiday,startWorkingTime,endWorkingTime,startNonWorkingTime1,endNonWorkingTime1,startNonWorkingTime2,endNonWorkingTime2,startNonWorkingTime3,endNonWorkingTime3,startNonWorkingTime4,endNonWorkingTime4,startNonWorkingTime5,endNonWorkingTime5) values('#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-','#!-')";
	private static final String STORE_DEFAULT = "insert into work_calendar(wcDate,holiday,startWorkingTime,endWorkingTime,startNonWorkingTime1,endNonWorkingTime1) values('#!-','#!-','#!-','#!-','#!-','#!-')";
	private static final String UPDATE = "update work_calendar set holiday='#!-',startWorkingTime='#!-',endWorkingTime='#!-',startNonWorkingTime1='#!-',endNonWorkingTime1='#!-',startNonWorkingTime2='#!-',endNonWorkingTime2='#!-',startNonWorkingTime3='#!-',endNonWorkingTime3='#!-',startNonWorkingTime4='#!-',endNonWorkingTime4='#!-',startNonWorkingTime5='#!-',endNonWorkingTime5='#!-' where wcDate='#!-'";
	private static final String DELETE = "delete from work_calendar where wcDate='#!-'";

	private Date wcDate;
	private int holiday;
	private Date startWorkingTime;
	private Date endWorkingTime;
	private Date startNonWorkingTime1;
	private Date endNonWorkingTime1;
	private Date startNonWorkingTime2;
	private Date endNonWorkingTime2;
	private Date startNonWorkingTime3;
	private Date endNonWorkingTime3;
	private Date startNonWorkingTime4;
	private Date endNonWorkingTime4;
	private Date startNonWorkingTime5;
	private Date endNonWorkingTime5;

	public String TAWorkCalendarToString(){
		String tmp = wcDate.getCalendarFormat();
		tmp += "/t" + holiday;
		if(startWorkingTime == null)
			tmp += " /t" ;
		else tmp += "/t" +startWorkingTime.getTimeString();
		if(endWorkingTime == null)
			tmp += " /t" ;
		else tmp += "/t" +endWorkingTime.getTimeString();
		
		if(startNonWorkingTime1 == null)
			tmp += " /t" ;
		else tmp += "/t" +startNonWorkingTime1.getTimeString();
		if(endNonWorkingTime1 == null)
			tmp += " /t" ;
		else tmp += "/t" +endNonWorkingTime1.getTimeString();
		
		if(startNonWorkingTime2 == null)
			tmp += " /t" ;
		else tmp += "/t" +startNonWorkingTime2.getTimeString();
		if(endNonWorkingTime2 == null)
			tmp += " /t" ;
		else tmp += "/t" +endNonWorkingTime2.getTimeString();
		if(startNonWorkingTime3 == null)
			tmp += " /t" ;
		else tmp += "/t" +startNonWorkingTime3.getTimeString();
		if(endNonWorkingTime3 == null)
			tmp += " /t" ;
		else tmp += "/t" +endNonWorkingTime3.getTimeString();
		if(startNonWorkingTime4 == null)
			tmp += " /t" ;
		else tmp += "/t" +startNonWorkingTime4.getTimeString();
		if(endNonWorkingTime4 == null)
			tmp += " /t" ;
		else tmp += "/t" +endNonWorkingTime4.getTimeString();
		if(startNonWorkingTime5 == null)
			tmp += " /t" ;
		else tmp += "/t" +startNonWorkingTime5.getTimeString();
		if(endNonWorkingTime5 == null)
			tmp += " /t" ;
		else tmp += "/t" +endNonWorkingTime5.getTimeString();
		
		return tmp;}
	
	public static WorkCalendar getByDate(String wcDate) {
		SQLCommand cmd = new SQLCommand();
		String result = cmd.executeQuery(WORK_CALENDAR, wcDate);
		if(result.length() > 0)
			return getObject(result);
		else return null;
	}

	public static List<WorkCalendar> getAll() {
		List<WorkCalendar> list = new ArrayList<WorkCalendar>();

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

	private static WorkCalendar getObject(String row) {
		String[] cols = row.split("\t");

		WorkCalendar obj = new WorkCalendar();
		obj.setWcDate(new Date(cols[0]));
		obj.setHoliday(Integer.valueOf(cols[1]));
		if(!cols[2].equals("null"))
			obj.setStartWorkingTime(new Date(cols[0] + " "+ cols[2]));
		if(!cols[3].equals("null"))
			obj.setEndWorkingTime(new Date(cols[0] + " " + cols[3]));
		if(!cols[4].equals("null"))
			obj.setStartNonWorkingTime1(new Date(cols[0] + " " + cols[4]));
		if(!cols[5].equals("null"))
			obj.setEndNonWorkingTime1(new Date(cols[0] + " " + cols[5]));
		if(!cols[6].equals("null"))
			obj.setStartNonWorkingTime2(new Date(cols[0] + " " + cols[6]));
		if(!cols[7].equals("null"))	
			obj.setEndNonWorkingTime2(new Date(cols[0] + " " + cols[7]));
		if(!cols[8].equals("null"))
			obj.setStartNonWorkingTime3(new Date(cols[0] + " " + cols[8]));
		if(!cols[9].equals("null"))
			obj.setEndNonWorkingTime3(new Date(cols[0] + " " + cols[9]));
		if(!cols[10].equals("null"))
			obj.setStartNonWorkingTime4(new Date(cols[0] + " " + cols[10]));
		if(!cols[11].equals("null"))
			obj.setEndNonWorkingTime4(new Date(cols[0] + " " + cols[11]));
		if(!cols[12].equals("null"))
			obj.setStartNonWorkingTime5(new Date(cols[0] + " " + cols[12]));
		if(!cols[13].equals("null"))
			obj.setEndNonWorkingTime5(new Date(cols[0] + " " + cols[13]));
		return obj;
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		values.add(String.valueOf(holiday));
		if(startWorkingTime == null)
			values.add("");
		else values.add(startWorkingTime.getFullDateTimeString());
		
		if(endWorkingTime == null)
			values.add("");
		else values.add(endWorkingTime.getFullDateTimeString());
		
		if(startNonWorkingTime1 == null)
			values.add("");
		else values.add(startNonWorkingTime1.getFullDateTimeString());
		
		if(endNonWorkingTime1 == null)
			values.add("");
		else values.add(endNonWorkingTime1.getFullDateTimeString());
		
		if(startNonWorkingTime2 == null)
			values.add("");
		else values.add(startNonWorkingTime2.getFullDateTimeString());
		
		if(endNonWorkingTime2 == null)
			values.add("");
		else values.add(endNonWorkingTime2.getFullDateTimeString());
		
		if(startNonWorkingTime3 == null)
			values.add("");
		else values.add(startNonWorkingTime3.getFullDateTimeString());
		
		if(endNonWorkingTime3 == null)
			values.add("");
		else values.add(endNonWorkingTime3.getFullDateTimeString());
		
		if(startNonWorkingTime4 == null)
			values.add("");
		else values.add(startNonWorkingTime4.getFullDateTimeString());
		
		if(endNonWorkingTime4 == null)
			values.add("");
		else values.add(endNonWorkingTime4.getFullDateTimeString());
		
		if(startNonWorkingTime5 == null)
			values.add("");
		else values.add(startNonWorkingTime5.getFullDateTimeString());
		
		if(endNonWorkingTime5 == null)
			values.add("");
		else values.add(endNonWorkingTime5.getFullDateTimeString());
		
		return values;
	}

	public int store() {
		List<String> values = getValues();
		values.add(wcDate.getFullDateTimeString());
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
	public int storeDefaultData() {
		List<String> values = new ArrayList<String>();
		values.add(wcDate.getFullDateTimeString());
		values.add(String.valueOf(holiday));
		if(startWorkingTime != null)
			values.add(startWorkingTime.getFullDateTimeString());
		else
			values.add("");
		
		if(endWorkingTime != null)
			values.add(endWorkingTime.getFullDateTimeString());
		else
			values.add("");
		
		if(startNonWorkingTime1 != null)
			values.add(startNonWorkingTime1.getFullDateTimeString());
		else
			values.add("");
		
		if(endNonWorkingTime1 != null)
			values.add(endNonWorkingTime1.getFullDateTimeString());
		else
			values.add("");
		
		SQLCommand cmd = new SQLCommand();
		if(cmd.executeUpdate(STORE_DEFAULT, values)) {
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
		values.add(wcDate.getFullDateTimeString());
		
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
		values.add(String.valueOf(holiday));
		values.add(startWorkingTime.getFullDateTimeString());
		values.add(endWorkingTime.getFullDateTimeString());
		values.add(startNonWorkingTime1.getFullDateTimeString());
		values.add(endNonWorkingTime1.getFullDateTimeString());
		values.add(startNonWorkingTime2.getFullDateTimeString());
		values.add(endNonWorkingTime2.getFullDateTimeString());
		values.add(startNonWorkingTime3.getFullDateTimeString());
		values.add(endNonWorkingTime3.getFullDateTimeString());
		values.add(startNonWorkingTime4.getFullDateTimeString());
		values.add(endNonWorkingTime4.getFullDateTimeString());
		values.add(startNonWorkingTime5.getFullDateTimeString());
		values.add(endNonWorkingTime5.getFullDateTimeString());

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

	public Date getWcDate() {
		return wcDate;
	}

	public void setWcDate(Date wcDate) {
		this.wcDate = wcDate;
	}

	public int getHoliday() {
		return holiday;
	}

	public void setHoliday(int holiday) {
		this.holiday = holiday;
	}

	public Date getStartWorkingTime() {
		return startWorkingTime;
	}

	public void setStartWorkingTime(Date startWorkingTime) {
		this.startWorkingTime = startWorkingTime;
	}

	public Date getEndWorkingTime() {
		return endWorkingTime;
	}

	public void setEndWorkingTime(Date endWorkingTime) {
		this.endWorkingTime = endWorkingTime;
	}

	public Date getStartNonWorkingTime1() {
		return startNonWorkingTime1;
	}

	public void setStartNonWorkingTime1(Date startNonWorkingTime1) {
		this.startNonWorkingTime1 = startNonWorkingTime1;
	}

	public Date getEndNonWorkingTime1() {
		return endNonWorkingTime1;
	}

	public void setEndNonWorkingTime1(Date endNonWorkingTime1) {
		this.endNonWorkingTime1 = endNonWorkingTime1;
	}

	public Date getStartNonWorkingTime2() {
		return startNonWorkingTime2;
	}

	public void setStartNonWorkingTime2(Date startNonWorkingTime2) {
		this.startNonWorkingTime2 = startNonWorkingTime2;
	}

	public Date getEndNonWorkingTime2() {
		return endNonWorkingTime2;
	}

	public void setEndNonWorkingTime2(Date endNonWorkingTime2) {
		this.endNonWorkingTime2 = endNonWorkingTime2;
	}

	public Date getStartNonWorkingTime3() {
		return startNonWorkingTime3;
	}

	public void setStartNonWorkingTime3(Date startNonWorkingTime3) {
		this.startNonWorkingTime3 = startNonWorkingTime3;
	}

	public Date getEndNonWorkingTime3() {
		return endNonWorkingTime3;
	}

	public void setEndNonWorkingTime3(Date endNonWorkingTime3) {
		this.endNonWorkingTime3 = endNonWorkingTime3;
	}

	public Date getStartNonWorkingTime4() {
		return startNonWorkingTime4;
	}

	public void setStartNonWorkingTime4(Date startNonWorkingTime4) {
		this.startNonWorkingTime4 = startNonWorkingTime4;
	}

	public Date getEndNonWorkingTime4() {
		return endNonWorkingTime4;
	}

	public void setEndNonWorkingTime4(Date endNonWorkingTime4) {
		this.endNonWorkingTime4 = endNonWorkingTime4;
	}

	public Date getStartNonWorkingTime5() {
		return startNonWorkingTime5;
	}

	public void setStartNonWorkingTime5(Date startNonWorkingTime5) {
		this.startNonWorkingTime5 = startNonWorkingTime5;
	}

	public Date getEndNonWorkingTime5() {
		return endNonWorkingTime5;
	}

	public void setEndNonWorkingTime5(Date endNonWorkingTime5) {
		this.endNonWorkingTime5 = endNonWorkingTime5;
	}

	public void setWcDate(java.util.Date time) {
		// TODO Auto-generated method stub
		
	}

}
