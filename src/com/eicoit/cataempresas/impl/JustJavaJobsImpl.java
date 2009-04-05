package com.eicoit.cataempresas.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.eicoit.cataempresas.common.Empresa;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class JustJavaJobsImpl extends HtmlUnitFormAdapter {
	private static final String HOST = "http://www.justjavajobs.com";
	private static final String FORM_URL = "http://www.justjavajobs.com";
	private static final String FORM_NAME = "form1";
	private static final String LINK_URL = HOST + "/Job.asp?ID=";
	private static final String FILE_NAME = "justJavaJobs";
	
	private static final String[]   EMPRESA    = {"<font face=\"Verdana\" size=3>","</font>"};
	private static final String[]   CONTATO    = {"emailEmployer()\">Email ", "</a>" };
	private static final String[][] EMAIL      = { {"EmailLeft\" value=\"","\">"}, {"EmailRight\" value=\"", "\">"} };		
	private static final String[]   SITE       = {"target=_Blank>","</A>"};		
	
	private Iterator nextPage;
	private String param;
	
	public JustJavaJobsImpl(String params) {
		this.param = params;
	}

	protected HtmlForm getForm() throws Exception {
		HtmlForm form = this.getPageLink(FORM_URL).getFormByName( FORM_NAME );
		form.setAttributeValue("JSSearchKeywords", this.param);
		return form;
	}
	
	protected Set getLinksForData(HtmlPage page) {
		Set links = new HashSet();
		for (int count = 1; count <= 50; count++) {
			String countStr = (count < 10) ? "0"+count : ""+count;
			List checks = page.getDocumentElement().getHtmlElementsByAttribute("input", "name", "check_"+countStr);
			
			if (checks == null || checks.isEmpty()) break;
			
			HtmlCheckBoxInput check = (HtmlCheckBoxInput)checks.get(0);
			links.add(LINK_URL + check.getValueAttribute());
		}
		
		return links;
	}

	protected HtmlPage nextPage(HtmlPage nowPage) throws IOException {
		if (this.nextPage == null) {
			HtmlSelect select = (HtmlSelect)nowPage.getDocumentElement().getHtmlElementsByAttribute("select", "name", "Goto").get(0);
			this.nextPage = select.getOptions().iterator();
			
			// a primeira eh a propria pagina atual
			if (this.nextPage.hasNext()) this.nextPage.next();
		}
		
		if (this.nextPage.hasNext()) {
			HtmlOption option = (HtmlOption)this.nextPage.next();
			
			HtmlForm form = nowPage.getFormByName( FORM_NAME );
			form.setActionAttribute(option.getValueAttribute());
			return (HtmlPage) form.submit();				
		}
		
		return null;
	}

	protected Empresa getData(HtmlPage page) {
		StringBuffer sb = new StringBuffer();
		sb.append(page.getWebResponse().getContentAsString());
		
		int posInit = 0;
		int posEnd  = 0;
		Empresa empresa = new Empresa();
		
		posInit = sb.indexOf(EMPRESA[0]);		
		posEnd  = (posInit > -1) ? sb.indexOf(EMPRESA[1], posInit) : -1;		
		if (posInit > -1 && posEnd > -1) {
			empresa.setNome( sb.substring(posInit+EMPRESA[0].length(), posEnd) );
		}
		
		posInit = sb.indexOf(SITE[0]);		
		posEnd  = (posInit > -1) ? sb.indexOf(SITE[1], posInit) : -1;		
		if (posInit > -1 && posEnd > -1) {
			empresa.setSite( sb.substring(posInit+SITE[0].length(), posEnd) );
		}		
		
		posInit = sb.indexOf(EMAIL[0][0]);		
		posEnd  = (posInit > -1) ? sb.indexOf(EMAIL[0][1], posInit) : -1;		
		if (posInit > -1 && posEnd > -1) {
			String maiLeft = sb.substring(posInit+EMAIL[0][0].length(), posEnd);
			
			posInit = sb.indexOf(EMAIL[1][0]);		
			posEnd  = (posInit > -1) ? sb.indexOf(EMAIL[1][1], posInit) : -1;

			String maiRight = sb.substring(posInit+EMAIL[1][0].length(), posEnd);
			
			if (!maiLeft.trim().equals("") && !maiRight.trim().equals(""))			
				empresa.setEmail( maiLeft + "@" +  maiRight );
		}			
		
		posInit = sb.indexOf(CONTATO[0]);		
		posEnd  = (posInit > -1) ? sb.indexOf(CONTATO[1], posInit) : -1;		
		if (posInit > -1 && posEnd > -1) {
			empresa.setContato( sb.substring(posInit+CONTATO[0].length(), posEnd) );
		}	
		
		empresa.setLocal("USA");
		
		return empresa;
	}

	public String getHost() {
		return HOST;
	}

	public String getFileName() {
		return FILE_NAME;
	}
}
