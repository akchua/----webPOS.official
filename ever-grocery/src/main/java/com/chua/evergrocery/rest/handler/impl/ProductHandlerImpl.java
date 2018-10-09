package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.beans.ProductDetailsFormBean;
import com.chua.evergrocery.beans.ProductFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.database.entity.PriceHistory;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.service.CategoryService;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.PriceHistoryService;
import com.chua.evergrocery.database.service.ProductDetailService;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.PriceHistoryType;
import com.chua.evergrocery.enums.TaxType;
import com.chua.evergrocery.enums.UnitType;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.ProductHandler;
import com.chua.evergrocery.rest.validator.ProductFormValidator;
import com.chua.evergrocery.utility.Html;

@Transactional
@Component
public class ProductHandlerImpl implements ProductHandler {

	/*@Autowired
	private BrandService brandService;*/
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductDetailService productDetailService;
	
	@Autowired
	private PriceHistoryService priceHistoryService;
	
	@Autowired
	private ProductFormValidator productFormValidator;

	@Override
	public ObjectList<Product> getProductList(Integer pageNumber, String searchKey, Long companyId) {
		if(searchKey != null && searchKey.length() > 4 && searchKey.matches("[0-9]+")) {
			List<Product> products = new ArrayList<Product>();
			ProductDetail productDetail = productDetailService.findByBarcode(searchKey);
			if(productDetail != null) products.add(productDetail.getProduct());
			
			ObjectList<Product> productList = new ObjectList<Product>();
			productList.setList(products);
			productList.setTotal(products.size());
			return productList;
		} else {
			return productService.findAllWithPagingOrderByName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, companyId);
		}
	}
	
	@Override
	public ObjectList<PriceHistory> getSalePriceHistoryList(Integer pageNumber) {
		return priceHistoryService.findAllSaleTypeWithin30DaysOrderByCreatedOn(pageNumber, UserContextHolder.getItemsPerPage());
	}
	
	@Override
	public Product getProduct(Long productId) {
		return productService.find(productId);
	}
	
	@Override
	public List<ProductDetail> getProductDetailList(Long productId) {
		return productDetailService.findAllByProductId(productId);
	}
	
	@Override
	public ResultBean createProduct(ProductFormBean productForm) {
		final ResultBean result;
		final Map<String, String> errors = productFormValidator.validate(productForm);

		if(errors.isEmpty()) {
			if(productService.isExistsByName(productForm.getName())) {
				errors.put("name", "Name already exists!");
			}
			
			if(productForm.getCode() != null && !productForm.getCode().isEmpty() && productService.isExistsByCode(productForm.getCode())) {
				errors.put("code", "Code already exists!");
			}
			
			if(errors.isEmpty()) {
				final Product product = new Product();
				setProduct(product, productForm);
				product.setSaleRate(70.0f);
				product.setPurchaseBudget(0.0f);
				product.setTotalBudget(0.0f);
				
				result = new ResultBean();
				result.setSuccess(productService.insert(product) != null);
				if(result.getSuccess()) {
					Map<String, Object> extras = new HashMap<String, Object>();
					extras.put("productId", product.getId());
					result.setExtras(extras);
					
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Product " + Html.text(Color.BLUE, product.getDisplayName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "");
				result.addToExtras("errors", errors);
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
			result.addToExtras("errors", errors);
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateProduct(ProductFormBean productForm) {
		final ResultBean result;
		
		final Product product = productService.find(productForm.getId());
		if(product != null) {
			final Map<String, String> errors = productFormValidator.validate(productForm);
			if(!(StringUtils.trimToEmpty(product.getName()).equalsIgnoreCase(productForm.getName())) &&
					productService.isExistsByName(productForm.getName())) {
				errors.put("name", "Name already exists!");
			}
			
			if(!(StringUtils.trimToEmpty(product.getCode()).equalsIgnoreCase(productForm.getCode())) &&
					productService.isExistsByCode(productForm.getCode())) {
				errors.put("code", productForm.getName() + "Code already exists!");
			}
				
			if(errors.isEmpty()) {
				setProduct(product, productForm);
				
				result = new ResultBean();
				result.setSuccess(productService.update(product));
				if(result.getSuccess()) {
					result.setMessage(Html.line("Product " + Html.text(Color.BLUE, product.getDisplayName()) + " has been successfully " + Html.text(Color.GREEN, "updated") + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, "");
				result.addToExtras("errors", errors);
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean removeProduct(Long productId) {
		final ResultBean result;
		
		final Product product = productService.find(productId);
		if(product != null) {
			result = new ResultBean();
			
			result.setSuccess(productService.delete(product));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Product " + Html.text(Color.BLUE, product.getName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean saveProductDetails(Long productId, List<ProductDetailsFormBean> productDetailsFormList) {
		final ResultBean result;
		
		final Product product = productService.find(productId);
		if(product != null) {
			result = new ResultBean();
			
			for(ProductDetailsFormBean productDetailsForm : productDetailsFormList) {
				final ProductDetail temp = productDetailService.findByBarcode(productDetailsForm.getBarcode());
				if(productDetailsForm.getBarcode() != null && !productDetailsForm.getBarcode().equals("") && temp != null && !temp.getId().equals(productDetailsForm.getId())) {
					result.setSuccess(Boolean.FALSE);
					result.setMessage("Barcode already exists.");
					break;
				} else {
					result.setSuccess(upsertProductDetails(product, productDetailsForm));
					if(!result.getSuccess()) {
						result.setMessage("Failed to save product details.");
						break;
					}
				}
			}
			
			if(result.getSuccess()) {
				result.setMessage("Product details successfully saved.");
			}
		} else {
			result = new ResultBean(false, "Product not found.");
		}
		
		return result;
	}
	
	private void setProduct(Product product, ProductFormBean productForm) {
		product.setName(productForm.getName());
		product.setDisplayName(productForm.getDisplayName());
		product.setCode(productForm.getCode());
		/*product.setBrand(brandService.find(productForm.getBrandId()));*/
		product.setCategory(categoryService.find(productForm.getCategoryId()));
		product.setCompany(companyService.find(productForm.getCompanyId()));
		product.setTaxType(productForm.getTaxType() != null ? productForm.getTaxType() : TaxType.VAT);
		product.setAllowSeniorDiscount(productForm.getAllowSeniorDiscount() != null ? productForm.getAllowSeniorDiscount() : false);
	}
	
	private void setProductDetail(ProductDetail productDetail, ProductDetailsFormBean productDetailsForm) {
		productDetail.setTitle(productDetailsForm.getTitle());
		productDetail.setBarcode(productDetailsForm.getBarcode());
		productDetail.setQuantity(productDetailsForm.getQuantity());
		if(productDetail.getQuantity() != 0) {
			productDetail.setUnitType(productDetailsForm.getUnitType() != null ? productDetailsForm.getUnitType() : UnitType.DEFAULT);
		} else {
			productDetail.setUnitType(null);
		}
		productDetail.setGrossPrice(productDetailsForm.getGrossPrice());
		productDetail.setDiscount(productDetailsForm.getDiscount());
		productDetail.setNetPrice(productDetailsForm.getNetPrice());
		productDetail.setPercentProfit(productDetailsForm.getPercentProfit());
		productDetail.setSellingPrice(productDetailsForm.getSellingPrice());
		productDetail.setNetProfit(productDetailsForm.getNetProfit());
		productDetail.setContent(productDetailsForm.getContent());
		productDetail.setContentUnit(productDetailsForm.getContentUnit());
	}
	
	private void setPriceHistory(PriceHistory priceHistory, Product product, ProductDetail productDetail, ProductDetailsFormBean productDetailsForm, PriceHistoryType priceHistoryType) {
		priceHistory.setProduct(product);
		priceHistory.setTitle(productDetailsForm.getTitle());
		priceHistory.setUnitType(productDetailsForm.getUnitType());
		priceHistory.setPriceHistoryType(priceHistoryType);
		priceHistory.setOldPrice(productDetail.getSellingPrice() != null ? productDetail.getSellingPrice() : 0.0f);
		priceHistory.setNewPrice(productDetailsForm.getSellingPrice());
		priceHistory.setUpdatedBy(UserContextHolder.getUser().getUserEntity());
	}
	
	private Boolean upsertProductDetails(Product product, ProductDetailsFormBean productDetailsForm) {
		final Boolean success;
		
		ProductDetail productDetail = productDetailService.find(productDetailsForm.getId());
		
		if(productDetail == null) {
			productDetail = new ProductDetail();
		}
		
		// RECORD PRICE CHANGES IN PRICE HISTORY
		// for selling price
		if(productDetailsForm.getTitle().equals("Whole") || productDetailsForm.getTitle().equals("Piece")) {
			if(!productDetailsForm.getSellingPrice().equals(0.0f) && (productDetail.getSellingPrice() == null || !productDetail.getSellingPrice().equals(productDetailsForm.getSellingPrice()))) {
				final PriceHistory priceHistory = new PriceHistory();
				setPriceHistory(priceHistory, product, productDetail, productDetailsForm, PriceHistoryType.SALE);
				priceHistoryService.insert(priceHistory);
			}
		}
		// for net purchase price
		if(productDetailsForm.getTitle().equals("Whole") || productDetailsForm.getTitle().equals("Piece")) {
			if(!productDetailsForm.getNetPrice().equals(0.0f) && (productDetail.getNetPrice() == null || !productDetail.getNetPrice().equals(productDetailsForm.getNetPrice()))) {
				final PriceHistory priceHistory = new PriceHistory();
				setPriceHistory(priceHistory, product, productDetail, productDetailsForm, PriceHistoryType.NET_PURCHASE);
				priceHistoryService.insert(priceHistory);
			}
		}
		
		setProductDetail(productDetail, productDetailsForm);
		
		if(productDetail.getId() == null) {
			productDetail.setProduct(product);
			success = productDetailService.insert(productDetail) != null;
		} else {
			success = productDetailService.update(productDetail);
		}
		
		return success;
	}
	
	@Override
	public ProductDetail getUpperProductDetail(Long productDetailId) {
		final ProductDetail productDetail = productDetailService.find(productDetailId);
		final ProductDetail upperProductDetail;
		
		if(productDetail != null) {
			switch(productDetail.getTitle()) {
			case "Whole":
				upperProductDetail = null;
				break;
			case "Piece":
				upperProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "Whole");
				break;
			case "Inner Piece":
				upperProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "Piece");
				break;
			case "2nd Inner Piece":
				upperProductDetail = productDetailService.findByProductIdAndTitle(productDetail.getProduct().getId(), "Inner Piece");
				break;
			default:
				upperProductDetail = null;
			}
		} else {
			upperProductDetail = null;
		}
		
		return upperProductDetail;
	}
	
	@Override
	public List<UnitType> getUnitTypeList() {
		return Stream.of(UnitType.values())
				.collect(Collectors.toList());
	}
}
