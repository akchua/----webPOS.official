package com.chua.evergrocery.rest.handler.impl;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.evergrocery.constants.FileConstants;
import com.chua.evergrocery.database.entity.Category;
import com.chua.evergrocery.database.entity.SystemVariable;
import com.chua.evergrocery.database.entity.User;
import com.chua.evergrocery.database.entity.XReading;
import com.chua.evergrocery.database.entity.ZReading;
import com.chua.evergrocery.database.service.CategoryService;
import com.chua.evergrocery.database.service.SystemVariableService;
import com.chua.evergrocery.database.service.UserService;
import com.chua.evergrocery.database.service.XReadingService;
import com.chua.evergrocery.database.service.ZReadingService;
import com.chua.evergrocery.enums.SystemVariableTag;
import com.chua.evergrocery.enums.UserType;
import com.chua.evergrocery.rest.handler.DatabaseInitHandler;
import com.chua.evergrocery.utility.DateUtil;
import com.chua.evergrocery.utility.EncryptionUtil;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   22 Mar 2019
 */
@Transactional
@Component
public class DatabaseInitHandlerImpl implements DatabaseInitHandler {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	@Autowired
	private SystemVariableService systemVariableService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ZReadingService zReadingService;
	
	@Autowired
	private XReadingService xReadingService;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		final Date start = new Date();
		LOG.info("Initializing database ...");
		initializeDatabase();
		final Float seconds = (new Date().getTime() - start.getTime()) / 1000.0f;
		LOG.info("Database init completed in " + seconds + "s");
	}
	
	private void initializeDatabase() {
		this.initializeUser();
		this.initializeSystemVariables();
		this.initializeCategory();
		this.initializeZReading();
		this.initializeXReading();
	}
	
	private void initializeUser() {
		final User rootUser = userService.findByUsernameAndPassword("root", EncryptionUtil.getMd5("root"));
		if(rootUser == null) {
			final User newRootUser = new User();
			newRootUser.setUsername("root");
			newRootUser.setFirstName("root");
			newRootUser.setLastName("user");
			newRootUser.setEmailAddress("everbazar.noreply@gmail.com");
			newRootUser.setContactNumber("(077) 772-01-64");
			newRootUser.setUserType(UserType.ADMINISTRATOR);
			newRootUser.setImage(fileConstants.getImageDefaultFileName());
			newRootUser.setPassword(EncryptionUtil.getMd5("root"));
			newRootUser.setLastSuccessfulLogin(DateUtil.getDefaultDate());
			newRootUser.setItemsPerPage(10);
			userService.insert(newRootUser);
			
			LOG.info("Created root user");
		} else {
			LOG.info("Root user exists");
		}
	}
	
	private void initializeSystemVariables() {
		final String SIN = systemVariableService.findByTag(SystemVariableTag.SERIAL_INVOICE_NUMBER.getTag());
		if(SIN == null || SIN.isEmpty()) {
			final SystemVariable newSIN = new SystemVariable();
			newSIN.setTag(SystemVariableTag.SERIAL_INVOICE_NUMBER.getTag());
			newSIN.setContent("1");
			systemVariableService.insert(newSIN);
			LOG.info("Created system variable SIN");
		} else {
			LOG.info("SIN value : "  + SIN);
		}
		
		final String RN = systemVariableService.findByTag(SystemVariableTag.REFUND_NUMBER.getTag());
		if(RN == null || RN.isEmpty()) {
			final SystemVariable newRN = new SystemVariable();
			newRN.setTag(SystemVariableTag.REFUND_NUMBER.getTag());
			newRN.setContent("1");
			systemVariableService.insert(newRN);
			LOG.info("Created system variable RN");
		} else {
			LOG.info("RN value : "  + RN);
		}
	}
	
	private void initializeCategory() {
		if(!categoryService.isExistsByName("Cigarette")) {
			final Category category = new Category();
			category.setName("Cigarette");
			categoryService.insert(category);
			LOG.info("Created Cigarette category");
		} else {
			LOG.info("Cigarette category exists");
		}
		
		if(!categoryService.isExistsByName("Liquors")) {
			final Category category = new Category();
			category.setName("Liquors");
			categoryService.insert(category);
			LOG.info("Created Liquors category");
		} else {
			LOG.info("Liquors category exists");
		}
		
		if(!categoryService.isExistsByName("Counter Item")) {
			final Category category = new Category();
			category.setName("Counter Item");
			categoryService.insert(category);
			LOG.info("Created Counter Item category");
		} else {
			LOG.info("Counter Item category exists");
		}
		
		if(!categoryService.isExistsByName("Medicine")) {
			final Category category = new Category();
			category.setName("Medicine");
			categoryService.insert(category);
			LOG.info("Created Medicine category");
		} else {
			LOG.info("Medicine category exists");
		}
	}
	
	private void initializeZReading() {
		final Boolean zReadingExists = zReadingService.findAllList().size() > 0;
		if(zReadingExists) {
			LOG.info("Z Reading Exists");
		} else {
			final ZReading zReading = new ZReading();
			final Calendar yesterday = Calendar.getInstance();
			yesterday.add(Calendar.DAY_OF_MONTH, -3);
			zReading.setReadingDate(DateUtil.floorDay(yesterday.getTime()));
			zReading.setCounter(0l);
			zReading.setBeginningSIN(0l);
			zReading.setEndingSIN(0l);
			zReading.setZeroRatedRemovedVat(0.0f);
			zReading.setBeginningRefundNumber(0l);
			zReading.setEndingRefundNumber(0l);
			zReading.setBeginningBalance(0.0f);
			zReading.setVatSales(0.0f);
			zReading.setVatExSales(0.0f);
			zReading.setZeroRatedSales(0.0f);
			zReading.setVatDiscount(0.0f);
			zReading.setVatExDiscount(0.0f);
			zReading.setZeroRatedDiscount(0.0f);
			zReading.setRegularDiscountAmount(0.0f);
			zReading.setSeniorDiscountAmount(0.0f);
			zReading.setPwdDiscountAmount(0.0f);
			zReading.setBeginningRefundAmount(0.0f);
			zReading.setRefundAmount(0.0f);
			zReading.setTotalCheckPayment(0.0f);
			zReading.setTotalCardPayment(0.0f);
			zReading.setTotalPointsPayment(0.0f);
			zReadingService.insert(zReading);
			LOG.info("Created 0th Z Reading");
		}
	}
	
	private void initializeXReading() {
		final Boolean xReadingExists = xReadingService.findAllList().size() > 0;
		if(xReadingExists) {
			LOG.info("X Reading Exists");
		} else {
			final Calendar yesterday = Calendar.getInstance();
			yesterday.add(Calendar.DAY_OF_MONTH, -1);
			
			final XReading xReading = new XReading();
			xReading.setReadingDate(yesterday.getTime());
			xReading.setCashier(userService.find(1l));
			xReading.setZeroRatedRemovedVat(0.0f);
			xReading.setBeginningBalance(0.0f);
			xReading.setNetSales(0.0f);
			xReading.setRegularDiscountAmount(0.0f);
			xReading.setSeniorDiscountAmount(0.0f);
			xReading.setPwdDiscountAmount(0.0f);
			xReading.setRefundAmount(0.0f);
			xReading.setTotalCheckPayment(0.0f);
			xReading.setTotalCardPayment(0.0f);
			xReading.setTotalPointsPayment(0.0f);
			xReadingService.insert(xReading);
			LOG.info("Created 0th X Reading");
		}
	}
}