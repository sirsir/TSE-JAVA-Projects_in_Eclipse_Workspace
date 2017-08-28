package mms.ws;


import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import mms.domain.UserMaster;

@Path("/usermasterWs")
public class UserMaterWs {
	@PUT
	public String store(@FormParam("name") String name, @FormParam("password") String password, @FormParam("roId") String roId,
			@FormParam("code") String code) {
		UserMaster userMaster = new UserMaster();
		userMaster.setName(name);
		userMaster.setPassword(password);
		userMaster.setRoId(Integer.valueOf(roId));
		userMaster.setCode(code);
		int result = userMaster.store();
		if(result == 0)
			return userMaster.getMessage();
		else return String.valueOf(result);
	}
	
	@POST
	public String update(@FormParam("usrId") String usrId, @FormParam("name") String name, @FormParam("password") String password, @FormParam("roId") String roId,
			@FormParam("code") String code) {
		
		UserMaster userMaster = UserMaster.get(Integer.valueOf(usrId));
		userMaster.setName(name);
		userMaster.setPassword(password);
		userMaster.setRoId(Integer.valueOf(roId));
		userMaster.setCode(code);
		return String.valueOf(userMaster.update());
	}
	
	@DELETE
	public String delete(@FormParam("usrId") String usrId) {
		if(UserMaster.delete(usrId))
			return "true";
		else return "false";
	}
}
