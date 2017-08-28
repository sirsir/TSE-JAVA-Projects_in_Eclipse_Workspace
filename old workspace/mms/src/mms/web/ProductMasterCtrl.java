package mms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mms.domain.Link;
import mms.domain.ProductMaster;
import mms.domain.SpecificationMaster;
import mms.domain.Subcategory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ProductMasterCtrl implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Home", "/mms/home.htm"));
		links.add(new Link("Product Master", ""));
		model.put("links", links);
		List<Subcategory> scatLst = Subcategory.getAll();
		List<ProductMaster> productLst = ProductMaster.getAll();
		model.put("scatLst", scatLst);
		model.put("productLst", productLst);
		List<SpecificationMaster> specLst = SpecificationMaster.getAll();
		model.put("specLst",specLst);
		if(productLst != null){
			int productId = productLst.get(0).getPrdId();
			model.put("productId", productId);
		}

		
		return new ModelAndView("product_master", "model", model);
	}
}
