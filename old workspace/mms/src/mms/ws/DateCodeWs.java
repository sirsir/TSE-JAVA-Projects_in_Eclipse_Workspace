package mms.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import mms.domain.ArrivalPlan;
import mms.domain.ManufacturingPlan;
import mms.domain.ShippingPlan;

@Path("/dateCodeWs")
public class DateCodeWs {
	@GET
	public String getHtml(@QueryParam("type") String type, @QueryParam("dcode") String dcode) {
		String dateCode = "00000000";
		if("arrival".equals(type))
			dateCode = ArrivalPlan.getDateCode(dcode);
		else if("shipping".equals(type))
			dateCode = ShippingPlan.getDateCode(dcode);
		else if("manufacturing".equals(type))
			dateCode = ManufacturingPlan.getDateCode(dcode);
		return "<?xml version=\"1.0\"?><items><dateCode id='1'>" + dateCode + "</dateCode></items>";
	}
}
