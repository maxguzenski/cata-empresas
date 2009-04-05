package com.eicoit.cataempresas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.eicoit.cataempresas.common.Empresa;
import com.eicoit.cataempresas.common.SearchSite;

public class Run {
	private static void outPutCvs(Map mapOutPut) throws IOException {
		final String titleOut = "EMPRESA;SITE;CONTATO;EMAIL;LOCAL;LINK;\n";
		OutputStream out = null;
			
		Set errors = (Set)mapOutPut.remove(SearchSite.ERROR_IDENTIFY);
		if (errors != null && errors.size() > 0) {
			String fileErro = "";
			
			for (Iterator it = errors.iterator(); it.hasNext(); ) {
				fileErro += it.next()+"\n";
			}
			
			out = new  FileOutputStream(new File("./"+SearchSite.ERROR_IDENTIFY+".csv"));
			out.write(fileErro.getBytes());
			out.flush();
			out.close();			
		}
		
		
		for (Iterator it = mapOutPut.keySet().iterator(); it.hasNext(); ) {
			String key = (String)it.next();
			Set empresas = (Set)mapOutPut.get(key);
			String strOut = "";
			
			for (Iterator keyIt = empresas.iterator(); keyIt.hasNext(); ) {
				Empresa empresa = (Empresa)keyIt.next();
			
				strOut += empresa.getNome()+";";
				strOut += (empresa.getSite() != null) ? empresa.getSite()+";" : ";";
				strOut += (empresa.getContato() != null) ? empresa.getContato()+";" : ";";
				strOut += (empresa.getEmail() != null) ? empresa.getEmail()+";" : ";";
				strOut += (empresa.getLocal() != null) ? empresa.getLocal()+";" : ";";
				strOut += empresa.getLink()+";";
				strOut += "\n";
			}
			
			out = new  FileOutputStream(new File("./"+key+".csv"));
			out.write( (titleOut + strOut).getBytes() );
			out.flush();
			out.close();					
		}
	}
	
	private static void outPutSql(Map mapOutPut) throws IOException {
		final String titleOut = "EMPRESA;SITE;CONTATO;EMAIL;LOCAL;LINK;\n";
		final String sqlInit  = "insert into EMPRESA(NOME, SITE, CONTATO, EMAIL_CONTATO, LOCAL, LINK) VALUES (";
		OutputStream out = null;
			
		Set errors = (Set)mapOutPut.remove(SearchSite.ERROR_IDENTIFY);
		if (errors != null && errors.size() > 0) {
			String fileErro = "";
			
			for (Iterator it = errors.iterator(); it.hasNext(); ) {
				fileErro += it.next()+"\n";
			}
			
			out = new  FileOutputStream(new File("./"+SearchSite.ERROR_IDENTIFY+".csv"));
			out.write(fileErro.getBytes());
			out.flush();
			out.close();			
		}
		
		
		for (Iterator it = mapOutPut.keySet().iterator(); it.hasNext(); ) {
			String key = (String)it.next();
			Set empresas = (Set)mapOutPut.get(key);
			String strOut = "";
						
			for (Iterator keyIt = empresas.iterator(); keyIt.hasNext(); ) {
				Empresa empresa = (Empresa)keyIt.next();
			
				strOut += sqlInit;
				strOut += "'"+empresa.getNome()+"',";
				strOut += (empresa.getSite() != null) ? "'"+empresa.getSite()+"'," : "null,";
				strOut += (empresa.getContato() != null) ? "'"+empresa.getContato()+"'," : "null,";
				strOut += (empresa.getEmail() != null) ? "'"+empresa.getEmail()+"'," : "null,";
				strOut += (empresa.getLocal() != null) ? "'"+empresa.getLocal()+"'," : "null,";
				strOut += "'"+ empresa.getLink()+"') ";
				strOut += "\ngo\n";
			}
			
			out = new  FileOutputStream(new File("./"+key+".sql"));
			out.write( (titleOut + strOut).getBytes() );
			out.flush();
			out.close();					
		}
	}
	
	
	private static void run(SearchSite impl) {
		try {
			long time = System.currentTimeMillis();		
			Run.outPutSql(impl.search());			
			time = System.currentTimeMillis() - time;
			
			System.out.println("Rastreamento finalizado em "+((double)(time / 1000 / 60))+"min ");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		String className = args[0];
		String params = null;
		
		if (className == null) throw new IllegalArgumentException("Nome da class (parametro 1) não pode ser nulo.");
		if (args.length > 1) params = args[1];
			
		if (className.trim().equals("all")) {
			for (Iterator it = ReflectionClass.getAllInstance(params).iterator(); it.hasNext(); ) {
				Run.run( (SearchSite)it.next() );	
			}			
		} else {
			Run.run( ReflectionClass.getInstance(className, params));
		}
	}

}
