package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UnitType {
	
	DEFAULT("Default", "#"),
	
	BUNDLE ("Bundle", "bd"),
	
	CASE ("Case", "cs"),
	
	SACK ("Sack", "sck"),
	
	TIN("Tin", "tin"),
	
	PIECE ("Piece", "pc"),
	
	PACK ("Pack", "pk"),
	
	PAIR ("Pair", "2s"),
	
	X6 ("6s", "6s"),
	
	X8 ("8s", "8s"),
	
	TIE ("Tie", "tie"),
	
	DOZEN ("Dozen", "dz"),
	
	REAM ("Ream", "rm"),
	
	SET ("Set", "set");
	
	//BAG ("Bag", "bg"),
	
	//BAR ("Bar", "bar"),
	
	//BOTTLE ("Bottle", "btl"),
	
	//BOX ("Box", "box"),
	
	//BUCKET ("Bucket", "bct"),
	
	//CAN ("Can", "can"),
	
	//JAR ("Jar", "jar"),
	
	//POUCH ("Pouch", "pch"),
	
	//TAB ("Tab", "tab"),
	
	//TIN ("Tin", "tin"),
	
	private final String displayName;
	
	private final String shorthand;
	
	UnitType(final String displayName, final String shorthand) {
		this.displayName = displayName;
		this.shorthand = shorthand;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getShorthand() {
		return shorthand;
	}
}
