package mms.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class LoginCtrl implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		String uri = request.getParameter("uri");
		String err = request.getParameter("err");
		String message ="";
		
		if("AcsDnl".equals(err)){
			message = "Access Denied";
		}
		else if("StgAcsDnl".equals(err)){
			message = "Access Denied:<br> You do not have permission to visit this stage.<br>"
					+ "Please contact your supervisor.";
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("uri", uri);
		model.put("message", message);*/
		return new ModelAndView("login");
	}
}
