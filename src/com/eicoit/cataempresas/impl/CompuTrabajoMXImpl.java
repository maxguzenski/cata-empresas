package com.eicoit.cataempresas.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eicoit.cataempresas.common.Empresa;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CompuTrabajoMXImpl extends HtmlUnitFormAdapter {

	private static final String HOST = "http://www.computrabajo.com.mx/";
	private static final String FORM_URL = "http://www.computrabajo.com.mx/bt-ofertas.htm";
	private static final String FORM_NAME = "";
	private static final String FILE_NAME = "computrabajo_MX";
	private static final String NEXT_PAGE = "bt-ofrlistado.htm?Bqd=&BqdPalabras=java&BqdComienzo=";
	
	private static final String[]   EMPRESA    = {"Empresa:", "Contacto:"};
	private static final String[]   CONTATO    = {"Contacto:", "Teléfono:" };

	private String param;
	
	// USADAS PARA CONTROLE DE PAGINACAO
	private int countLinks = 0;
	private int qtAnt = 0;
	
	public CompuTrabajoMXImpl(String param) {
		this.param = param;
	}
	
	protected HtmlForm getForm() throws Exception {
		HtmlForm form = this.getPageLink(FORM_URL).getFormByName(FORM_NAME);
		form.setAttributeValue("BqdPalabras", this.param);
		
		return form;
	}

	protected Set getLinksForData(HtmlPage page) {
		
		Set links = new HashSet();

		String strPage = page.asXml();
					
		Pattern p = Pattern.compile("bt-ofrd(.*?)htm");
		Matcher m = p.matcher(strPage);

		while (m.find()) {
			//System.out.println(HOST+m.group());
			links.add(HOST+m.group());
		}
		this.countLinks += links.size();
		return links;
	}

	protected HtmlPage nextPage(HtmlPage nowPage) throws IOException {

		if (this.qtAnt != this.countLinks && (this.countLinks % 10) == 0) {
			
			this.qtAnt = this.countLinks;
			
			String strPage = nowPage.asText();
			System.out.println("###   PAGE: "+strPage);
			
			String url = HOST + NEXT_PAGE + (this.countLinks+1);
				
			System.out.println("####################################################################################");
			System.out.println("###   NEXT PAGE: "+url);
				
			HtmlPage newPage = this.getPageLink(url).getPage();
				
			//System.out.println("###   PAGE: "+newPage.asText());
			System.out.println("####################################################################################");

			//System.out.println("#### ORIGINAL:  " + HOST + "bt-ofrlistado.htm?Bqd=&BqdPalabras=java&BqdComienzo=21");
			//System.out.println("#### MANUAL:    " + HOST + NEXT_PAGE + (this.countLinks+1));

			return newPage;
		} else {
			return null;
		}
		
	}

	protected Empresa getData(HtmlPage page) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(page.asText());
		
		int posInit = 0;
		int posEnd  = 0;

		Empresa empresa = new Empresa();
		
		posInit = sb.indexOf(EMPRESA[0]);
		posEnd  = (posInit > -1) ? sb.indexOf(EMPRESA[1], posInit) : -1;
		// NOME EMPRESA
		if (posInit > -1 && posEnd > -1) {
			empresa.setNome(sb.substring(posInit + EMPRESA[0].length(), posEnd).trim());
		}
		
		posInit = sb.indexOf(CONTATO[0]);
		posEnd  = (posInit > -1) ? sb.indexOf(CONTATO[1], posInit) : -1;
		if (posInit > -1 && posEnd > -1) {
			empresa.setContato(sb.substring(posInit+CONTATO[0].length(), posEnd).trim());
		}	

		empresa.setSite(null);
		empresa.setEmail(null);
		empresa.setLocal("MX");

		System.out.println("\n### EMPRESA:"+empresa.getNome());
		System.out.println("### CONTATO:"+empresa.getContato()+"\n");

		return empresa;
	}

	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHost() {
		return HOST;
	}

	public String getFileName() {
		return FILE_NAME;
	}
}

