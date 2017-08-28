package mms.ws;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import mms.domain.UserAuthProcess;
import mms.domain.UserMaster;
import mms.domain.Process;

@Path("/loginWs")
public class LoginWs {
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public String get(@QueryParam("type") String type, @QueryParam("id") String id, @QueryParam("password") String password) {
		StringBuffer strBuf = new StringBuffer("<?xml version=\"1.0\"?>");
		if("list".equals(type)) {
			strBuf.append("<items>");
			List<UserMaster> userLst = UserMaster.getAll();
			for(UserMaster user : userLst) {
				strBuf.append("<item name='" + user.getName() + "' value='" + user.getUsrId() + "'/>");
			}
			strBuf.append("</items>");
			return strBuf.toString();
		}
		else if("login".equals(type) ) {
			UserMaster user = UserMaster.getUserByPassword(Integer.valueOf(id),password);
			if(user == null){
				strBuf.append("<items>");
				strBuf.append("<item name='empty'/>");
				strBuf.append("</items>");
				return strBuf.toString();
			}
			strBuf.append("<items>");
			String shortName = user.getName();
			String[] s = shortName.split(" ");
			shortName = s[0] + s[1].substring(0, 1);
			strBuf.append("<item name='" + shortName + "' value='" + user.getUsrId() + "' type='user' />");
			List<UserAuthProcess> processLst = UserAuthProcess.getByUserId(user.getUsrId());
			for(UserAuthProcess process : processLst) {
				Process p = Process.get(process.getPrcId());
				strBuf.append("<item name='" + p.getName() + "' value='" + process.getPrcId() + "' type='process' />");
			}

			strBuf.append("</items>");
			return strBuf.toString();
		}
		else return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}
}
