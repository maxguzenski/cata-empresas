package com.eicoit.cataempresas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegx {
	public static void main(String[] args) {
		String value = "<fonr> dsa dsa dsa dsa\n\r dsa ds a<a href=\"/jobsearch.asp?re=4&pg=2&brd=1&cy=UK&brd=1&z=norespage&q=java&sort=rv&vw=d\">Next page &gt;&gt;</a>";
		String regx = "<a href=\"[\\W[\\w]]*\">Next page";
		
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(value);		
		if (m.find()) {
			System.out.println( m.groupCount() );
			System.out.println( m.group() );
		}		
	}
}
