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
		navigationService.navigateAndApplyRecursive(struct, "metadata.productCount.count", new CopyTo("../../newField"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, String> productCount = (Map<String, String>) metadata.get("productCount");
		assertNotNull(productCount);
		assertEquals(2, productCount.get("count"));

		assertEquals(2, metadata.get("newField"));
	}

	@Test
	void copyToBubbleUpObject() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApplyRecursive(struct, "metadata.productCount", new CopyTo("../newField"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, String> productCount = (Map<String, String>) metadata.get("productCount");
		assertNotNull(productCount);
		assertEquals(2, productCount.get("count"));

		Map<String, Object> newField = (Map<String, Object>) metadata.get("newField");
		assertEquals(2, newField.get("count"));
	}

	@Test
	void copyToBubbleDownField() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApplyRecursive(struct, "metadata.productCount.count", new CopyTo("../newField/field"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, Object> productCount = (Map<String, Object>) metadata.get("productCount");
		Map<String, Object> newField = (Map<String, Object>) productCount.get("newField");

		int field = (Integer) newField.get("field");
		assertEquals(2,field);
	}

	@Test
	void copyToBubbleUpAccumulateArray() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApplyRecursive(struct, "metadata.products.relevantContext.offers", new CopyTo("../../../newOffers"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");

		Map<String, Object> relevantContext = (Map<String, Object>) products.get(0).get("relevantContext");
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(1).get("relevantContext");
		Map<String, Object> relevantContext2 = (Map<String, Object>) products.get(2).get("relevantContext");

		String duration = (String) relevantContext.get("duration");
		assertNotNull(duration); // check if didn't overwrite

		// product 1 has 1 offer
		ArrayList<Map<String, Object>> offers = (ArrayList<Map<String, Object>>) relevantContext.get("offers");
		assertNotNull(offers);

		// product 2 has 2 offers
		ArrayList<Map<String, Object>> offers1 = (ArrayList<Map<String, Object>>) relevantContext1.get("offers");
		assertNotNull(offers1);

		// product 3 has no offers
		ArrayList<Map<String, Object>> offers2 = (ArrayList<Map<String, Object>>) relevantContext2.get("offers");
		assertNull(offers2);

		ArrayList<Map<String, Object>> newOffers = (ArrayList<Map<String, Object>>) metadata.get("newOffers");

		String staticId = (String) newOffers.get(0).get("staticId");
		assertEquals("D2C_OFFER_MONTH", staticId);

		String staticId1 = (String) newOffers.get(1).get("staticId");
		assertEquals("D2C_OFFER_MONTH", staticId1);

		String staticId1_1 = (String) newOffers.get(2).get("staticId");
		assertEquals("D2C_OFFER_DAY_2", staticId1_1);

		assertEquals(3, newOffers.size());

		ArrayList<Map<String, Object>> newOffers2 = (ArrayList<Map<String, Object>>) relevantContext2.get("newOffers");
		assertNull(newOffers2);
	}

	@Test
	void copyToBubbleUpAndDownArrayExistingPathArrayInArray() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApplyRecursive(struct, "metadata.products.relevantContext.offers", new CopyTo("../../relevantContext/newOffers"));

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

		// product 0 has 1 newOffers
		ArrayList<Map<String, Object>> newOffers = (ArrayList<Map<String, Object>>) relevantContext.get("newOffers");
		String staticId = (String) newOffers.get(0).get("staticId");
		assertEquals("D2C_OFFER_MONTH", staticId);

		// product 1 has 2 newOffers
		ArrayList<Map<String, Object>> newOffers1 = (ArrayList<Map<String, Object>>) relevantContext1.get("newOffers");
		String staticId1 = (String) newOffers1.get(0).get("staticId");
		assertEquals("D2C_OFFER_MONTH", staticId1);
		String staticId1_1 = (String) newOffers1.get(1).get("staticId");
		assertEquals("D2C_OFFER_DAY_2", staticId1_1);

		// product 2 has no newOffers
		ArrayList<Map<String, Object>> newOffers2 = (ArrayList<Map<String, Object>>) relevantContext2.get("newOffers");
		assertNull(newOffers2);
	}

}
