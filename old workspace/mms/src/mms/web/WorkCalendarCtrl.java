package mms.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mms.domain.Link;
import mms.domain.view.TAWorkCalendar;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class WorkCalendarCtrl implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Home", "/mms/home.htm"));
		links.add(new Link("Work Calendar", ""));
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		String monthYear = request.getParameter("monthYear");
		
		if(monthYear == null) {
			String month = request.getParameter("month");
			if(month ==  null)
				cal = Calendar.getInstance(Locale.ENGLISH);
			else {
				String year = request.getParameter("year");
				cal = new GregorianCalendar(Integer.valueOf(year), Integer.valueOf(month), 1);
			}
		}
		else {
			String[] names = monthYear.split(" ");
			int month = TAWorkCalendar.getMonth(names[0]);
			int year = Integer.valueOf(names[1]);
			
			String mtype = request.getParameter("mtype");
			
			if("next".equals(mtype)) {
				++month;
				if(month > 11) {
					month = 0;
					++year;
				}
			}
			else if("previous".equals(mtype)) {
				--month;
				if(month < 0) {
					month = 11;
					--year;
				}
			}
			cal = new GregorianCalendar(year, month, 1);
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		List<TAWorkCalendar> taWclst = TAWorkCalendar.getTAWorkCalendarLst(year, month);
		monthYear = TAWorkCalendar.monthFormat.format(cal.getTime()) + " " + year;
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("links", links);
		model.put("taWclst", taWclst);
		model.put("monthYear", monthYear);
		return new ModelAndView("work_calendar", "model", model);
	}
}
