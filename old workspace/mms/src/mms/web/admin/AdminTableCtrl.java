package mms.web.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import mms.util.StringUtil;
//import mms.domain.User;
import mms.domain.admin.TableMapping;
import mms.domain.Link;
//import mms.web.RedirectCtrl;

public class AdminTableCtrl implements Controller {
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//RedirectCtrl.send(request, response);
		
		String table = request.getParameter("table");
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Home", "home.htm"));
		links.add(new Link(StringUtil.capitalize(table), ""));
		model.put("links", links);
		
		
		List<TableMapping> tableField = TableMapping.getTableField(table);
		List<TableMapping> tableData = TableMapping.getAll(table);
		
		model.put("tableField", tableField);
		model.put("tableData",tableData);
		model.put("table", table);
		
		return new ModelAndView("/admin/admin_table","model", model);
	}
}
