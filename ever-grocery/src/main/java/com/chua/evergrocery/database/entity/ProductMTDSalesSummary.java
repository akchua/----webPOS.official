package com.chua.evergrocery.database.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.evergrocery.serializer.json.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 12, 2019
 */
@DiscriminatorValue("Product")
@Entity(name = "ProductMTDSalesSummary")
public class ProductMTDSalesSummary extends MTDSalesSummary {

	private static final long serialVersionUID = 1643931633691566105L;

	@JsonSerialize(using = ProductSerializer.class)
	private Product product;

	@ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
