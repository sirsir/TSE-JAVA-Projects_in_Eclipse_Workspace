package mms.domain.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import mms.domain.WorkCalendar;
import mms.util.Date;


public class TAWorkCalendar {
	public static final String[] DAY_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	public static final String WT_STARTTIME = "09:00:00";
	public static final String WT_ENDTIME = "17:00:00";
	public static final String NWT_STARTTIME1 = "12:00:00";
	public static final String NWT_ENDTIME1 = "13:00:00";
	public static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public static DateFormat monthFormat = new SimpleDateFormat("MMM");
	
	private String wcdate;
	private String wcday;
	private int holiday;
	private String startWorkingTime;
	private String endWorkingTime;
	private String startNonWorkingTime1;
	private String endNonWorkingTime1;
	private String startNonWorkingTime2;
	private String endNonWorkingTime2;
	private String startNonWorkingTime3;
	private String endNonWorkingTime3;
	private String startNonWorkingTime4;
	private String endNonWorkingTime4;
	private String startNonWorkingTime5;
	private String endNonWorkingTime5;
	
	
	public static List<TAWorkCalendar> getTAWorkCalendarLst(int year, int month){
		int tMonth = month + 1;
		int day = 1;
		List<TAWorkCalendar> taWClst = new ArrayList<TAWorkCalendar>();
		Calendar calendar = new GregorianCalendar(year, month, day);
		int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		for(int i=1; i<=dayOfMonth; i++) {
			Calendar c = new GregorianCalendar(year, month, i);
			TAWorkCalendar taWC = new TAWorkCalendar();
			String date = year + "-" + (tMonth<10?"0"+tMonth:String.valueOf(tMonth)) + "-" + (i<10?"0"+i:String.valueOf(i));
			WorkCalendar wc = WorkCalendar.getByDate(date);
			Integer dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			taWC.setWcdate(date);
			taWC.setWcday(i+" ("+DAY_OF_WEEK[dayOfWeek-1]+")");
			if(wc!=null){
				taWC.setHoliday(wc.getHoliday());
				Date startWorkingTime = wc.getStartWorkingTime();
				Date endWorkingTime = wc.getEndWorkingTime();
				Date startNonWorkingTime1 = wc.getStartNonWorkingTime1();
				Date endNonWorkingTime1 = wc.getEndNonWorkingTime1();
				Date startNonWorkingTime2 = wc.getStartNonWorkingTime2();
				Date endNonWorkingTime2 = wc.getEndNonWorkingTime2();
				Date startNonWorkingTime3 = wc.getStartNonWorkingTime3();
				Date endNonWorkingTime3 = wc.getEndNonWorkingTime3();
				Date startNonWorkingTime4 = wc.getStartNonWorkingTime4();
				Date endNonWorkingTime4 = wc.getEndNonWorkingTime4();
				Date startNonWorkingTime5 = wc.getStartNonWorkingTime5();
				Date endNonWorkingTime5 = wc.getEndNonWorkingTime5();
				
				if(startWorkingTime!=null){
					taWC.setStartWorkingTime(timeFormat.format(startWorkingTime));
					taWC.setEndWorkingTime(timeFormat.format(endWorkingTime));
				}
				if(startNonWorkingTime1!=null){
					taWC.setStartNonWorkingTime1(timeFormat.format(startNonWorkingTime1));
					taWC.setEndNonWorkingTime1(timeFormat.format(endNonWorkingTime1));
				}
				if(startNonWorkingTime2!=null){
					taWC.setStartNonWorkingTime2(timeFormat.format(startNonWorkingTime2));
					taWC.setEndNonWorkingTime2(timeFormat.format(endNonWorkingTime2));
				}
				if(startNonWorkingTime3!=null){
					taWC.setStartNonWorkingTime3(timeFormat.format(startNonWorkingTime3));
					taWC.setEndNonWorkingTime3(timeFormat.format(endNonWorkingTime3));
				}
				if(startNonWorkingTime4!=null){
					taWC.setStartNonWorkingTime4(timeFormat.format(startNonWorkingTime4));
					taWC.setEndNonWorkingTime4(timeFormat.format(endNonWorkingTime4));
				}
				if(startNonWorkingTime5!=null){
					taWC.setStartNonWorkingTime5(timeFormat.format(startNonWorkingTime5));
					taWC.setEndNonWorkingTime5(timeFormat.format(endNonWorkingTime5));
				}
			}else{
				WorkCalendar newWc = new WorkCalendar();
				Date wcDate = new Date(date + " 00:00:00");
				newWc.setWcDate(wcDate);
				if(dayOfWeek == 1||dayOfWeek == 7){			
					newWc.setHoliday(1);
				}else{
					newWc.setHoliday(0);
					Date startWorkingTime = new Date(date + " " + WT_STARTTIME);
					Date endWorkingTime = new Date(date + " " + WT_ENDTIME);
					Date startNonWorkingTime1 = new Date(date + " " + NWT_STARTTIME1);
					Date endNonWorkingTime1 = new Date(date + " " + NWT_ENDTIME1);
					newWc.setStartWorkingTime(startWorkingTime);
					newWc.setEndWorkingTime(endWorkingTime);
					newWc.setStartNonWorkingTime1(startNonWorkingTime1);
					newWc.setEndNonWorkingTime1(endNonWorkingTime1);
					
					taWC.setStartWorkingTime(timeFormat.format(startWorkingTime));
					taWC.setEndWorkingTime(timeFormat.format(endWorkingTime));
					taWC.setStartNonWorkingTime1(timeFormat.format(startNonWorkingTime1));
					taWC.setEndNonWorkingTime1(timeFormat.format(endNonWorkingTime1));
				}
				taWC.setHoliday(newWc.getHoliday());
				
				newWc.storeDefaultData();
			}
			taWClst.add(taWC);
		}
		return taWClst;
	}
	
