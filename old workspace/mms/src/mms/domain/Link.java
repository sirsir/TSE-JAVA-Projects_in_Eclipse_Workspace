package mms.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Link implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String url;
	
	public String toString() {
		return name + "\t" + url;
	}
	public Link(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public static List<Link> get(int sgId) {
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("Home", "/mms/wf/home.htm"));
		
		/*
		links.add(new Link("Workflow", "/mms/wf/workflow/workflow.htm"));
		
		List<TraceNode> traceNodes = new ArrayList<TraceNode>();
		StageTrace.setTraceNode(traceNodes, StageTrace.STAGE_TYPE, String.valueOf(sgId), new SQLCommand());
		int size = traceNodes.size();
		for(int i=size; --i>=0;) {
			TraceNode pnode = traceNodes.get(i);
			TraceNode cnode = traceNodes.get(--i);
			links.add(new Link(pnode.getName() + " - " + cnode.getName(), "/mms/wf/workflow/stage.htm?sgId=" + cnode.getId()));
		}
		*/
		return links;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
