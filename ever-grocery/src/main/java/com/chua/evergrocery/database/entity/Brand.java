package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chua.evergrocery.database.entity.base.BaseObject;

@Entity(name = "Brand")
@Table(name = Brand.TABLE_NAME)
public class Brand extends BaseObject {
	private static final long serialVersionUID = 7028392275601746824L;
	
	public static final String TABLE_NAME = "brand";
	
	private String name;
	
	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
