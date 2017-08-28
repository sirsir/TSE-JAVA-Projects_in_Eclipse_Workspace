package mms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mms.domain.Link;
import mms.domain.Role;
import mms.domain.UserMaster;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class UserMasterCtrl implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Home", "/mms/home.htm"));
		links.add(new Link("User Master", ""));
		
		List<Role> roleLst = Role.getAll();
		List<UserMaster> userLst = UserMaster.getAll();
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("links", links);
		model.put("roleLst", roleLst);
		model.put("userLst", userLst);
		
		
		return new ModelAndView("user_master", "model", model);
	}
}
