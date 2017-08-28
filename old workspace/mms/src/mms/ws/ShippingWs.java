package mms.ws;


import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import mms.domain.ArrivalPlan;
import mms.domain.ShippingPlan;
import mms.util.Date;


@Path("/shippingWs")
public class ShippingWs {
	@PUT
	public String store(@FormParam("date") String date, @FormParam("no") String no, @FormParam("prdId") String prdId,
			@FormParam("planQty") String planQty, @FormParam("shipQty") String shipQty) {
		ShippingPlan shipping = new ShippingPlan();
		shipping.setShpDate(new Date(date));
		shipping.setNo(no);
		shipping.setPrdId(Integer.valueOf(prdId));
		shipping.setPlanQty(Integer.valueOf(planQty));
		try {shipping.setShipQty(Integer.valueOf(shipQty));} catch(NumberFormatException e) {};
		
		int result = shipping.store();
		if(result == 0)
			return shipping.getMessage();
		else return String.valueOf(result);
	}
	
	@POST
	public String update(@FormParam("shpId") String shpId, @FormParam("date") String date, @FormParam("prdId") String prdId,
			@FormParam("planQty") String planQty, @FormParam("shipQty") String shipQty) {
		ShippingPlan shipping = ShippingPlan.get(Integer.valueOf(shpId));
		shipping.setShpDate(new Date(date));
		shipping.setPrdId(Integer.valueOf(prdId));
		shipping.setPlanQty(Integer.valueOf(planQty));
		try {shipping.setShipQty(Integer.valueOf(shipQty));} catch(NumberFormatException e) {}
		
		return String.valueOf(shipping.update());
	}
	
	@DELETE
	public String delete(@FormParam("shpId") String shpId) {
		if(ShippingPlan.delete(shpId))
			return "true";
		else return "false";
	}
}
