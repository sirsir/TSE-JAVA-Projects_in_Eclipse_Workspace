package mms.ws;


import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import mms.domain.Category;
import mms.domain.ProductProcess;
import mms.domain.Production;
import mms.domain.Subcategory;
import mms.util.Date;


@Path("/productionWs")
public class ProductionWs {
	@GET
	public String get(@QueryParam("ppId") String ppId) {
		StringBuffer strBuf = new StringBuffer();
		List<Production> productionLst = Production.getByPpId(Integer.valueOf(ppId));
		if(productionLst == null)
			return "";
		for(Production production : productionLst) {
			strBuf.append(production + "\n");
		}
		return strBuf.toString().trim();
	}
	
	@GET
	@Path("/refresh")
	public String refresh(@QueryParam("mpId") int mpId) {
		StringBuffer strBuf = new StringBuffer();
		List<ProductProcess> ppLst = ProductProcess.getByMpId(mpId);
		if(ppLst != null) {
			for(ProductProcess pp: ppLst) {
				strBuf.append(pp + "\n");
			}
		}
		return strBuf.toString().trim();
	}
	
	@GET
	@Path("/trigger/{mpId}/{barcode}")
	public String getTrigger(@PathParam("mpId") String mpId, @PathParam("barcode") String barcode) {
		Production production = Production.getByMpIdBarcode(mpId, barcode);
		if(production == null) {
			ProductProcess pp = ProductProcess.getByFirstProcess(mpId);
			
			int ppId = pp.getPpId();
			int planQty = pp.getPlanQty();
			int count = Integer.valueOf(Production.getCount(ppId));
			if(count >= planQty)
				return "The number of units in process " + pp.getPrcName() + " is equal to quantity plan.";
			else {
				production = new Production();
				production.setPpId(Integer.valueOf(ppId));
				String serialNo = Production.getSn(mpId, barcode);
				if(!"null".equals(serialNo))
					production.setSerialNo(serialNo);
				production.setBarcode(barcode);
				production.setPdtId(production.store());
				
				Date now = new Date();
				pp.setStartActual(now);
				pp.update();
				
				production.setFinishActual(now);
				production.setResult(-1);
			}
		}
		else if(production.getFinishActual() != null) {
			int ppId = production.getPpId();
			ProductProcess pp = ProductProcess.get(ppId);
			
			if("Plating".equals(pp.getPrcName()) && production.getSerialNo() == null)
				return "Please fill \'Serial No.\' for barcode " + barcode + " in 'Plating' process and then you can start to next process.";
			
			int prcId = pp.getPrcId() + 1;
			pp = ProductProcess.getByMpIdPrcId(mpId, String.valueOf(prcId));
			if(pp == null)
				return "This product finished all of processes";
			else {
				ppId = pp.getPpId();
				int planQty = pp.getPlanQty();
				int count = Integer.valueOf(Production.getCount(ppId));
				
				if(count >= planQty)
					return "The number of units in process " + pp.getPrcName() + " is equal to quantity plan.";
				else {
					Production nproduction = new Production();
					nproduction.setPpId(Integer.valueOf(ppId));
					nproduction.setSerialNo(production.getSerialNo());
					nproduction.setBarcode(production.getBarcode());
					nproduction.setPdtId(nproduction.store());
					
					Date now = new Date();
					pp.setStartActual(now);
					pp.update();
					
					nproduction.setFinishActual(now);
					nproduction.setResult(-1);
					
					production = nproduction;
				}
			}
		}
		return production.toString();
	}
	
	@POST
	@Path("/trigger/autoStart/{pdtId}")
	public String autoStart(@PathParam("pdtId") int pdtId) {
		Date now = new Date();
		Production production = Production.get(pdtId);
		production.setStartActual(now);
		if(production.update())
			return String.valueOf(now.getFullDateTimeString());
		else return "";
	}
	
	@POST
	@Path("/trigger/autoFinish/{pdtId}")
	public String autoFinish(@PathParam("pdtId") int pdtId) {
		Date now = new Date();
		Production production = Production.get(pdtId);
		ProductProcess pp = ProductProcess.get(production.getPpId());
		pp.setFinishActual(now);
		if(pp.update())
			return pp.getPpId() + "\t" + String.valueOf(now.getFullDateTimeString());
		else return "";
	}
	
	@POST
	@Path("/trigger/finishResult/{pdtId}/{result}")
	public String finishResult(@PathParam("pdtId") int pdtId, @PathParam("result") String result) {
		String scatId = Subcategory.getIdByName(Category.PROCESS_RESULT, result);
		Date now = new Date();
		Production production = Production.get(pdtId);
		production.setFinishActual(now);
		production.setResult(Integer.valueOf(scatId));
		if(production.update()) {
			int resultCount = 0;
			int defectiveCount = 0;
			int noResult = 0;

			Map<Integer, Integer> resultCntMap = Production.getResultCount(production.getPpId());
			List<Subcategory> resultLst = Subcategory.getListByName(Category.PROCESS_RESULT);
			for(Subcategory scat : resultLst) {
				Integer count = resultCntMap.get(scat.getScatId());
				String resultName = scat.getName();
				if(count != null) {
					if("OK".equals(resultName) || "NG".equals(resultName))
						resultCount += count;
					else if("REJECT".equals(resultName)) {
						//resultCount += count;
						defectiveCount += count;
					}
					else noResult += count;
				}
			}
			ProductProcess pp = ProductProcess.get(production.getPpId());
			pp.setResultQty(resultCount);
			pp.setDefectiveQty(defectiveCount);
			
			if(pp.getPlanQty() == resultCount + defectiveCount)
				pp.setFinishActual(now);
			pp.update();
			
			return String.valueOf(now.getFullDateTimeString() + "\t" + result + "\t" + production.getPpId()  + "\t" + resultCount + "\t" + defectiveCount + "\t" + noResult);
		}
		else return "";
	}
	
	@POST
	@Path("/start/{ppId}")
	public String start(@PathParam("ppId") int ppId) {
		Date now = new Date();
		ProductProcess pp = ProductProcess.get(ppId);
		pp.setStartActual(now);
		if(pp.update())
			return String.valueOf(now.getFullDateTimeString());
		else return "";
	}
	
	@POST
	@Path("/finish/{ppId}")
	public String finish(@PathParam("ppId") int ppId) {
		Date now = new Date();
		ProductProcess pp = ProductProcess.get(ppId);
		pp.setFinishActual(now);
		if(pp.update())
			return String.valueOf(now.getFullDateTimeString());
		else return "";
	}
	
	@POST
	@Path("/sn/{pdtId}/{sn}")
	public String setSn(@PathParam("pdtId") int pdtId, @PathParam("sn") String sn) {
		Production production = Production.get(pdtId);
		production.setSerialNo(sn);
		if(production.update())
			return sn;
		else return "";
	}
}
