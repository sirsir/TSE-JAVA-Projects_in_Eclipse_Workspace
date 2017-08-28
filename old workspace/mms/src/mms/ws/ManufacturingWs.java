package mms.ws;


import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import mms.db.SQLCommand;
import mms.domain.Category;
import mms.domain.ManufacturingPlan;
import mms.domain.Process;
import mms.domain.ProductProcess;
import mms.domain.Subcategory;
import mms.domain.UserMaster;
import mms.util.Date;


@Path("/manufacturingWs")
public class ManufacturingWs {
	@GET
	@Produces(MediaType.TEXT_XML)
	public String get(@QueryParam("prcId") String prcId) {
		StringBuffer strBuf = new StringBuffer("<?xml version=\"1.0\"?>");
		List<ManufacturingPlan> manuLst = ManufacturingPlan.getRunning();
		strBuf.append("<items>");
		for(ManufacturingPlan manu : manuLst) {
			strBuf.append("<item name='" + manu.getNo() + "' value='" + manu.getMpId() + "'/>");
		}
		strBuf.append("</items>");
		return strBuf.toString();
	}
	
	@PUT
	public String store(@FormParam("date") String date, @FormParam("no") String no, @FormParam("prdId") String prdId,
			@FormParam("planQty") String planQty, @FormParam("startPlan") String startPlan, @FormParam("finishPlan") String finishPlan) {
		ManufacturingPlan manu = new ManufacturingPlan();
		manu.setMpDate(new Date(date));
		manu.setNo(no);
		manu.setPrdId(Integer.valueOf(prdId));
		manu.setQty(Integer.valueOf(planQty));
		manu.setStartPlan(new Date(startPlan));
		manu.setFinishPlan(new Date(finishPlan));
		
		int result = manu.store();
		if(result == 0)
			return manu.getMessage();
		else return String.valueOf(result);
	}
	
	@POST
	public String update(@FormParam("mpId") String mpId, @FormParam("date") String date, @FormParam("prdId") String prdId,
			@FormParam("planQty") String planQty, @FormParam("startPlan") String startPlan, @FormParam("finishPlan") String finishPlan) {
		ManufacturingPlan manu = ManufacturingPlan.get(Integer.valueOf(mpId));
		manu.setMpDate(new Date(date));
		manu.setPrdId(Integer.valueOf(prdId));
		manu.setQty(Integer.valueOf(planQty));
		manu.setStartPlan(new Date(startPlan));
		manu.setFinishPlan(new Date(finishPlan));
		
		return String.valueOf(manu.update());
	}
	
	@POST
	@Path("/start/{mpId}")
	public String start(@PathParam("mpId") int mpId) {
		Date now = new Date();
		String status = Subcategory.getIdByName(Category.MANUFACTURING_STATUS, ManufacturingPlan.IN_PRODUCTION);
		ManufacturingPlan manu = ManufacturingPlan.get(mpId);
		manu.setStartActual(now);
		manu.setStatus(Integer.valueOf(status));
		if(manu.update()) {
			boolean success = true;
			SQLCommand cmd = new SQLCommand();
			cmd.executeUpdate("begin");
			List<Process> processLst =Process.getAll();
			for(Process process : processLst) {
				if(!ProductProcess.store(cmd, mpId, process.getPrcId(), manu.getQty())) {
					success = false;
					break;
				}
			}
			if(success)
				cmd.executeUpdate("commit");
			else cmd.executeUpdate("rollback");
			return String.valueOf(now.getFullDateTimeString());
		}
		else return "";
	}
	
	@POST
	@Path("/restart/{mpId}")
	public String restart(@PathParam("mpId") int mpId) {
		Date now = new Date();
		String status = Subcategory.getIdByName(Category.MANUFACTURING_STATUS, ManufacturingPlan.IN_PRODUCTION);
		ManufacturingPlan manu = ManufacturingPlan.get(mpId);
		manu.setStartActual(now);
		manu.setStatus(Integer.valueOf(status));
		manu.update();
		return String.valueOf(now.getFullDateTimeString());
	}
	
	@POST
	@Path("/finish/{mpId}")
	public String finish(@PathParam("mpId") int mpId) {
		Date now = new Date();
		String status = Subcategory.getIdByName(Category.MANUFACTURING_STATUS, ManufacturingPlan.FINISHED);
		ManufacturingPlan manu = ManufacturingPlan.get(mpId);
		manu.setFinishActual(now);
		manu.setStatus(Integer.valueOf(status));
		manu.update();
		return String.valueOf(now.getFullDateTimeString());
	}
	
	@POST
	@Path("/break/{mpId}")
	public String breakMp(@PathParam("mpId") int mpId) {
		Date now = new Date();
		String status = Subcategory.getIdByName(Category.MANUFACTURING_STATUS, ManufacturingPlan.BREAK);
		ManufacturingPlan manu = ManufacturingPlan.get(mpId);
		manu.setStatus(Integer.valueOf(status));
		manu.update();
		return String.valueOf(now.getFullDateTimeString());
	}
	
	@POST
	@Path("/continue/{mpId}")
	public String continueMp(@PathParam("mpId") int mpId) {
		Date now = new Date();
		String status = Subcategory.getIdByName(Category.MANUFACTURING_STATUS, ManufacturingPlan.IN_PRODUCTION);
		ManufacturingPlan manu = ManufacturingPlan.get(mpId);
		manu.setStatus(Integer.valueOf(status));
		manu.update();
		return String.valueOf(now.getFullDateTimeString());
	}
	
	@POST
	@Path("/cancel/{mpId}")
	public String cancel(@PathParam("mpId") int mpId) {
		Date now = new Date();
		String status = Subcategory.getIdByName(Category.MANUFACTURING_STATUS, ManufacturingPlan.CANCELED);
		ManufacturingPlan manu = ManufacturingPlan.get(mpId);
		manu.setStatus(Integer.valueOf(status));
		manu.update();
		return String.valueOf(now.getFullDateTimeString());
	}
	
	@DELETE
	public String delete(@FormParam("mpId") String mpId) {
		if(ManufacturingPlan.delete(mpId))
			return "true";
		else return "false";
	}
}
