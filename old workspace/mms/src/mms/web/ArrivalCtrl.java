package mms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import mms.domain.ArrivalPlan;
import mms.domain.Link;
import mms.domain.ProductMaster;
import mms.util.Date;

public class ArrivalCtrl implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//User user = RedirectCtrl.send(request, response);
		
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		Date startDate = null;
		Date endDate = null;
		
		if(start == null) {
			endDate = new Date();
			startDate = Date.addDays(endDate, -1);
		}
		else if(end == null || "".equals(end)) {
			startDate = new Date(start);
			endDate = new Date(start);
		}
		else {
			startDate = new Date(start);
			endDate = new Date(end);
		}
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Home", "/mms/home.htm"));
		links.add(new Link("Arrival Plan", ""));
		
		List<ProductMaster> productLst = ProductMaster.getAll();
		List<ArrivalPlan> arrivalLst = ArrivalPlan.getByPeriod(startDate.getDateString(), endDate.getDateString());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("links", links);
		model.put("productLst", productLst);
		model.put("arrivalLst", arrivalLst);
		model.put("startDate", startDate);
		model.put("endDate", endDate);
		
		return new ModelAndView("arrival", "model", model);
	}
}
