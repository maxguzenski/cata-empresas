package com.eicoit.cataempresas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

public class CVSToSQL {
	public static void main(String[] args) throws IOException {
		final String sqlInit  = "insert into EMPRESA(NOME, SITE, CONTATO, EMAIL_CONTATO, LOCAL, LINK) VALUES (";
		
		String file = "";
		String outFile = "";
		InputStream  in  = new FileInputStream(new File(args[0] + ".csv"));
		OutputStream out = new FileOutputStream(new File(args[0] + ".sql"));
		
		byte[] line = new byte[1024];	
		while ( in.read(line) > -1) {
			file += new String(line);
		}		
		
		
		StringTokenizer nlTok = new StringTokenizer(file, "\n");
		while (nlTok.hasMoreTokens()) {
			StringTokenizer lnTok = new StringTokenizer(nlTok.nextToken(), ";");
			outFile += sqlInit;
			
			while (lnTok.hasMoreTokens()) {
				String value = lnTok.nextToken();
				
				if (! lnTok.hasMoreTokens()) {
					outFile += "'IE', ";
					outFile += "'"+value.trim()+"'";
				} else {
					if (value == null || value.trim().equals("")) outFile += "null";
					else outFile += "'"+value.trim()+"'";
				}
								
				if (lnTok.hasMoreTokens()) outFile += ", ";
			}
			
			outFile += ")\ngo\n";
		}
		
		out.write(outFile.getBytes());
		out.flush();
		out.close();
	}
}
