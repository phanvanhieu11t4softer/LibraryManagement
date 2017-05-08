package com.framgia.util;

import java.util.Locale;

public class Helper {
	
	public static String formatCurrency(Float price) {
		java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(Locale.US);
		
		return format.format(price);
	}
}
