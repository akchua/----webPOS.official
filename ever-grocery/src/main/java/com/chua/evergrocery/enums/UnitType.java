package com.chua.evergrocery.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UnitType {
	
	BAG ("Bag", "bg"),
	
	BAR ("Bar", "bar"),
	
	BOTTLE ("Bottle", "btl"),
	
	BOX ("Box", "box"),
	
	BUCKET ("Bucket", "bct"),
	
	BUNDLE ("Bundle", "bd"),
	
	CAN ("Can", "can"),
	
	CASE ("Case", "cs"),
	
	DEFAULT("Default", "#"),
	
	DOZEN ("Dozen", "dz"),
	
	JAR ("Jar", "jar"),
	
	PACK ("Pack", "pk"),
	
	PAIR ("Pair", "par"),
	
	PIECE ("Piece", "pc"),
	
	POUCH ("Pouch", "pch"),
	
	REAM ("Ream", "rm"),
	
	SACK ("Sack", "sck"),
	
	SET ("Set", "set"),
	
	TAB ("Tab", "tab"),
	
	TIE ("Tie", "tie"),
	
	TIN ("Tin", "tin"),
	
	X6 ("6s", "6s"),
	
	X8 ("8s", "8s");
	
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
