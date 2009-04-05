package com.eicoit.cataempresas.common;

import java.util.Map;

public interface SearchSite {	
	public static final String ERROR_IDENTIFY = "errors";	
	Map search() throws Exception;
}
