package mms.ws;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import mms.domain.ManufacturingPlan;
import mms.domain.Process;
import mms.domain.ProductMaster;
import mms.domain.ProductProcess;
import mms.domain.Production;
import mms.domain.UserAuthProcess;

@Path("/processWs")
public class ProcessWs {
	@GET
	@Produces(MediaType.TEXT_XML)
	public String get(@QueryParam("type") String type, @QueryParam("uId") String uid ,@QueryParam("prcId") int prcId, @QueryParam("barcode") String barcode,@QueryParam("mpId") String mpId) {
		StringBuffer strBuf = new StringBuffer("<?xml version=\"1.0\"?>");
		if("list".equals(type)) {
			strBuf.append("<items>");
			List<UserAuthProcess> processLst = UserAuthProcess.getByUserId(Integer.valueOf(uid));
			for(UserAuthProcess process : processLst) {
				Process p = Process.get(process.getPrcId());
				strBuf.append("<item name='" + p.getName() + "' value='" + process.getPrcId() + "' type='process' />");
			}
			strBuf.append("</items>");
			return strBuf.toString();
		}
		else if("checkbarcode".equals(type)) {
			strBuf.append("<items>");
			Production p = Production.getByMpIdBarcode(mpId, barcode);
			if(p == null){
				strBuf.append("<item no='BarCode or Serial No is mistake' name='' started=''/>");
				strBuf.append("</items>");
				return strBuf.toString();
			}
			ProductProcess pp = ProductProcess.get(p.getPpId());
			int currPrcId = pp.getPrcId();
			System.out.println("/ " + currPrcId + ":" + pp.getPrcId() + "\t" + pp);
			if(currPrcId == prcId) {
				if(p.getFinishActual() != null)
					strBuf.append("<item no='The unit was done.' name='' started=''/>");
				else {
					ManufacturingPlan mp = ManufacturingPlan.get(pp.getMpId());
					ProductMaster pm = ProductMaster.get(mp.getPrdId());
					strBuf.append("<item no='" + mp.getNo() + "' name='" + pm.getName()+ "' serialNo='"+p.getSerialNo()+"' started='" + (p.getFinishActual()==null?"true":"false") + "'/>");
				}
			}
			else if((currPrcId+1) == prcId && p.getFinishActual() != null) {
				ManufacturingPlan mp = ManufacturingPlan.get(pp.getMpId());
				ProductMaster pm = ProductMaster.get(mp.getPrdId());
				strBuf.append("<item no='" + mp.getNo() + "' name='" + pm.getName()+ "' serialNo='"+p.getSerialNo()+"' started='false'/>");
			}
			else {
				if(p.getFinishActual() == null)
					strBuf.append("<item no='The unit is running in " + pp.getPrcName() + " process.' name='' started=''/>");
				else strBuf.append("<item no='The unit was done in " + pp.getPrcName() + " process.' name='' started=''/>");
			}
			strBuf.append("</items>");
			return strBuf.toString();
		}
		else return "<?xml version=\"1.0\"?>";
	}
}
