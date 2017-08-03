package com.chua.evergrocery.enums;

public enum UnitType {
	
	BAG ("bg"),
	
	BAR ("bar"),
	
	BOTTLE ("btl"),
	
	BOX ("bx"),
	
	BUCKET ("bkt"),
	
	BUNDLE ("bd"),
	
	CAN ("cn"),
	
	CASE ("cs"),
	
	DOZEN ("dz"),
	
	JAR ("jar"),
	
	PACK ("pk"),
	
	PAIR ("pr"),
	
	PIECE ("pc"),
	
	POUCH ("pch"),
	
	REAM ("rm"),
	
	SACK ("sk"),
	
	SET ("st"),
	
	TAB ("tb"),
	
	TIE ("tie"),
	
	TIN ("tin"),
	
	X6 ("6s"),
	
	X8 ("8s");
	
	private final String shorthand;
	
	UnitType(final String shorthand) {
		this.shorthand = shorthand;
	}
	
	public String getShorthand() {
		return shorthand;
	}
}
