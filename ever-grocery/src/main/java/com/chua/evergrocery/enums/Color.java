package com.chua.evergrocery.enums;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 22, 2016
 */
public enum Color {

	GRAY("Gray"),
	
	BLUE("Blue"),
	
	GREEN("Green"),
	
	TURQUOISE("Turquoise"),
	
	YELLOW("Yellow"),
	
	RED("Red"),
	
	DEFAULT("Default");
	
	private final String name;
	
	private Color(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
