package mms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mms.domain.Link;
import mms.domain.SpecificationMaster;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class SpecificationMasterCtrl implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Home", "/mms/home.htm"));
		links.add(new Link("Specification Master", ""));
		
		List<SpecificationMaster> specLst = SpecificationMaster.getAll();
		String[] nameAttrLst = SpecificationMaster.NAME_ATTRIBUTE;
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("links", links);
		model.put("specLst", specLst);
		model.put("nameAttrLst", nameAttrLst);
		
		
		return new ModelAndView("specification_master", "model", model);
	}
}
