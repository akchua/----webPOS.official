package com.chua.evergrocery.database.service.impl;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import com.chua.evergrocery.database.dao.CashTransferDAO;import com.chua.evergrocery.database.entity.CashTransfer;import com.chua.evergrocery.database.service.CashTransferService;/** * @author  Adrian Jasper K. Chua * @version 1.0 * @since   30 June 2018 */@Servicepublic class CashTransferServiceImpl		extends AbstractService<CashTransfer, Long, CashTransferDAO> 		implements CashTransferService {	@Autowired	protected CashTransferServiceImpl(CashTransferDAO dao) {		super(dao);	}}
