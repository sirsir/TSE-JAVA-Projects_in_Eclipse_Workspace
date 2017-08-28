package mms.ws;


import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import mms.domain.SpecificationMaster;

@Path("/specificationmasterWs")
public class SpecificationMaterWs {
	@PUT
	public String store(@FormParam("name") String name, @FormParam("attrId") String attrId, @FormParam("part0") String part0,
			@FormParam("part1") String part1,@FormParam("part2") String part2,@FormParam("part3") String part3,@FormParam("part4") String part4,
			@FormParam("part5") String part5,@FormParam("part6") String part6,@FormParam("part7") String part7,@FormParam("part8") String part8,
			@FormParam("part9") String part9) {
		SpecificationMaster specMaster = new SpecificationMaster();
		specMaster.setName(name);
		specMaster.setAttribute(Integer.valueOf(attrId));
		specMaster.setPart0(part0);
		specMaster.setPart1(part1);
		specMaster.setPart2(part2);
		specMaster.setPart3(part3);
		specMaster.setPart4(part4);
		specMaster.setPart5(part5);
		specMaster.setPart6(part6);
		specMaster.setPart7(part7);
		specMaster.setPart8(part8);
		specMaster.setPart9(part9);
		int result = specMaster.store();
		if(result == 0)
			return specMaster.getMessage();
		else return String.valueOf(result);
	}
	
	@POST
	public String update(@FormParam("specId") String specId, @FormParam("name") String name, @FormParam("attrId") String attrId, @FormParam("part0") String part0,
			@FormParam("part1") String part1,@FormParam("part2") String part2,@FormParam("part3") String part3,@FormParam("part4") String part4,
			@FormParam("part5") String part5,@FormParam("part6") String part6,@FormParam("part7") String part7,@FormParam("part8") String part8,
			@FormParam("part9") String part9) {
		SpecificationMaster specMaster = SpecificationMaster.get(Integer.valueOf(specId));
		specMaster.setName(name);
		specMaster.setAttribute(Integer.valueOf(attrId));
		specMaster.setPart0(part0);
		specMaster.setPart1(part1);
		specMaster.setPart2(part2);
		specMaster.setPart3(part3);
		specMaster.setPart4(part4);
		specMaster.setPart5(part5);
		specMaster.setPart6(part6);
		specMaster.setPart7(part7);
		specMaster.setPart8(part8);
		specMaster.setPart9(part9);
		return String.valueOf(specMaster.update());
	}
	
	@DELETE
	public String delete(@FormParam("specId") String specId) {
		if(SpecificationMaster.delete(Integer.valueOf(specId)))
			return "true";
		else return "false";
	}
}
