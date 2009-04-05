package com.eicoit.cataempresas.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.eicoit.cataempresas.common.Empresa;
import com.eicoit.cataempresas.common.SearchSite;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public abstract class HtmlUnitFormAdapter implements SearchSite {
	protected final WebClient client;	
	
	protected HtmlUnitFormAdapter() {
		this.client = new WebClient();
		this.client.setTimeout(20000);
		this.getClient().setJavaScriptEnabled(false);
	}
	
	protected HtmlPage submit(HtmlForm form) throws IOException {
		return (HtmlPage)form.submit();
	}
		
	protected HtmlPage getPageLink(String link) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		return (HtmlPage)this.client.getPage(new URL(link));
	}
	
	public Map search() {
		Set empresas = new HashSet();
		Set linkErrors = new HashSet();
		
		int count = 1;		
		HtmlPage page = null;		

		try {
			System.out.println("Submetendo formulario em "+getHost()); 
			page = this.submit(this.getForm());
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
		while (page != null) {
			Set links = this.getLinksForData(page);
			System.out.println(links.size()+" links encontrados na pagina "+(count++));

			for (Iterator it = links.iterator(); it.hasNext(); ) {
				String link = "";
				
				try {
					HtmlPage pageData = this.getPageLink( (String)it.next() );
					link = pageData.getWebResponse().getUrl().toString();
					System.out.println(">> "+link);
					
					Empresa empresa = this.getData(pageData);
					empresa.setLink( link );
					empresas.add ( empresa );
					
				} catch (Exception e) {
					System.err.println(">> ERRO: "+e.getMessage());
					linkErrors.add(e.getMessage() + " no link: "+link);
				}
			}
			
			System.out.println("> Ate o momento "+ empresas.size()+" empresas encontradas.");
			
			try {
				page = this.nextPage(page);
			} catch (Exception e) {
				System.err.println(">> ERRO: "+e.getMessage());
				linkErrors.add(e.getMessage());
				break;
			}
		}
		
		Map ret = new HashMap();
		ret.put(this.getFileName(), empresas);
		ret.put(SearchSite.ERROR_IDENTIFY, linkErrors);
		
		return ret;
	}
	
	protected WebClient getClient() {
		return client;
	}
	
	protected abstract String getHost();
	
	protected abstract String getFileName();	
	
	protected abstract HtmlForm getForm() throws Exception;
		
	protected abstract Set getLinksForData(HtmlPage page);
	
	protected abstract HtmlPage nextPage(HtmlPage nowPage) throws IOException;
	
	protected abstract Empresa getData(HtmlPage page);
}
