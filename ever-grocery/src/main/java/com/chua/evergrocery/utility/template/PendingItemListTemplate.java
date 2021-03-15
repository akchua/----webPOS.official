package com.chua.evergrocery.utility.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.database.entity.CustomerOrderDetail;
import com.chua.evergrocery.enums.DocType;

/**
 * @author Adrian Jasper K. Chua
 * @version 1.0
 * @since Mar 15, 2021
 */
public class PendingItemListTemplate extends AbstractTemplate {

	private List<CustomerOrderDetail> pendingItems;

	private List<String> formattedPendingItems;

	public PendingItemListTemplate(List<CustomerOrderDetail> customerOrderItems) {
		this.pendingItems = customerOrderItems;
		this.formattedPendingItems = new ArrayList<String>();
	}

	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		for (CustomerOrderDetail pendingItem : pendingItems) {
			final PendingItemTemplate pendingItemTemplate = new PendingItemTemplate(pendingItem);
			formattedPendingItems.add(pendingItemTemplate.merge(velocityEngine, docType));
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				docType.getFolderName() + "/pendingItemList.vm", "UTF-8", model);
	}

	public List<String> getFormattedPendingItems() {
		return formattedPendingItems;
	}
}
