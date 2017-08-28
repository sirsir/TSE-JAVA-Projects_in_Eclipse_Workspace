package mms.ws;


import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import mms.db.SQLCommand;
import mms.domain.Process;
import mms.domain.ProductMaster;
import mms.domain.ProductProcessSpec;

@Path("/productmasterWs")
public class ProductMasterWs {
	@PUT
	public String store(@FormParam("name") String name, @FormParam("code") String code,
			@FormParam("scatId") String scatId) {
		StringBuffer buf = new StringBuffer();
		ProductMaster productmaster= new ProductMaster();
		productmaster.setName(name);
		productmaster.setCode(code);
		productmaster.setScatId(Integer.valueOf(scatId));
		int result = productmaster.store();
		if(result == 0)
			return productmaster.getMessage();
		else 
		{	buf.append(result + "/n");
			boolean success = true;
            SQLCommand cmd = new SQLCommand();
            cmd.executeUpdate("begin");
            List<Process> processLst =Process.getAll();
            for(Process process : processLst) {
                  if(!ProductProcessSpec.store(cmd, result, process.getPrcId())) {
                         success = false;
                         break;
                  }
                  ProductProcessSpec p = ProductProcessSpec.getQuery(result,process.getPrcId());
                  buf.append(p + "/n");
            }
            if(success)
                  cmd.executeUpdate("commit");
            else cmd.executeUpdate("rollback");
            
            return buf.toString();
		}	
			
	}
	@POST
	@Path("/copyProduct")
	public String copyProduct(@FormParam("prdId") String prdId, @FormParam("copyCodePrd") String copyCodePrd,
			@FormParam("copyNamePrd") String copyNamePrd) {
		StringBuffer buf = new StringBuffer();
		ProductMaster p1 = ProductMaster.get(Integer.valueOf(prdId));
		ProductMaster p2= new ProductMaster();
		p2.setName(copyNamePrd);
		p2.setCode(copyCodePrd);
		p2.setScatId(p1.getScatId());
		int result = p2.store();
		if(result == 0)
			return p2.getMessage();
		else 
		{	ProductMaster tmpP = ProductMaster.getQuery(result);
			buf.append(tmpP + "/n");
			boolean success = true;
            SQLCommand cmd = new SQLCommand();
            cmd.executeUpdate("begin");
            List<Process> processLst =Process.getAll();
            for(Process process : processLst) {
            		ProductProcessSpec ps1 = ProductProcessSpec.get(Integer.valueOf(prdId),process.getPrcId());
            		ProductProcessSpec newps = new ProductProcessSpec();
            		newps.setPrdId(result);
            		newps.setPrcId(process.getPrcId());
            		newps.setContent(ps1.getContent());
            		newps.setStandardTime(ps1.getStandardTime());
            		newps.setManPower(ps1.getManPower());
            		newps.setUnitCount(ps1.getUnitCount());
            		newps.setPrintLabel(ps1.getPrintLabel());
            		newps.setUnitSize(ps1.getUnitSize());
            		newps.setSpecId(ps1.getSpecId());
            		if(newps.store(cmd) == 0) {
                         success = false;
                         break;
                  }
                 ProductProcessSpec ps2 = ProductProcessSpec.getQuery(result,process.getPrcId());
                  buf.append(ps2 + "/n");
            }
            if(success)
                  cmd.executeUpdate("commit");
            else cmd.executeUpdate("rollback");
		}
            return buf.toString();			
	}
	@PUT
	@Path("/process")
	public String get(@FormParam("prdId") String prdId) {
		StringBuffer buf = new StringBuffer();
		buf.append(prdId + "/n");
        List<Process> processLst =Process.getAll();
        for(Process process : processLst) {
            ProductProcessSpec p = ProductProcessSpec.getQuery(Integer.valueOf(prdId),process.getPrcId());
            buf.append(p + "/n");
         }
        return buf.toString();	
	}
	
	
	@POST
	@Path("/product")
	public String update(@FormParam("prdId") String prdId, @FormParam("name") String name, @FormParam("code") String code,
			@FormParam("scatId") String scatId) {
		ProductMaster productmaster = ProductMaster.get(Integer.valueOf(prdId));
		productmaster.setName(name);
		productmaster.setCode(code);
		productmaster.setScatId(Integer.valueOf(scatId));
		
		return String.valueOf(productmaster.update());
	}
	
	@POST
	@Path("/process")
	public String updateProcess(@FormParam("prdId") String prdId, @FormParam("prcId") String prcId, @FormParam("prcContent") String prcContent,
			@FormParam("standardTime") String standardTime, @FormParam("manPower") String manPower, @FormParam("unitCount") String unitCount, 
			@FormParam("printLabel") String printLabel,@FormParam("unitSize") String unitSize,@FormParam("specId") String specId) {
		ProductProcessSpec p = ProductProcessSpec.get(Integer.valueOf(prdId),Integer.valueOf(prcId));
		 p.setContent(prcContent);
		 p.setStandardTime(Integer.valueOf(standardTime));
		 p.setManPower(Integer.valueOf(manPower));
		 p.setUnitCount(Integer.valueOf(unitCount));
		 p.setPrintLabel(Integer.valueOf(printLabel));
		 p.setUnitSize(Integer.valueOf(unitSize));
		 if(!specId.equals(""))
			 p.setSpecId(Integer.valueOf(specId));
		
		return String.valueOf(p.update());
	}
	@DELETE
	@Path("/product")
	public String delete(@FormParam("prdId") String prdId) {
		if(ProductMaster.delete(prdId))
			return "true";
		else return "false";
	}
}
