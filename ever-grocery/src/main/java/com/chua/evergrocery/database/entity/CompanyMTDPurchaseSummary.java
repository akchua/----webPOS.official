package com.chua.evergrocery.database.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.serializer.json.CompanySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@DiscriminatorValue("Company")
@Entity(name = "CompanyMTDPurchaseSummary")
public class CompanyMTDPurchaseSummary extends MTDPurchaseSummary {

	private static final long serialVersionUID = -4089605632261658058L;

	public static final String TABLE_NAME = "company_MTD_purchase_summary";

	@JsonSerialize(using = CompanySerializer.class)
	private Company company;

	@ManyToOne(targetEntity = Company.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
