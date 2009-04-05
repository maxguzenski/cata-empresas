package com.eicoit.cataempresas.impl;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eicoit.cataempresas.common.Empresa;
import com.eicoit.cataempresas.common.SearchSite;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class HtmlUnitMultiplySiteFormAdapter extends HtmlUnitFormAdapter {
	private String siteData;
	
	public Map search() {
		Map map = new HashMap();
		
		while ((this.siteData = nextSite()) != null) {
			Map tmp = super.search();
			if (tmp == null) continue;
			
			for (Iterator keyIt = tmp.keySet().iterator(); keyIt.hasNext(); ) {
				String key = (String)keyIt.next();
				
				if (SearchSite.ERROR_IDENTIFY.equals( key )) {					
					if (map.containsKey(SearchSite.ERROR_IDENTIFY)) {
						Set error = (Set)map.get(SearchSite.ERROR_IDENTIFY);
						error.addAll((Set)tmp.get(SearchSite.ERROR_IDENTIFY));
					} else {
						map.put(SearchSite.ERROR_IDENTIFY, tmp.get(SearchSite.ERROR_IDENTIFY));						
					}					
				} else {
					map.put( key , tmp.get( key ));
				}
			}
		}
		
		return map;
	}

	//protected final String getSiteData() {
	//	return this.siteData;
	//}
	
	protected final HtmlForm getForm() throws Exception {
		return getForm(siteData);
	}
	
	protected final HtmlPage nextPage(HtmlPage nowPage) throws IOException {
		return nextPage(nowPage, this.siteData);
	}
		
	protected final String getFileName() {
		return getFileName(this.siteData);
	}

	protected final String getHost() {
		return getHost(this.siteData);
	}
	
	protected final Empresa getData(HtmlPage page) {
		return this.getData(page, this.siteData);
	}

	protected final Set getLinksForData(HtmlPage page) {
		return this.getLinksForData(page, this.siteData);
	}

	
	protected abstract Empresa getData(HtmlPage page, String siteData) ;

	protected abstract Set getLinksForData(HtmlPage page, String siteData) ;

	protected abstract HtmlPage nextPage(HtmlPage nowPage, String siteData) throws IOException ;		
	
	protected abstract HtmlForm getForm(String siteData) throws Exception ;
	
	protected abstract String getFileName(String siteData);
	
	protected abstract String getHost(String siteData);
	
	protected abstract String nextSite();	
}
