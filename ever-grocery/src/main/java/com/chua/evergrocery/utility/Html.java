package com.chua.evergrocery.utility;

import com.chua.evergrocery.enums.Color;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 22, 2016
 */
public class Html {

	public static final String newLine = "<br/>";
	
	public static String line(String content) {
		return line(Color.DEFAULT, content);
	}
	
	public static String line(Color color, String content) {
		String s = "";
		
		switch(color) {
			case GRAY:
				s += "<p class=\"text-muted\">";
				break;
			case BLUE:
				s += "<p class=\"text-primary\">";
				break;
			case GREEN:
				s += "<p class=\"text-success\">";
				break;
			case TURQUOISE:
				s += "<p class=\"text-info\">";
				break;
			case YELLOW:
				s += "<p class=\"text-warning\">";
				break;
			case RED:
				s += "<p class=\"text-danger\">";
				break;
			default:
				s += "<p>";
				break;
		}
		
		return s + content + "</p>";
	}
	
	public static String text(String content) {
		return text(Color.DEFAULT, content);
	}
	
	public static String text(Color color, String content) {
		String s = "";
		
		switch(color) {
			case GRAY:
				s += "<span class=\"text-muted\">";
				break;
			case BLUE:
				s += "<span class=\"text-primary\">";
				break;
			case GREEN:
				s += "<span class=\"text-success\">";
				break;
			case TURQUOISE:
				s += "<span class=\"text-info\">";
				break;
			case YELLOW:
				s += "<span class=\"text-warning\">";
				break;
			case RED:
				s += "<span class=\"text-danger\">";
				break;
			default:
				s += "<span>";
				break;
		}
		
		return s + content + "</span>";
	}
}
