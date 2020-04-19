package com.chua.evergrocery.rest.handler.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.UserContextHolder;
import com.chua.evergrocery.annotations.CheckAuthority;
import com.chua.evergrocery.beans.GeneratedOfftakeBean;
import com.chua.evergrocery.beans.GeneratedProductPOBean;
import com.chua.evergrocery.beans.InventoryBean;
import com.chua.evergrocery.beans.PurchaseOrderFormBean;
import com.chua.evergrocery.beans.ResultBean;
import com.chua.evergrocery.beans.UserBean;
import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.constants.PrintConstants;
import com.chua.evergrocery.database.entity.Company;
import com.chua.evergrocery.database.entity.Product;
import com.chua.evergrocery.database.entity.ProductDetail;
import com.chua.evergrocery.database.entity.PurchaseOrder;
import com.chua.evergrocery.database.entity.PurchaseOrderDetail;
import com.chua.evergrocery.database.service.CompanyService;
import com.chua.evergrocery.database.service.ProductDetailService;
import com.chua.evergrocery.database.service.ProductService;
import com.chua.evergrocery.database.service.PurchaseOrderDetailService;
import com.chua.evergrocery.database.service.PurchaseOrderService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.enums.Color;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.enums.Status;
import com.chua.evergrocery.objects.ObjectList;
import com.chua.evergrocery.rest.handler.InventoryHandler;
import com.chua.evergrocery.rest.handler.PurchaseOrderHandler;
import com.chua.evergrocery.rest.handler.TransactionSummaryHandler;
import com.chua.evergrocery.rest.validator.PurchaseOrderFormValidator;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.Html;
import com.chua.evergrocery.utility.SimplePdfWriter;
import com.chua.evergrocery.utility.StringHelper;
import com.chua.evergrocery.utility.format.DateFormatter;
import com.chua.evergrocery.utility.print.Printer;
import com.chua.evergrocery.utility.template.GeneratedOfftakeTemplate;
import com.chua.evergrocery.utility.template.GeneratedPurchaseTemplate;
import com.chua.evergrocery.utility.template.PurchaseOrderCopyTemplate;

/**
 * All 0.999 values found in this class are correction factors used only for
 * computations and does not affect actual stored data
 * 
 * @author EVER
 *
 */
@Transactional
@Component
public class PurchaseOrderHandlerImpl implements PurchaseOrderHandler {

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private PurchaseOrderDetailService purchaseOrderDetailService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductDetailService productDetailService;

	@Autowired
	private InventoryHandler inventoryHandler;

	@Autowired
	private TransactionSummaryHandler transactionSummaryHandler;

	@Autowired
	private PurchaseOrderFormValidator purchaseOrderFormValidator;

	@Autowired
	private FileConstants fileConstants;

