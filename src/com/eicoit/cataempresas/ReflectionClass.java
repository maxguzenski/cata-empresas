package com.eicoit.cataempresas;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eicoit.cataempresas.common.SearchSite;

public class ReflectionClass {
	private static final Map CLASS_IMPL = new HashMap();
	
	static {
		CLASS_IMPL.put("JustJavaJobs", "com.eicoit.cataempresas.impl.JustJavaJobsImpl");
		CLASS_IMPL.put("Monster", "com.eicoit.cataempresas.impl.MonsterImpl");
		
		CLASS_IMPL.put("CompuTrabajoAr", "com.eicoit.cataempresas.impl.CompuTrabajoARImpl");
		//CLASS_IMPL.put("CompuTrabajoCl", "com.eicoit.cataempresas.impl.CompuTrabajoCLImpl");
		CLASS_IMPL.put("CompuTrabajoEs", "com.eicoit.cataempresas.impl.CompuTrabajoESImpl");
		CLASS_IMPL.put("CompuTrabajoMx", "com.eicoit.cataempresas.impl.CompuTrabajoMXImpl");
		CLASS_IMPL.put("CompuTrabajoVe", "com.eicoit.cataempresas.impl.CompuTrabajoVEImpl");
	}
	
	public static SearchSite getInstance(String name, String param) {
		try {
			Class clazz = Class.forName((String)CLASS_IMPL.get(name));
			Constructor cons = clazz.getConstructor(new Class[]{String.class});
			if (cons != null) {
				return (SearchSite)cons.newInstance(new Object[]{param});
			}
			
			return (SearchSite)clazz.newInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List getAllInstance(String param)  {
		List ret = new ArrayList();		
		Set keys = CLASS_IMPL.keySet();
			
		for (Iterator it = keys.iterator(); it.hasNext(); ) {
			try {
				SearchSite site = getInstance((String)it.next(), param);
				if (site != null) ret.add( site );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ret;
	}
}
