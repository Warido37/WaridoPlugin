package com.warido.plugin;

public interface StringTo {
	public static Long toLong(String s) {
		System.out.println("WARIDO: STARTING");
		try {
			System.out.println("WARIDO: Trying");
			long res = Long.parseLong(s);
			System.out.println("WARIDO: "+res);
			return res;
		} catch(Exception e) {
			System.out.println("WARIDO: ERROR");
			return null;
		}
	}
}
