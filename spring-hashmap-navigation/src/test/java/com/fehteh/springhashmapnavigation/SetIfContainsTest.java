package com.fehteh.springhashmapnavigation;

import com.fehteh.springhashmapnavigation.navigation.NavigationService;
import com.fehteh.springhashmapnavigation.transformer.SetIfContains;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SetIfContainsTest {
	@Test
	void setIfContainsBubbleUpField() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.productCount.count", new SetIfContains("../../newField", 2, true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, String> productCount = (Map<String, String>) metadata.get("productCount");
		assertNotNull(productCount);
		assertEquals(2, productCount.get("count"));

		boolean newField = (boolean) metadata.get("newField");
		assertTrue(newField);
	}

	@Test
	void setIfContainsBubbleDownField() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.productCount.count", new SetIfContains("../newField/field", 2, true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, Object> productCount = (Map<String, Object>) metadata.get("productCount");
		Map<String, Object> newField = (Map<String, Object>) productCount.get("newField");

		boolean field = (boolean) newField.get("field");
		assertTrue(field);
	}

	@Test
	void setIfContainsBubbleUpAndDownArrayExistingPathArrayInArray() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.offers.staticId", new SetIfContains("../../../relevantContext/newField/D2C_OFFER_MONTH", "D2C_OFFER_MONTH",true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");

		Map<String, Object> relevantContext = (Map<String, Object>) products.get(0).get("relevantContext");
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(1).get("relevantContext");

		String duration = (String) relevantContext.get("duration");
		assertNotNull(duration); // check if didn't overwrite

		ArrayList<Map<String, Object>> offers = (ArrayList<Map<String, Object>>) relevantContext.get("offers");
		String staticId = (String) offers.get(0).get("staticId");
		assertEquals("D2C_OFFER_MONTH", staticId);

		Map<String, Object> newField = (Map<String, Object>) relevantContext.get("newField");
		Map<String, Object> newField1 = (Map<String, Object>) relevantContext1.get("newField");

		boolean field = (boolean) newField.get("D2C_OFFER_MONTH");
		boolean field1 = (boolean) newField1.get("D2C_OFFER_MONTH");

		assertTrue(field);
		assertTrue(field1);
	}

}
