package mms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mms.domain.Link;
import mms.domain.ManufacturingPlan;
import mms.domain.ProductProcess;
import mms.domain.Production;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ProductionResultsCtrl implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//User user = RedirectCtrl.send(request, response);
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Home", "/mms/home.htm"));
		links.add(new Link("Manufacturing Results", "/mms/manufacturing_results.htm"));
		links.add(new Link("Production Results", ""));
		
		String mpId = request.getParameter("mpId");
		int iMpId = Integer.valueOf(mpId);
		ManufacturingPlan manufacturingPlan = ManufacturingPlan.get(iMpId);
		List<ProductProcess> pdtPrcLst = ProductProcess.getByMpId(iMpId);
		int ppId = 0;
		if(pdtPrcLst != null) {
			for(ProductProcess pdtPrc: pdtPrcLst) {
				ppId = pdtPrc.getPpId();
				if(pdtPrc.getFinishActual() == null)
					break;
			}
		}
		
		List<Production> productionLst = Production.getByPpId(ppId);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("links", links);
		model.put("manufacturingPlan", manufacturingPlan);
		model.put("pdtPrcLst", pdtPrcLst);
		model.put("productionLst", productionLst);
		model.put("ppId", ppId);
		
		return new ModelAndView("production_results", "model", model);
	}
}
