package com.chua.evergrocery.utility.template;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.chua.evergrocery.beans.GeneratedOfftakeBean;
import com.chua.evergrocery.enums.DocType;
import com.chua.evergrocery.utility.StringHelper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Oct 25, 2019
 */
public class GeneratedOfftakeItemTemplate extends AbstractTemplate {

	private GeneratedOfftakeBean generatedOfftake;
	
	private List<String> overflowList;
	
	private final Integer ITEM_NAME_MAX_LENGTH = 35;
	
	private final Integer MAX_LINE_SPLIT_ADJUST = 7;
	
	public GeneratedOfftakeItemTemplate(GeneratedOfftakeBean generatedOfftake) {
		this.generatedOfftake = generatedOfftake;
		
		this.overflowList = new ArrayList<String>();
	}
	
	@Override
	public String merge(VelocityEngine velocityEngine, DocType docType) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("t", this);
		return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, docType.getFolderName() + "/generatedOfftakeItem.vm", "UTF-8", model);
	}
	
	//62
	public String getFormattedProductName() {
		return StringHelper.center(generatedOfftake.getProductName(), 62);
	}
	
	public String getFormattedOrder() {
		final String formattedOrder = generatedOfftake.getSuggestedOrder() + " " + generatedOfftake.getProductWholeUnit().getDisplayName();
		
		return String.format("%-13s", formattedOrder);
	}
	
	public String getFormattedOfftake() {
		DecimalFormat df = new DecimalFormat("0.0000");
		return String.format("%-13s", df.format(generatedOfftake.getOfftake()));
	}
	
	public String getFormattedQuantity() {
		NumberFormat nf = new DecimalFormat("##.#");
		return String.format("%-4s", nf.format(generatedOfftake.getSuggestedOrder()));
	}
	
	public String getFormattedName() {
		String formattedName = generatedOfftake.getProductWholeUnit().getShorthand() + " ";
		
		formattedName += generatedOfftake.getProductDisplayName();
		
		int index = ITEM_NAME_MAX_LENGTH;
		int splits = 1;
		int firstSplitIndex = ITEM_NAME_MAX_LENGTH;
		while (index < formattedName.length()) {
			int adjustedIndex = index;
			int i = 0;
			// try to find a white space up to 'MAX_LINE_SPLIT_ADJUST' characters back
			while(formattedName.charAt(adjustedIndex) != ' ' && i < MAX_LINE_SPLIT_ADJUST) {
				adjustedIndex--;
				i++;
			}
			// reset index if exceeded max line split and no white space found
			if(i == MAX_LINE_SPLIT_ADJUST && formattedName.charAt(adjustedIndex) != ' ') adjustedIndex = index;
			else adjustedIndex++;
			
			// record the first split
			if(splits == 1) firstSplitIndex = adjustedIndex;
			
			// +1 to not include the space on the next line
			overflowList.add("       " + formattedName.substring(adjustedIndex, Math.min(adjustedIndex + ITEM_NAME_MAX_LENGTH, formattedName.length())));
		    index = adjustedIndex + ITEM_NAME_MAX_LENGTH;
		    splits++;
		}
		formattedName = formattedName.substring(0, Math.min(firstSplitIndex, formattedName.length()));
		
		return String.format("%-" + ITEM_NAME_MAX_LENGTH + "s", formattedName.trim());
	}
	
	public Boolean isOverflow() {
		return !overflowList.isEmpty();
	}
	
	public List<String> getOverflowList() {
		return overflowList;
	}
}
