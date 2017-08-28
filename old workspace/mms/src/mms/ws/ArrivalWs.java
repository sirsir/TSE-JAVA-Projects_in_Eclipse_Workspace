package mms.ws;


import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import mms.domain.ArrivalPlan;
import mms.util.Date;


@Path("/arrivalWs")
public class ArrivalWs {
	@PUT
	public String store(@FormParam("date") String date, @FormParam("no") String no, @FormParam("prdId") String prdId,
			@FormParam("planQty") String planQty, @FormParam("arrivalQty") String arrivalQty) {
		ArrivalPlan arrival = new ArrivalPlan();
		arrival.setArrDate(new Date(date));
		arrival.setNo(no);
		arrival.setPrdId(Integer.valueOf(prdId));
		arrival.setPlanQty(Integer.valueOf(planQty));
		try {arrival.setArrQty(Integer.valueOf(arrivalQty));} catch(NumberFormatException e) {};
		
		int result = arrival.store();
		if(result == 0)
			return arrival.getMessage();
		else return String.valueOf(result);
	}
	
	@POST
	public String update(@FormParam("arrId") String arrId, @FormParam("date") String date, @FormParam("prdId") String prdId,
			@FormParam("planQty") String planQty, @FormParam("arrivalQty") String arrivalQty) {
		ArrivalPlan arrival = ArrivalPlan.get(Integer.valueOf(arrId));
		arrival.setArrDate(new Date(date));
		arrival.setPrdId(Integer.valueOf(prdId));
		arrival.setPlanQty(Integer.valueOf(planQty));
		try {arrival.setArrQty(Integer.valueOf(arrivalQty));} catch(NumberFormatException e) {}
		
		return String.valueOf(arrival.update());
	}
	
	@DELETE
	public String delete(@FormParam("arrId") String arrId) {
		if(ArrivalPlan.delete(arrId))
			return "true";
		else return "false";
	}
}
