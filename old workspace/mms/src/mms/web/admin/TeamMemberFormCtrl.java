package mms.web.admin;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.object.SqlCall;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import mms.db.SQLCommand;
//import mms.domain.Team;
//import mms.domain.TeamMember;

public class TeamMemberFormCtrl implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String submit =request.getParameter("submit");
		String id = request.getParameter("tmId");
		
		
		if("Add".equals(submit)){
			String uId = request.getParameter("uId");
			String tId = request.getParameter("tId");
			String sql = "insert into team_member (tId,uId) values('"+tId+"','"+uId+"');";
			new SQLCommand().executeUpdate(sql);
			return new ModelAndView("redirect:/admin/admin_table.htm?table=team_member");
		} else if("Update".equals(submit)){
			String uId = request.getParameter("uId");
			if(uId.equals(""))
				uId = request.getParameter("_uId");
			String tId = request.getParameter("tId");
			String exp = request.getParameter("expired");
			String sql = "update team_member set tId ='"+tId+"', uId ='"+uId+"', expired ='"+exp+"' where tmId = "+id+";";
			new SQLCommand().executeUpdate(sql);
			return new ModelAndView("redirect:/admin/admin_table.htm?table=team_member");
		} else {
			/*TeamMember teamMember = null;
			String userName = null;
			List<Team> team = Team.getAll();
			if(id != null){
				SQLCommand cmd = new SQLCommand();
				teamMember = TeamMember.get(Integer.valueOf(id));
				userName = cmd.executeQuery("select concat(fname,'  ',sname) from user where uId=#!-",String.valueOf(teamMember.getUId()));
			} else {
				id = "0";
			}
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("teamMember", teamMember);
			model.put("team", team);
			model.put("id", id);
			model.put("userName", userName);
			
			return new ModelAndView("/admin/team_member_form","model",model); */
			return new ModelAndView("/admin/team_member_form");
		}
	}
}
