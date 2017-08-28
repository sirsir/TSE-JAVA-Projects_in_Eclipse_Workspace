package mms.ws;


import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import mms.domain.WorkCalendar;
import mms.domain.view.TAWorkCalendar;
import mms.util.Date;

@Path("/workcalendarWs")
public class WorkCalendarWs {
	@PUT
	public String updatedefault(@FormParam("date") String date) {
		
		WorkCalendar wc = WorkCalendar.getByDate(date);
		Date wcDate = new Date(date + " 00:00:00");
		wc.setWcDate(wcDate);
		wc.setHoliday(0);
		Date startWorkingTime = new Date(date + " " + TAWorkCalendar.WT_STARTTIME);
		Date endWorkingTime = new Date(date + " " + TAWorkCalendar.WT_ENDTIME);
		Date startNonWorkingTime1 = new Date(date + " " + TAWorkCalendar.NWT_STARTTIME1);
		Date endNonWorkingTime1 = new Date(date + " " + TAWorkCalendar.NWT_ENDTIME1);
		wc.setStartWorkingTime(startWorkingTime);
		wc.setEndWorkingTime(endWorkingTime);
		wc.setStartNonWorkingTime1(startNonWorkingTime1);
		wc.setEndNonWorkingTime1(endNonWorkingTime1);
		wc.setStartNonWorkingTime2(null);
		wc.setEndNonWorkingTime2(null);
		wc.setStartNonWorkingTime3(null);
		wc.setEndNonWorkingTime3(null);
		wc.setStartNonWorkingTime4(null);
		wc.setEndNonWorkingTime4(null);
		wc.setStartNonWorkingTime5(null);
		wc.setEndNonWorkingTime5(null);
		wc.update();
		return wc.TAWorkCalendarToString();
	}
	
	@POST
	public String update(@FormParam("date") String date, @FormParam("holiday") String holiday, @FormParam("startWorkingTime") String startWorkingTime,@FormParam("endWorkingTime") String endWorkingTime,
			@FormParam("startNonWorkingTime1") String startNonWorkingTime1,@FormParam("endNonWorkingTime1") String endNonWorkingTime1,
			@FormParam("startNonWorkingTime2") String startNonWorkingTime2,@FormParam("endNonWorkingTime2") String endNonWorkingTime2,
			@FormParam("startNonWorkingTime3") String startNonWorkingTime3,@FormParam("endNonWorkingTime3") String endNonWorkingTime3,
			@FormParam("startNonWorkingTime4") String startNonWorkingTime4,@FormParam("endNonWorkingTime4") String endNonWorkingTime4,
			@FormParam("startNonWorkingTime5") String startNonWorkingTime5,@FormParam("endNonWorkingTime5") String endNonWorkingTime5) {
		WorkCalendar wc = WorkCalendar.getByDate(date);
		Date wcDate = new Date(date + " 00:00:00");
		wc.setWcDate(wcDate);
		wc.setHoliday(Integer.valueOf(holiday));
		Date dateStartWorkingTime = null;
		Date dateEndWorkingTime = null; 
		Date dateStartNonWorkingTime1 = null;
		Date dateEndNonWorkingTime1= null;
		Date dateStartNonWorkingTime2 = null;
		Date dateEndNonWorkingTime2= null;
		Date dateStartNonWorkingTime3 = null;
		Date dateEndNonWorkingTime3= null;
		Date dateStartNonWorkingTime4 = null;
		Date dateEndNonWorkingTime4= null;
		Date dateStartNonWorkingTime5 = null;
		Date dateEndNonWorkingTime5= null;
		
		if(holiday.equals("0")){
			if(!startWorkingTime.equals(""))
				dateStartWorkingTime = new Date(date + " " + startWorkingTime+":00");
			if(!endWorkingTime.equals(""))
				dateEndWorkingTime = new Date(date + " " + endWorkingTime+":00");
			if(!startNonWorkingTime1.equals(""))
				dateStartNonWorkingTime1 = new Date(date + " " + startNonWorkingTime1+":00");
			if(!endNonWorkingTime1.equals(""))
				dateEndNonWorkingTime1 = new Date(date + " " + endNonWorkingTime1+":00");
			if(!startNonWorkingTime2.equals(""))
				dateStartNonWorkingTime2 = new Date(date + " " + startNonWorkingTime2+":00");
			if(!endNonWorkingTime2.equals(""))
				dateEndNonWorkingTime2 = new Date(date + " " + endNonWorkingTime2+":00");
			if(!startNonWorkingTime3.equals(""))
				dateStartNonWorkingTime3 = new Date(date + " " + startNonWorkingTime3+":00");
			if(!endNonWorkingTime3.equals(""))
				dateEndNonWorkingTime3 = new Date(date + " " + endNonWorkingTime3+":00");
			if(!startNonWorkingTime4.equals(""))
				dateStartNonWorkingTime4 = new Date(date + " " + startNonWorkingTime4+":00");
			if(!endNonWorkingTime4.equals(""))
				dateEndNonWorkingTime4 = new Date(date + " " + endNonWorkingTime4+":00");
			if(!startNonWorkingTime5.equals(""))
				dateStartNonWorkingTime5 = new Date(date + " " + startNonWorkingTime5+":00");
			if(!endNonWorkingTime5.equals(""))
				dateEndNonWorkingTime5 = new Date(date + " " + endNonWorkingTime5+":00");
		}
		wc.setStartWorkingTime(dateStartWorkingTime);
		wc.setEndWorkingTime(dateEndWorkingTime);
		wc.setStartNonWorkingTime1(dateStartNonWorkingTime1);
		wc.setEndNonWorkingTime1(dateEndNonWorkingTime1);
		wc.setStartNonWorkingTime2(dateStartNonWorkingTime2);
		wc.setEndNonWorkingTime2(dateEndNonWorkingTime2);
		wc.setStartNonWorkingTime3(dateStartNonWorkingTime3);
		wc.setEndNonWorkingTime3(dateEndNonWorkingTime3);
		wc.setStartNonWorkingTime4(dateStartNonWorkingTime4);
		wc.setEndNonWorkingTime4(dateEndNonWorkingTime4);
		wc.setStartNonWorkingTime5(dateStartNonWorkingTime5);
		wc.setEndNonWorkingTime5(dateEndNonWorkingTime5);
		wc.update();
		return wc.TAWorkCalendarToString();
	}
	
}
