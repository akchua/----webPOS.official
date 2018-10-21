package com.chua.evergrocery.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.evergrocery.database.dao.CompanyMTDPurchaseSummaryDAO;
import com.chua.evergrocery.database.entity.CompanyMTDPurchaseSummary;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 19, 2018
 */
@Repository
public class CompanyMTDPurchaseSummaryDAOImpl 
		extends AbstractDAO<CompanyMTDPurchaseSummary, Long>
		implements CompanyMTDPurchaseSummaryDAO {

	@Override
	public CompanyMTDPurchaseSummary findByCompanyAndMonthId(long companyId, int monthId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		conjunction.add(Restrictions.eq("monthId", monthId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<CompanyMTDPurchaseSummary> findAllByCompanyWithOrder(Long companyId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}
}