	public static int getMonth(String name) {
		int month = 0;
		
		switch(name) {
			case "Feb":
				month = 1;
				break;
			case "Mar":
				month = 2;
				break;
			case "Apr":
				month = 3;
				break;
			case "May":
				month = 4;
				break;
			case "Jun":
				month = 5;
				break;
			case "Jul":
				month = 6;
				break;
			case "Aug":
				month = 7;
				break;
			case "Sep":
				month = 8;
				break;
			case "Oct":
				month = 9;
				break;
			case "Nov":
				month = 10;
				break;
			case "Dec":
				month = 11;
				break;
		}
		return month;
	}
	
	public String getWcdate() {
		return wcdate;
	}

	public void setWcdate(String wcdate) {
		this.wcdate = wcdate;
	}
	
	public String getWcday() {
		return wcday;
	}

	public void setWcday(String wcday) {
		this.wcday = wcday;
	}

	public int getHoliday() {
		return holiday;
	}

	public void setHoliday(int holiday) {
		this.holiday = holiday;
	}

	public String getStartWorkingTime() {
		return startWorkingTime;
	}

	public void setStartWorkingTime(String startWorkingTime) {
		this.startWorkingTime = startWorkingTime;
	}

	public String getEndWorkingTime() {
		return endWorkingTime;
	}

	public void setEndWorkingTime(String endWorkingTime) {
		this.endWorkingTime = endWorkingTime;
	}

	public String getStartNonWorkingTime1() {
		return startNonWorkingTime1;
	}

	public void setStartNonWorkingTime1(String startNonWorkingTime1) {
		this.startNonWorkingTime1 = startNonWorkingTime1;
	}

	public String getEndNonWorkingTime1() {
		return endNonWorkingTime1;
	}

	public void setEndNonWorkingTime1(String endNonWorkingTime1) {
		this.endNonWorkingTime1 = endNonWorkingTime1;
	}

	public String getStartNonWorkingTime2() {
		return startNonWorkingTime2;
	}

	public void setStartNonWorkingTime2(String startNonWorkingTime2) {
		this.startNonWorkingTime2 = startNonWorkingTime2;
	}

	public String getEndNonWorkingTime2() {
		return endNonWorkingTime2;
	}

	public void setEndNonWorkingTime2(String endNonWorkingTime2) {
		this.endNonWorkingTime2 = endNonWorkingTime2;
	}

	public String getStartNonWorkingTime3() {
		return startNonWorkingTime3;
	}

	public void setStartNonWorkingTime3(String startNonWorkingTime3) {
		this.startNonWorkingTime3 = startNonWorkingTime3;
	}

	public String getEndNonWorkingTime3() {
		return endNonWorkingTime3;
	}

	public void setEndNonWorkingTime3(String endNonWorkingTime3) {
		this.endNonWorkingTime3 = endNonWorkingTime3;
	}

	public String getStartNonWorkingTime4() {
		return startNonWorkingTime4;
	}

	public void setStartNonWorkingTime4(String startNonWorkingTime4) {
		this.startNonWorkingTime4 = startNonWorkingTime4;
	}

	public String getEndNonWorkingTime4() {
		return endNonWorkingTime4;
	}

	public void setEndNonWorkingTime4(String endNonWorkingTime4) {
		this.endNonWorkingTime4 = endNonWorkingTime4;
	}

	public String getStartNonWorkingTime5() {
		return startNonWorkingTime5;
	}

	public void setStartNonWorkingTime5(String startNonWorkingTime5) {
		this.startNonWorkingTime5 = startNonWorkingTime5;
	}

	public String getEndNonWorkingTime5() {
		return endNonWorkingTime5;
	}

	public void setEndNonWorkingTime5(String endNonWorkingTime5) {
		this.endNonWorkingTime5 = endNonWorkingTime5;
	}
}