	@Autowired
	private VelocityEngine velocityEngine;

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	public ObjectList<PurchaseOrder> getPurchaseOrderList(Integer pageNumber, Long companyId, Boolean showChecked) {
		if (showChecked) {
			return purchaseOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), companyId);
		} else {
			return this.getActivePurchaseOrderList(pageNumber, companyId);
		}
	}

	private ObjectList<PurchaseOrder> getActivePurchaseOrderList(Integer pageNumber, Long companyId) {
		return purchaseOrderService.findAllWithPagingAndStatus(pageNumber, UserContextHolder.getItemsPerPage(),
				companyId, new Status[] { Status.LISTING });
	}

	@Override
	public PurchaseOrder getPurchaseOrder(Long purchaseOrderId) {
		return (purchaseOrderId != null && purchaseOrderId > 0l) ? purchaseOrderService.find(purchaseOrderId) : null;
	}

	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean createPurchaseOrder(PurchaseOrderFormBean purchaseOrderForm) {
		final ResultBean result;
		Map<String, String> errors = purchaseOrderFormValidator.validate(purchaseOrderForm);

		if (errors.isEmpty()) {
			final Company company = companyService.find(purchaseOrderForm.getCompanyId());
			if (company != null) {
				errors = purchaseOrderFormValidator
						.validateDeliveryDateWithCompanyLastPurchaseDate(purchaseOrderForm.getDeliveredOn(), company);
				if (errors.isEmpty()) {
					final PurchaseOrder purchaseOrder = new PurchaseOrder();
					purchaseOrder.setCompany(company);
					purchaseOrder.setTotalAmount(0.0f);
					purchaseOrder.setTotalItems(0);

					purchaseOrder.setCreator(userService.find(UserContextHolder.getUser().getId()));
					purchaseOrder.setStatus(Status.LISTING);
					purchaseOrder.setDeliveredOn(purchaseOrderForm.getDeliveredOn());
					purchaseOrder.setCheckedOn(DateUtil.getDefaultDate());

					result = new ResultBean();
					result.setSuccess(purchaseOrderService.insert(purchaseOrder) != null);
					if (result.getSuccess()) {
						Map<String, Object> extras = new HashMap<String, Object>();
						extras.put("purchaseOrderId", purchaseOrder.getId());
						result.setExtras(extras);

						result.setMessage("Purchase order successfully created.");
					} else {
						result.setMessage("Failed to create purchase order.");
					}
				} else {
					result = new ResultBean(Boolean.FALSE, "");
					result.addToExtras("errors", errors);
				}
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line(Html.text(Color.RED, "Failed") + " to load company. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
			result.addToExtras("errors", errors);
		}

		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean generatePurchaseOrder(Long companyId, Float daysToBook) {
		// Storing start time of this method
		final Date generateStartTime = new Date();

		final ResultBean result;
		final Company company = companyService.find(companyId);

		if (company != null) {
			if (daysToBook >= 3) {
				// Check if last purchase order date is more than 3 days ago
				if (Days.daysBetween(new DateTime(company.getLastPurchaseOrderDate()), new DateTime()).getDays() > 3) {
					LOG.info("#### Generating purchase order for " + company.getName() + " - "
							+ DateFormatter.longFormat(generateStartTime));

					// Determining last purchase order date
					Calendar lastPODate = Calendar.getInstance();
					lastPODate.setTime(company.getLastPurchaseOrderDate());
					if (lastPODate.getTimeInMillis() == DateUtil.getDefaultDateInMillis()) {
						lastPODate
								.setTime(purchaseOrderService.findLatestDeliveryByCompany(companyId).getDeliveredOn());
					}
					LOG.info("Last PO Date : " + lastPODate.getTime());

					// Determining last booked days
					if (company.getDaysBooked().equals(0.0f)) {
						company.setDaysBooked(
								Days.daysBetween(new DateTime(lastPODate.getTime()), new DateTime()).getDays() / 1.0f);
					}
					LOG.info("Last Booked Days : " + company.getDaysBooked());

					// Determining actual sales period (in days)
					final int salesPeriod = DateUtil.daysBetween(company.getLastPurchaseOrderDate(), generateStartTime);
					LOG.info("Actual Sales Period (In Days) : " + salesPeriod);

					// Retrieving latest inventories
					final List<InventoryBean> inventories = inventoryHandler.getProductInventoryByCompany(companyId,
							generateStartTime);
					final List<GeneratedProductPOBean> generatedProductPOs = new ArrayList<GeneratedProductPOBean>();

					for (InventoryBean inventory : inventories) {
						final Product product = inventory.getProduct();
						final GeneratedProductPOBean generatedProductPO = new GeneratedProductPOBean();
						generatedProductPO.setProductId(product.getId());
						generatedProductPO.setProductName(product.getName());
						generatedProductPO.setProductCode(product.getCode());
						generatedProductPO.setInventory(inventory);

						LOG.info("## Generating purchase order of " + product.getName());

						// Compute for ACTUAL BUDGET (Adjusted Stock Budget +
						// Net Purchase Amount)
						final Float actualBudget = product.getStockBudget() + inventory.getTotalNetPurchase();
						LOG.info("Computed actual budget : " + actualBudget);

						// Compute for sale rate (net sales amount / actual
						// budget * 100) [cap value between 0-100%]
						final Float proportionedNetSalesAmount = (inventory.getTotalBaseSales() < actualBudget)
								? inventory.getTotalBaseSales() / salesPeriod * company.getDaysBooked()
								: inventory.getTotalBaseSales();
						Float tempRate = actualBudget.equals(0.0f) ? 0
								: proportionedNetSalesAmount / actualBudget * 100;
						final Float saleRate = tempRate > 100.0f ? 100.0f : (tempRate < 0.0f ? 0.0f : tempRate);
						LOG.info("Computed sale rate : " + saleRate);

						// Compute adjustment rate {actual value to be
						// multiplied to budget for simplicity}
						// 20/30 Maximum increase of 25% at 70-100% sales AND
						// 30/70 Maximum decrease of 30% at 0-70% sales
						Float tempAdjustmentRate = (saleRate - 70.0f)
								* (saleRate >= 70.0f ? (25.0f / 30.0f) : (30.0f / 70.0f)) / 100.0f;
						if ((saleRate >= 95.0f && product.getSaleRate() >= 95.0f)
								|| (saleRate <= 30 && product.getSaleRate() <= 30)) {
							tempAdjustmentRate *= 2.0f;
						}
						final Float adjustmentRate = 1.0f + tempAdjustmentRate;
						LOG.info("Computed adjustment rate : " + adjustmentRate);

						// ################ CHANGES TO PRODUCT STARTS HERE

						// Compute new total budget (total budget * adjustment
						// rate)
						Float tempTotalBudget = actualBudget * adjustmentRate;
						tempTotalBudget = saleRate >= 70 ? Math.max(tempTotalBudget, product.getTotalBudget())
								: Math.min(tempTotalBudget, product.getTotalBudget());
						// adjust to number of days to book
						if (!company.getDaysBooked().equals(0.0f))
							tempTotalBudget = (tempTotalBudget / company.getDaysBooked()) * daysToBook;
						product.setTotalBudget(tempTotalBudget);
						LOG.info("Computed new total budget : " + product.getTotalBudget());

						// Compute new purchase budget (total budget - (actual
						// budget - sales))
						final Float actualStock = inventory.getStockBudget();
						product.setPurchaseBudget(product.getTotalBudget() - (actualStock > 0.0f ? actualStock : 0.0f));
						LOG.info("Computed new purchase budget : " + product.getPurchaseBudget());

						// Applying new sale rate to product
						product.setSaleRate(saleRate);

						// Saving changes
						productService.update(product);

						final InventoryBean toPurchase = new InventoryBean();
						toPurchase.setPiecePurchasePrice(inventory.getPiecePurchasePrice());
						toPurchase.setPieceUnit(inventory.getPieceUnit());
						toPurchase.setWholePurchasePrice(inventory.getWholePurchasePrice());
						toPurchase.setWholeUnit(inventory.getWholeUnit());
						toPurchase.setStockBudget(product.getPurchaseBudget());
						toPurchase.setProduct(product);
						generatedProductPO.setToPurchase(toPurchase);

						generatedProductPOs.add(generatedProductPO);
					}

					// Computing expected delivery date (30% of previous days
					// booked)
					Calendar expectedDeliveryDate = Calendar.getInstance();
					int maxDaysToDeliver = company.getDaysBooked().equals(0.0f) ? 2
							: (int) Math.floor(company.getDaysBooked() * 0.30f);
					expectedDeliveryDate.add(Calendar.DAY_OF_MONTH, maxDaysToDeliver);

					// Applying changes to company
					company.setDaysBooked(daysToBook);
					company.setLastPurchaseOrderDate(generateStartTime);

					// Saving changes
					companyService.update(company);

					// Generate pdf file of generated purchase order
					final String fileName = StringHelper.convertToFileSafeFormat(company.getName()) + "_purchase.order_"
							+ DateFormatter.fileSafeShortFormat(new Date()) + ".pdf";
					final String filePath = fileConstants.getGeneratePurchasesHome() + fileName;
					final String temp = new GeneratedPurchaseTemplate(company.getName(), lastPODate.getTime(),
							generateStartTime, expectedDeliveryDate.getTime(), daysToBook, generatedProductPOs)
									.merge(velocityEngine);
					SimplePdfWriter.write(temp, "Ever Bazar", filePath, true);
					final Map<String, Object> extras = new HashMap<String, Object>();
					extras.put("fileName", fileName);
					result = new ResultBean(Boolean.TRUE, "Done");
					result.setExtras(extras);
				} else {
					result = new ResultBean(Boolean.FALSE,
							Html.line("Last purchase order was generated on "
									+ DateFormatter.longFormat(company.getLastPurchaseOrderDate())
									+ ". You can generate another after a minimum of 3 days have passed."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line("You must book for at least " + Html.text(Color.BLUE, "3 days.")));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select a company."));
		}

		return result;
	}

	@Override
	public ResultBean generateOfftake(Long companyId, Float offtakeDays, Boolean download, Boolean print) {
		final ResultBean result;
		final Company company = companyService.find(companyId);

		if (company != null) {
			final List<GeneratedOfftakeBean> generatedOfftakes = new ArrayList<GeneratedOfftakeBean>();
			final List<Product> products = productService.findAllByCompanyOrderByName(companyId);

			// average 3x of the offtake days ex. 7 days -> average of past 21
			// days (limit of 28 days)
			final Integer daysToAverage = (int) Math.ceil(offtakeDays * 3 > 28 ? 28.0 : offtakeDays * 3.0);
			// System.out.println("Days to average used : " + daysToAverage);

			for (Product product : products) {
				final Double averageOfftake = (transactionSummaryHandler
						.getProductDailySalesSummaryList(product.getId(), daysToAverage).stream()
						.mapToDouble(pdss -> pdss.getBaseTotal()).sum()) / ((float) daysToAverage);
				final ProductDetail wholeDetail = productDetailService.findByProductIdAndTitle(product.getId(),
						"Whole");
				final GeneratedOfftakeBean generatedOfftake = new GeneratedOfftakeBean();
				generatedOfftake.setProductId(product.getId());
				generatedOfftake.setProductName(product.getName());
				generatedOfftake.setCategoryName(product.getCategory().getName());
				generatedOfftake
						.setProductDisplayName((product.getDisplayName() != null && !product.getDisplayName().isEmpty())
								? product.getDisplayName() : product.getName());
				generatedOfftake.setProductWholeUnit(wholeDetail.getUnitType());
				generatedOfftake.setOfftake((float) (averageOfftake / wholeDetail.getNetPrice() * offtakeDays));
				generatedOfftakes.add(generatedOfftake);
			}

			generatedOfftakes.sort((GeneratedOfftakeBean go1, GeneratedOfftakeBean go2) -> go2.getSuggestedOrder()
					.compareTo(go1.getSuggestedOrder()));
			generatedOfftakes.sort((GeneratedOfftakeBean go1, GeneratedOfftakeBean go2) -> go2.getCategoryName()
					.compareTo(go1.getCategoryName()));

			/*
			 * for(GeneratedOfftakeBean generatedOfftake : generatedOfftakes) {
			 * System.out.println(generatedOfftake.getProductName() + " - " +
			 * generatedOfftake.getOfftake() + " - " +
			 * generatedOfftake.getSuggestedOrder() + " " +
			 * generatedOfftake.getProductWholeUnit().getShorthand()); }
			 */

			if (download) {
				// Generate pdf file of generated purchase order
				final String fileName = StringHelper.convertToFileSafeFormat(company.getName()) + "_average.offtake_"
						+ DateFormatter.fileSafeShortFormat(new Date()) + ".pdf";
				final String filePath = fileConstants.getGenerateOfftakeHome() + fileName;
				final String temp = new GeneratedOfftakeTemplate(company.getName(), offtakeDays, generatedOfftakes)
						.merge(velocityEngine);
				SimplePdfWriter.write(temp, "Ever Bazar", filePath, false);
				final Map<String, Object> extras = new HashMap<String, Object>();
				extras.put("fileName", fileName);
				result = new ResultBean(Boolean.TRUE, "Done");
				result.setExtras(extras);
			} else {
				result = new ResultBean(Boolean.TRUE, "Done");
			}

			if (print) {
				final List<GeneratedOfftakeBean> relevantOfftakes = new ArrayList<GeneratedOfftakeBean>();
				for (GeneratedOfftakeBean generatedOfftake : generatedOfftakes) {
					if (generatedOfftake.getSuggestedOrder() > 1)
						relevantOfftakes.add(generatedOfftake);
				}

				Printer printer = new Printer();

				final GeneratedOfftakeTemplate got = new GeneratedOfftakeTemplate(company.getName(), offtakeDays,
						relevantOfftakes);

				try {
					printer.print(got.merge(velocityEngine, DocType.PRINT), "Offtake",
							PrintConstants.EVER_ACCOUNTING_PRINTER, 11);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("Please select a company."));
		}

		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 3)
	public ResultBean removePurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;

		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		if (purchaseOrder != null) {
			final Map<String, String> errors = purchaseOrderFormValidator
					.validateDeliveryDateWithCompanyLastPurchaseDate(purchaseOrder.getDeliveredOn(),
							purchaseOrder.getCompany());
			if (purchaseOrder.getStatus().equals(Status.LISTING) && errors.isEmpty()) {
				result = new ResultBean();

				purchaseOrder.setStatus(Status.CANCELLED);

				result.setSuccess(purchaseOrderService.delete(purchaseOrder));
				if (result.getSuccess()) {
					result.setMessage("Successfully removed Purchase order \"" + purchaseOrder.getId() + "("
							+ purchaseOrder.getCompany().getName() + ")\".");
				} else {
					result.setMessage("Failed to remove Purchase order \"" + purchaseOrder.getId() + " of "
							+ purchaseOrder.getCompany().getName() + "\".");
				}
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line(Html.text(Color.RED, "Declined!") + " Processed purchase order cannot be deleted."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "Error please refresh the page.");
		}

		return result;
	}

	@Override
	public void refreshPurchaseOrder(Long purchaseOrderId) {
		this.refreshPurchaseOrder(purchaseOrderService.find(purchaseOrderId));
	}

	private void refreshPurchaseOrder(PurchaseOrder purchaseOrder) {
		float totalAmount = 0l;
		int totalItems = 0;

		List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrderDetailService
				.findAllByPurchaseOrderId(purchaseOrder.getId());

		for (PurchaseOrderDetail purchaseOrderDetail : purchaseOrderDetails) {
			totalAmount += purchaseOrderDetail.getTotalPrice();
			totalItems += purchaseOrderDetail.getQuantity();
		}

		purchaseOrder.setTotalAmount(totalAmount);
		purchaseOrder.setTotalItems(totalItems);
		purchaseOrderService.update(purchaseOrder);
	}

	@Override
	public ObjectList<PurchaseOrderDetail> getPurchaseOrderDetailList(Integer pageNumber, Long purchaseOrderId) {
		return purchaseOrderDetailService.findAllWithPagingOrderByLastUpdate(pageNumber,
				UserContextHolder.getItemsPerPage(), purchaseOrderId);
	}

	@Override
	public ResultBean addItemByProductDetailId(Long productDetailId, Long purchaseOrderId, Integer quantity) {
		final ResultBean result;

		final ProductDetail productDetail = productDetailService.find(productDetailId);
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);

		if (purchaseOrder != null) {
			if (purchaseOrder.getStatus().equals(Status.LISTING)) {
				if (productDetail != null) {
					result = this.addItem(productDetail, purchaseOrder, quantity);
				} else {
					result = new ResultBean(false, "Product not found.");
				}
			} else {
				result = new ResultBean(false, "Purchase order cannot be edited right now.");
			}
		} else {
			result = new ResultBean(false, "Purchase order not found.");
		}

		return result;
	}

	private ResultBean addItem(ProductDetail productDetail, PurchaseOrder purchaseOrder, Integer quantity) {
		final ResultBean result;

		final PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailService
				.findByOrderAndDetailId(purchaseOrder.getId(), productDetail.getId());

		final Map<String, String> errors = purchaseOrderFormValidator.validateDeliveryDateWithCompanyLastPurchaseDate(
				purchaseOrder.getDeliveredOn(), purchaseOrder.getCompany());
		if (errors.isEmpty()) {
			if (purchaseOrderDetail == null) {
				if (purchaseOrder.getCompany().getId().equals(productDetail.getProduct().getCompany().getId())) {
					result = new ResultBean();

					final PurchaseOrderDetail newPurchaseOrderDetail = new PurchaseOrderDetail();
					setPurchaseOrderDetail(newPurchaseOrderDetail, purchaseOrder, productDetail);

					result.setSuccess(purchaseOrderDetailService.insert(newPurchaseOrderDetail) != null
							&& this.changePurchaseOrderDetailQuantity(newPurchaseOrderDetail, quantity).getSuccess());

					if (result.getSuccess()) {
						result.setMessage("Successfully added item.");
					} else {
						result.setMessage("Failed to add item.");
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Error! ")
							+ Html.text(Color.TURQUOISE, productDetail.getProduct().getName()) + " is not a product of "
							+ Html.text(Color.TURQUOISE, purchaseOrder.getCompany().getName()) + "."));
				}
			} else {
				result = this.changePurchaseOrderDetailQuantity(purchaseOrderDetail,
						purchaseOrderDetail.getQuantity() + quantity);
			}
		} else {
			result = new ResultBean(Boolean.FALSE,
					Html.line(Html.text(Color.RED, "Declined!") + " Processed purchase order cannot be edited."));
		}

		return result;
	}

	@Override
	public ResultBean addItemByBarcode(String barcode, Long purchaseOrderId) {
		final ResultBean result;

		if (barcode != null && barcode.length() > 4) {
			final ProductDetail productDetail;
			String[] temp = barcode.split("\\*|;");
			if (temp.length == 2) {
				productDetail = productDetailService.findByBarcode(temp[1]);
			} else if (temp.length == 1) {
				productDetail = productDetailService.findByBarcode(temp[0]);
			} else {
				productDetail = null;
			}

			if (productDetail != null) {
				if (temp.length == 2) {
					result = this.addItemByProductDetailId(productDetail.getId(), purchaseOrderId,
							Integer.parseInt(temp[0]));
				} else if (temp.length == 1) {
					result = this.addItemByProductDetailId(productDetail.getId(), purchaseOrderId, 1);
				} else {
					result = new ResultBean(false, "Invalid barcode.");
				}
			} else {
				result = new ResultBean(false, "Barcode not found.");
			}
		} else {
			result = new ResultBean(false, "Invalid barcode.");
		}

		return result;
	}

	@Override
	public ResultBean removePurchaseOrderDetail(Long purchaseOrderDetailId) {
		final ResultBean result;
		final PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailService.find(purchaseOrderDetailId);

		if (purchaseOrderDetail != null) {
			final PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();
			if (purchaseOrder != null) {
				if (purchaseOrder.getStatus().equals(Status.LISTING)) {
					result = this.removePurchaseOrderDetail(purchaseOrderDetail);
				} else {
					result = new ResultBean(false, "Purchase order cannot be edited right now.");
				}
			} else {
				result = new ResultBean(false, "Purchase order not found.");
			}
		} else {
			result = new ResultBean(false, "Item not found.");
		}

		return result;
	}

	private ResultBean removePurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
		final ResultBean result;

		final PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();

		final Map<String, String> errors = purchaseOrderFormValidator.validateDeliveryDateWithCompanyLastPurchaseDate(
				purchaseOrder.getDeliveredOn(), purchaseOrder.getCompany());
		if (errors.isEmpty()) {
			result = new ResultBean();

			purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() - purchaseOrderDetail.getTotalPrice());
			purchaseOrder.setTotalItems(purchaseOrder.getTotalItems() - purchaseOrderDetail.getQuantity());
			result.setSuccess(purchaseOrderDetailService.erase(purchaseOrderDetail));
			if (result.getSuccess()) {
				purchaseOrderService.update(purchaseOrder);
				result.setMessage("Successfully removed item \"" + purchaseOrderDetail.getProductName() + " ("
						+ purchaseOrderDetail.getUnitType() + ")\".");
			} else {
				result.setMessage("Failed to remove item \"" + purchaseOrderDetail.getProductName() + " ("
						+ purchaseOrderDetail.getUnitType() + ")\".");
			}
		} else {
			result = new ResultBean(Boolean.FALSE,
					Html.line(Html.text(Color.RED, "Declined!") + " Processed purchase order cannot be edited."));
		}

		return result;
	}

	@Override
	public ResultBean changePurchaseOrderDetailQuantity(Long purchaseOrderDetailId, Integer quantity) {
		final ResultBean result;

		final PurchaseOrderDetail purchaseOrderDetail = purchaseOrderDetailService.find(purchaseOrderDetailId);

		if (purchaseOrderDetail != null) {
			final PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();
			if (purchaseOrder != null) {
				if (purchaseOrder.getStatus().equals(Status.LISTING)) {
					result = this.changePurchaseOrderDetailQuantity(purchaseOrderDetail, quantity);
				} else {
					result = new ResultBean(false, "Purchase order cannot be edited right now.");
				}
			} else {
				result = new ResultBean(false, "Purchase order not found.");
			}
		} else {
			result = new ResultBean(false, "Purchase order detail not found.");
		}

		return result;
	}

	private ResultBean changePurchaseOrderDetailQuantity(PurchaseOrderDetail purchaseOrderDetail, Integer quantity) {
		final ResultBean result;

		if (quantity != null && quantity != 0) {
			// Limit maximum quantity to 999
			if (quantity > 999) {
				quantity = 999;
			}

			final PurchaseOrder purchaseOrder = purchaseOrderDetail.getPurchaseOrder();

			final Map<String, String> errors = purchaseOrderFormValidator
					.validateDeliveryDateWithCompanyLastPurchaseDate(purchaseOrder.getDeliveredOn(),
							purchaseOrder.getCompany());
			if (errors.isEmpty()) {
				result = new ResultBean();

				purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() - purchaseOrderDetail.getTotalPrice());
				purchaseOrder.setTotalItems(purchaseOrder.getTotalItems() - purchaseOrderDetail.getQuantity());

				setPurchaseOrderDetailQuantity(purchaseOrderDetail, quantity);
				result.setSuccess(purchaseOrderDetailService.update(purchaseOrderDetail));

				if (result.getSuccess()) {
					purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() + purchaseOrderDetail.getTotalPrice());
					purchaseOrder.setTotalItems(purchaseOrder.getTotalItems() + purchaseOrderDetail.getQuantity());
					purchaseOrderService.update(purchaseOrder);

					result.setMessage("Quantity successfully updated.");
				} else {
					result.setMessage("Failed to update quantity.");
				}
			} else {
				result = new ResultBean(Boolean.FALSE,
						Html.line(Html.text(Color.RED, "Declined!") + " Processed purchase order cannot be edited."));
			}
		} else {
			result = this.removePurchaseOrderDetail(purchaseOrderDetail);
		}

		return result;
	}

	@Override
	public ResultBean checkPurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);

		if (purchaseOrder != null) {
			if (purchaseOrder.getStatus() != Status.CHECKED && purchaseOrder.getStatus() != Status.CANCELLED) {
				result = new ResultBean();

				purchaseOrder.setManagerInCharge(userService.find(UserContextHolder.getUser().getId()));
				purchaseOrder.setStatus(Status.CHECKED);
				purchaseOrder.setCheckedOn(new Date());

				result.setSuccess(purchaseOrderService.update(purchaseOrder));
				if (result.getSuccess()) {
					// #############################################################################################
					// add to stock!!!

					result.setMessage("Successfully checked Purchase order \"" + purchaseOrder.getId() + "\".");
				} else {
					result.setMessage("Failed to check Purchase order \"" + purchaseOrder.getId() + "\".");
				}
			} else {
				result = new ResultBean(false, "Purchase order already checked or cancelled.");
			}
		} else {
			result = new ResultBean(false, "Purchase order not found.");
		}

		return result;
	}

	@Override
	public ResultBean printPurchaseOrderCopy(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);

		if (purchaseOrder != null) {
			final UserBean currentUser = UserContextHolder.getUser();

			if (purchaseOrder.getStatus().equals(Status.LISTING) || currentUser.getUserType().getAuthority() <= 3) {
				final List<PurchaseOrderDetail> purchaseOrderItems = purchaseOrderDetailService
						.findAllByPurchaseOrderIdOrderByProductName(purchaseOrder.getId());
				final PurchaseOrderCopyTemplate purchaseOrderCopy = new PurchaseOrderCopyTemplate(purchaseOrder,
						purchaseOrderItems);

				Printer printer = new Printer();
				try {
					printer.print(purchaseOrderCopy.merge(velocityEngine, DocType.PRINT),
							"Purchase Order #" + purchaseOrder.getId() + " (COPY)",
							PrintConstants.EVER_ACCOUNTING_PRINTER);
				} catch (Exception e) {
					e.printStackTrace();
				}

				result = new ResultBean(Boolean.TRUE,
						"Successfully printed Purchase order \"" + purchaseOrder.getId() + "\".");
			} else {
				result = new ResultBean(false, "You are not authorized to print a copy.");
			}
		} else {
			result = new ResultBean(false, "Purchase order not found.");
		}

		return result;
	}

	private void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail, PurchaseOrder purchaseOrder,
			ProductDetail productDetail) {
		purchaseOrderDetail.setPurchaseOrder(purchaseOrder);
		purchaseOrderDetail.setProductDetail(productDetail);
		purchaseOrderDetail.setProduct(productDetail.getProduct());
		purchaseOrderDetail.setProductName(productDetail.getProduct().getName());
		purchaseOrderDetail.setProductCode(productDetail.getProduct().getCode());
		purchaseOrderDetail.setUnitType(productDetail.getUnitType());
		purchaseOrderDetail.setGrossPrice(productDetail.getGrossPrice());
		purchaseOrderDetail.setNetPrice(productDetail.getNetPrice());
		purchaseOrderDetail.setQuantity(0);
		purchaseOrderDetail.setTotalPrice(0.0f);
	}

	private void setPurchaseOrderDetailQuantity(PurchaseOrderDetail purchaseOrderDetail, int quantity) {
		purchaseOrderDetail.setQuantity(quantity);
		purchaseOrderDetail.setTotalPrice(quantity * purchaseOrderDetail.getNetPrice());
	}
}