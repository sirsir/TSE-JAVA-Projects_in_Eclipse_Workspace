package mms.web.admin;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import mms.db.SQLCommand;
import mms.domain.admin.FormMapping;

public class AdminFormCtrl implements Controller{
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//RedirectCtrl.send(request, response);
		
		String submit =request.getParameter("submit");
		String table = request.getParameter("table");
		
		if(submit == null){		
			String id = request.getParameter("id");
			
			List<FormMapping> formMap = null;
			if(id == null){
				 formMap = FormMapping.getField(table);
			} else {
				formMap = FormMapping.getField(table, id);
			}
			
			Map<String, Object> modelForm = new HashMap<String, Object>();
			modelForm.put("formMap", formMap);
			modelForm.put("table", table);
			modelForm.put("id", id);
			
			return new ModelAndView("/admin/admin_form","modelForm",modelForm);
		} else if("Add".equals(submit)) { 
			StringBuffer sqlQuery = new StringBuffer();
			Enumeration<String> e = request.getParameterNames();
			
			sqlQuery.append("insert into "+table+" (");
			
			StringBuffer fieldName = new StringBuffer();
			StringBuffer fieldValue = new StringBuffer();
			String prefix="";
			for(; e.hasMoreElements();) {
				String name = e.nextElement();
				String value = request.getParameter(name);
				if(!request.getParameter("pk").equals(name) && !("pk").equals(name) && !("submit").equals(name) &&!("table").equals(name)){
					if(("coId").equals(name) && ("user").equals(table) || "password".equals(name)){
						if("password".equals(name)){
						fieldName.append(prefix);
						fieldValue.append(prefix);
						fieldName.append(name);
						fieldValue.append("sha2('"+value+"',224)");
						prefix = ",";
						
						}
					} else {
						fieldName.append(prefix);
						fieldValue.append(prefix);
						prefix =",";
						fieldName.append(name);
						fieldValue.append("'"+value+"'");
					}
					
				}		
			}
			sqlQuery.append(fieldName+") values("+fieldValue+");");
			new SQLCommand().executeUpdate(sqlQuery.toString());
			
			return new ModelAndView("redirect:/admin_table.htm?table="+table);
		} else {
			StringBuffer sqlQuery = new StringBuffer();
			Enumeration<String> e = request.getParameterNames();
			
			sqlQuery.append("update "+table+" set ");
			
			StringBuffer fieldUpdate = new StringBuffer();
			String prefix="";
			for(; e.hasMoreElements();) {
				String name = e.nextElement();
				String value = request.getParameter(name);
				if(!request.getParameter("pk").equals(name) && !("pk").equals(name) && !("submit").equals(name) &&!("table").equals(name)){
					if(("coId").equals(name) && ("user").equals(table) || "password".equals(name)){
						if("password".equals(name)){
							fieldUpdate.append(prefix);
							fieldUpdate.append(name+"=sha2('"+value+"',224)");
							prefix = ",";
						}
					} else {
						fieldUpdate.append(prefix);
						prefix =",";
						fieldUpdate.append(name+"='"+value+"'");
					}
					
				}		
			}
			sqlQuery.append(fieldUpdate+" where "+request.getParameter("pk")+" = "+request.getParameter(request.getParameter("pk"))+";");
			
			new SQLCommand().executeUpdate(sqlQuery.toString());
			
			return new ModelAndView("redirect:/admin_table.htm?table="+table);
		}
	}
}
