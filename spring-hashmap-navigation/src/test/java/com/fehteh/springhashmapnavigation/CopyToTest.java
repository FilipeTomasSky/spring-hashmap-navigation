package com.fehteh.springhashmapnavigation;

import com.fehteh.springhashmapnavigation.navigation.NavigationService;
import com.fehteh.springhashmapnavigation.transformer.CopyTo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CopyToTest {
	@Test
	void copyToBubbleUpField() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.productCount.count", new CopyTo("../../newField"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, String> productCount = (Map<String, String>) metadata.get("productCount");
		assertNotNull(productCount);
		assertEquals(2, productCount.get("count"));

		assertEquals(2, metadata.get("newField"));
	}

	@Test
	void copyToBubbleDownField() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.productCount.count", new CopyTo("../newField/field"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, Object> productCount = (Map<String, Object>) metadata.get("productCount");
		Map<String, Object> newField = (Map<String, Object>) productCount.get("newField");

		boolean field = (boolean) newField.get("field");
		assertTrue(field);
	}

	@Test
	void copyToBubbleUpAndDownArrayExistingPathArrayInArray() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.offers", new CopyTo("../../relevantContext/newOffers"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");

		Map<String, Object> relevantContext = (Map<String, Object>) products.get(0).get("relevantContext");
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(1).get("relevantContext");
		Map<String, Object> relevantContext2 = (Map<String, Object>) products.get(2).get("relevantContext");

		String duration = (String) relevantContext.get("duration");
		assertNotNull(duration); // check if didn't overwrite

		ArrayList<Map<String, Object>> offers = (ArrayList<Map<String, Object>>) relevantContext.get("offers");
		assertNotNull(offers);

		ArrayList<Map<String, Object>> offers1 = (ArrayList<Map<String, Object>>) relevantContext1.get("offers");
		assertNotNull(offers1);

		ArrayList<Map<String, Object>> offers2 = (ArrayList<Map<String, Object>>) relevantContext2.get("offers");
		assertNull(offers2);

		ArrayList<Map<String, Object>> newOffers = (ArrayList<Map<String, Object>>) relevantContext.get("newOffers");
		Map<String, Object> staticId = (Map<String, Object>) newOffers.get(0).get("staticId");
		assertEquals("D2C_OFFER_MONTH", staticId);

		ArrayList<Map<String, Object>> newOffers1 = (ArrayList<Map<String, Object>>) relevantContext1.get("newOffers");
		Map<String, Object> staticId1 = (Map<String, Object>) newOffers1.get(0).get("staticId");
		assertEquals("D2C_OFFER_MONTH", staticId1);
		Map<String, Object> staticId1_1 = (Map<String, Object>) newOffers1.get(1).get("staticId");
		assertEquals("D2C_OFFER_DAY_2", staticId1_1);

		ArrayList<Map<String, Object>> newOffers2 = (ArrayList<Map<String, Object>>) relevantContext2.get("newOffers");
		assertNull(newOffers2);

	}

}
