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


@Path("/htProductionWs")
public class HtProductionWs {
	@GET
	@Path("/start/{mpId}/{barcode}/{prcId}")
	public String getTrigger(@PathParam("mpId") String mpId, @PathParam("barcode") String barcode, @PathParam("prcId") int prcId) {
		Production production = Production.getByMpIdBarcode(mpId, barcode);
		System.out.println("+ " + production);
		StringBuffer strBuf = new StringBuffer("<items>");
		if(production == null) {
			ProductProcess pp = ProductProcess.getByFirstProcess(mpId);
			
			int ppId = pp.getPpId();
			int planQty = pp.getPlanQty();
			int count = Integer.valueOf(Production.getCount(ppId));
			
			
			if(count >= planQty) {
				strBuf.append("<item name='msg' value='The number of units in process " + pp.getPrcName() + " is equal to quantity plan.' />");
				strBuf.append("</items>");
				
				return strBuf.toString();
			}
			else {
				Date now = new Date();
				
				production = new Production();
				production.setPpId(Integer.valueOf(ppId));
				String serialNo = Production.getSn(mpId, barcode);
				if(!"null".equals(serialNo))
					production.setSerialNo(serialNo);
				production.setBarcode(barcode);
				production.setStartActual(now);
				production.setPdtId(production.store());
				
				pp.setStartActual(now);
				pp.update();
				
				production.setFinishActual(now);
				production.setResult(-1);
			}
		}
		else if(production.getFinishActual() != null) {
			int ppId = production.getPpId();
			ProductProcess pp = ProductProcess.get(ppId);
			
			int nextPrcId = pp.getPrcId() + 1;
			if(prcId == nextPrcId) {
				pp = ProductProcess.getByMpIdPrcId(mpId, String.valueOf(nextPrcId));
				if(pp == null) {
					strBuf.append("<item name='msg' value='This product finished all of processes.' />");
					strBuf.append("</items>");
					return strBuf.toString();
				}
				else if("Back Coating".equals(pp.getPrcName()) && production.getSerialNo() == null) {
					strBuf.append("<item name='msg' value='Please fill \'Serial No.\' for barcode " + barcode + " in 'Plating' process and then you can start to next process.' />");
					strBuf.append("</items>");
					return strBuf.toString();
				}
				else {
					ppId = pp.getPpId();
					int planQty = pp.getPlanQty();
					int count = Integer.valueOf(Production.getCount(ppId));
					
					if(count >= planQty) {
						strBuf.append("<item name='msg' value='The number of units in process " + pp.getPrcName() + " is equal to quantity plan.' />");
						strBuf.append("</items>");
						return strBuf.toString();
					}
					else {
						Date now = new Date();
						Production nproduction = new Production();
						nproduction.setPpId(Integer.valueOf(ppId));
						nproduction.setSerialNo(production.getSerialNo());
						nproduction.setBarcode(production.getBarcode());
						nproduction.setStartActual(now);
						nproduction.setPdtId(nproduction.store());
						
						pp.setStartActual(now);
						pp.update();
						
						nproduction.setFinishActual(now);
						nproduction.setResult(-1);
						
						production = nproduction;
					}
				}
			}
		}
		strBuf.append("</items>");
		return strBuf.toString();
	}
	
	@GET
	@Path("/finish/{mpId}/{barcode}/{result}")
	public String result(@PathParam("mpId") String mpId, @PathParam("barcode") String barcode, @PathParam("result") String result) {
		Production production = Production.getByMpIdBarcode(mpId, barcode);
		if(production != null && production.getFinishActual() == null) {
			int scatId = 0;
			List<Subcategory> scatLst = Subcategory.getListByName(Category.PROCESS_RESULT);
			for(Subcategory scat : scatLst) {
				if(scat.getName().equals(result)) {
					scatId = scat.getScatId();
					break;
				}
			}
			Date now = new Date();
			production.setFinishActual(now);
			production.setResult(scatId);
			
			if(production.update()) {
				int resultCount = 0;
				int defectiveCount = 0;
				//int noResult = 0;
	
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
						//else noResult += count;
					}
				}
				ProductProcess pp = ProductProcess.get(production.getPpId());
				pp.setResultQty(resultCount);
				pp.setDefectiveQty(defectiveCount);
				
				if(pp.getPlanQty() == resultCount + defectiveCount)
					pp.setFinishActual(now);
				pp.update();
			}
		}
		return production.toString();
	}
}
