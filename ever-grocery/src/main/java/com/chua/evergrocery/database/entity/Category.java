package com.chua.evergrocery.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.chua.evergrocery.database.entity.base.BaseObject;

@Entity(name = "Category")
@Table(name = Category.TABLE_NAME)
public class Category extends BaseObject {
	private static final long serialVersionUID = -9017987825062299908L;
	
	public static final String TABLE_NAME = "category";
	
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
