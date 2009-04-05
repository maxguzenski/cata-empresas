package com.eicoit.cataempresas.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eicoit.cataempresas.common.Empresa;
import com.gargoylesoftware.htmlunit.html.HtmlFont;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class MonsterImpl extends HtmlUnitMultiplySiteFormAdapter {
	private static final String KEYWORD_NAME = "q";
	private static final String[] LINKS_DATA = {"/getjob.asp?", "\"" };
	private static final String[] DATA_REGEX = {"<FONT FACE=\"Verdana,Arial,Helvetica\" SIZE=-1>", "</FONT></NOBR></TD></TR></TABLE>"};
	private static final String NEXT_PAGE  = "<a href=\"[\\W[\\w]]*\">Next page";
	
	private static final String DATA_USA_COMPANY = "class=\"FormLabelBold\">Company:</td>";
	private static final String DATA_USA_CONTACT = "class=\"FormLabelBold\">Contact:</td>";
	private static final String DATA_USA_EMAIL = "class=\"FormLabelBold\">Email:</td>";
	
	
	private Map sites;
	private Iterator itSites;
	private String param;
	
	public MonsterImpl(String param) {
		this.param = param;
		this.sites = new HashMap();
		
		/*1º site ident(file name), 2º host, 3º form url, */
		//this.sites.put("monster-uk", 			new String[]{"http://www.monster.co.uk", 	"http://jobsearch.monster.co.uk", "UK"} );
		this.sites.put("monster-belgium", 		new String[]{"http://english.monster.be", 	"http://jobsearch.monster.be", "BE"} );
		this.sites.put("monster-denmark", 		new String[]{"http://www.monster.dk", 		"http://jobsoeg.monster.dk", "DK"} );
		this.sites.put("monster-czechrepublic", new String[]{"http://www.monster.cz", 		"http://jobsearch.monster.cz", "CZ"} );
		this.sites.put("monster-france", 		new String[]{"http://www.monster.fr", 		"http://offres.monster.fr", "FR"} );
		this.sites.put("monster-germany", 		new String[]{"http://www.monster.de/", 		"http://jobsuche.monster.de", "DE"} );
		this.sites.put("monster-finland", 		new String[]{"http://www.monster.fi",		"http://hae.monster.fi", "FI"} );
		this.sites.put("monster-ireland", 		new String[]{"http://www.monster.ie", 		"http://jobsearch.monster.ie", "IE"} );
		this.sites.put("monster-italy", 		new String[]{"http://www.monster.it", 		"http://cercalavoro.monster.it", "IT"} );
		this.sites.put("monster-netherlands",	new String[]{"http://www.monsterboard.nl", 	"http://jobsearch.monsterboard.nl", "NL"} );
		this.sites.put("monster-luxembourg", 	new String[]{"http://www.monster.no", 		"http://jobbsok.monster.no", "NO"} );
		this.sites.put("monster-norway", 		new String[]{"http://english.monster.lu", 	"http://jobsearch.monster.lu", "LU"} );
		this.sites.put("monster-spain", 		new String[]{"http://www.monster.es", 		"http://buscartrabajo.monster.es", "ES"} );
		this.sites.put("monster-sweden", 		new String[]{"http://www.jobline.se", 		"http://jobb.monster.se", "SE"} );
		this.sites.put("monster-switzerland", 	new String[]{"http://english.monster.ch", 	"http://jobsearch.monster.ch", "CH"} );		
		this.sites.put("monster-canada", 		new String[]{"http://www.monster.ca", 		"http://jobsearch.monster.ca", "CA"} );
		this.sites.put("monster-usa", 			new String[]{"http://www.monster.com", 		"http://jobsearch.monster.com", "USA"} );
	}
	
	private Empresa getDataUSA(HtmlPage page) {
		Empresa empresa = new Empresa();
		String str  = page.getWebResponse().getContentAsString();
		
		int posInit = str.lastIndexOf(DATA_USA_COMPANY);
		int posEnd  = (posInit > -1) ? str.indexOf("</tr>", posInit) : -1;		
		if (posInit > -1) {
			String company = str.substring(posInit + DATA_USA_COMPANY.length(), posEnd);
			
			posInit = company.indexOf(">");
			posEnd = company.lastIndexOf("<");
			if (posInit > -1) empresa.setNome(company.substring(posInit+1, posEnd));
		}
		
		posInit = str.lastIndexOf(DATA_USA_CONTACT);
		posEnd  = (posInit > -1) ? str.indexOf("</tr>", posInit) : -1;		
		if (posInit > -1) {
			String contact = str.substring(posInit + DATA_USA_CONTACT.length(), posEnd);
			
			posInit = contact.indexOf(">");
			posEnd = contact.lastIndexOf("<");
			if (posInit > -1) empresa.setContato(contact.substring(posInit+1, posEnd));
		}
		
		posInit = str.lastIndexOf(DATA_USA_EMAIL);
		posEnd  = (posInit > -1) ? str.indexOf("</tr>", posInit) : -1;		
		if (posInit > -1) {
			String line  = str.substring(posInit + DATA_USA_EMAIL.length(), posEnd);
			
			int posAInit = line.indexOf("<A");
			int posAEnd  = line.indexOf("</A>");
			if (posAInit > -1) {
				String ahref = line.substring(posAInit, posAEnd); 
				String email = ahref.substring(ahref.indexOf(">")+1);
			
				int indexArr = email.indexOf("@");
			
				empresa.setEmail(email);
				if (indexArr > -1) empresa.setSite("http://www." + email.substring(email.indexOf("@")+1));
			}
		}		
		
		return (empresa.getNome() == null) ? null : empresa ;
	}
	
	protected Empresa getData(HtmlPage page, String siteData) {
		if (siteData.equals("monster-usa")) {
			return getDataUSA(page);
		}
		
		Empresa empresa = null;
		String str  = page.getWebResponse().getContentAsString();
		
		try {			
			int posInit = str.lastIndexOf(DATA_REGEX[0]) + DATA_REGEX[0].length();
			int posEnd  = str.indexOf(DATA_REGEX[1], posInit);
			
			String data = str.substring(posInit, posEnd);
			empresa = new Empresa();

			int lastBr = 0;
			int nextBr = data.indexOf("<BR>", lastBr);
			
			if (nextBr > -1) {
				String data1 = data.substring(lastBr, nextBr).trim();
				if (data1.indexOf("<A") > -1) {
					String email = data1.substring(data1.indexOf(">")+1, data1.indexOf("<", 2));
					int indexArr = email.indexOf("@");
					empresa.setEmail(email);
					if (indexArr > -1) empresa.setSite( "http://www." + email.substring(indexArr+1));
				} else {
					empresa.setContato(data1);
				}
				
				lastBr = nextBr + "<BR>".length();
				nextBr = data.indexOf("<BR>", lastBr);
			
				if (nextBr > -1) {
					data1 = data.substring(lastBr, nextBr).trim();
					if (data1.indexOf("<A") > -1) {
						String email = data1.substring(data1.indexOf(">")+1, data1.indexOf("<", 2));
						int indexArr = email.indexOf("@");
						empresa.setEmail(email);
						if (indexArr > -1) empresa.setSite( "http://www." + email.substring(indexArr+1));
						
					} else {
						empresa.setNome(data1);
					}
				}
				
				if (empresa.getNome() == null) {
					lastBr = nextBr + "<BR>".length();
					nextBr = data.indexOf("<BR>", lastBr);
				
					if (nextBr > -1) {
						empresa.setNome( data.substring(lastBr, nextBr).trim() );
					}
				}
			} else {
				if (data.indexOf("<A") > -1) {
					String email = data.substring(data.indexOf(">")+1, data.indexOf("<", 2));
					int indexArr = email.indexOf("@");
					empresa.setEmail(email);
					if (indexArr > -1) empresa.setSite( "http://www." + email.substring(indexArr+1));

				} else {
					empresa.setNome( data );
				}				
			}
			
		} catch (Exception ex) {
			empresa = null;
		}	
		
		String pais = ((String[])sites.get(siteData))[2];
		empresa.setLocal(pais);
		
		return empresa;
	}

	protected Set getLinksForData(HtmlPage page, String siteData) {
		System.out.println("LINK: "+page.getWebResponse().getUrl().toString());
		
		Set links = new HashSet();
		String str = page.getWebResponse().getContentAsString();
		String formUrl = ((String[])this.sites.get(siteData))[1];		

		int pos0 = 0;
		int pos1 = 0;
		while( str.length() > pos1 && (pos0 = str.indexOf(LINKS_DATA[0], pos1)) > -1) {
			pos1 = str.indexOf(LINKS_DATA[1], pos0);
			String lnk = str.substring(pos0, pos1);			
						
			links.add( (lnk.startsWith("/")) ? formUrl + lnk : formUrl + "/" + lnk );
			pos0 = pos1;
		}

		return links;
	}

	protected HtmlPage nextPage(HtmlPage nowPage, String siteData) throws IOException {
		String str = nowPage.getWebResponse().getContentAsString();
		String formUrl = ((String[])this.sites.get(siteData))[1];
		String lnk = null;
		
		Pattern p = Pattern.compile(NEXT_PAGE);
		Matcher m = p.matcher(str);		
		if (m.find()) {
			str = m.group();
			if (str != null && str.trim().length() > 0) {
				int indexAhref = str.lastIndexOf("href");
				if (indexAhref > -1) {
					String ahref = str.substring(indexAhref);
					lnk = ahref.substring(ahref.indexOf("\"")+1, ahref.lastIndexOf("\""));
					lnk = (lnk.startsWith("/")) ? formUrl + lnk : formUrl + "/" + lnk; 
				}				
			}
		}	
		
		if (lnk != null) return this.getPageLink(lnk);
		return null;
	}

	protected HtmlForm getForm(String siteData) throws Exception {
		
		String[] site = (String[])this.sites.get(siteData);
		
		HtmlPage page = this.getPageLink(site[1]+"/?cookie=no");		
		HtmlForm form = (HtmlForm)page.getForms().get(0);
		
		form.setAttributeValue(KEYWORD_NAME,this.param);
		
		return form;
	}

	protected String getFileName(String siteData) {
		return siteData;
	}

	protected String getHost(String siteData) {
		String[] site = (String[])this.sites.get(siteData);
		return site[0];
	}

	protected String nextSite() {
		if (this.itSites == null) this.itSites = this.sites.keySet().iterator();
		if (this.itSites.hasNext()) return (String)this.itSites.next();
		return null;
	}	
}
