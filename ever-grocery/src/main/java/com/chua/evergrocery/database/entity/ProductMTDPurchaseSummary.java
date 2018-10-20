package com.chua.evergrocery.database.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@DiscriminatorValue("Product")
@Entity(name = "ProductMTDPurchaseSummary")
@Table(name = ProductMTDPurchaseSummary.TABLE_NAME)
public class ProductMTDPurchaseSummary extends MTDPurchaseSummary {

	private static final long serialVersionUID = 2856734920543733062L;

	public static final String TABLE_NAME = "product_MTD_purchase_summary";
	
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
