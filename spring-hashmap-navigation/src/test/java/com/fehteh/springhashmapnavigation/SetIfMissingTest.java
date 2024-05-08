package com.fehteh.springhashmapnavigation;

import com.fehteh.springhashmapnavigation.navigation.NavigationService;
import com.fehteh.springhashmapnavigation.transformer.SetIfMissing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SetIfMissingTest {

	@Test
	void setIfMissingBubbleUpArray() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.isIncluded", new SetIfMissing("../newField", true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);
		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
		assertNotNull(products);
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(0).get("relevantContext");
		assertNotNull(relevantContext1);
		Map<String, Object> relevantContext2 = (Map<String, Object>) products.get(1).get("relevantContext");
		assertNotNull(relevantContext2);

		assertNull(relevantContext1.get("newField"));

		boolean newField = (boolean) relevantContext2.get("newField");
		assertTrue(newField);
	}

	@Test
	void setIfMissingBubbleUpSetOutsideArray() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.isIncluded", new SetIfMissing("../../../newField", true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		boolean newField = (boolean) metadata.get("newField");
		assertTrue(newField);
	}

	@Test
	void setIfMissingBubbleUpSetTwoArrayElements() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.missing", new SetIfMissing("../newField", "yes"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);
		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
		assertNotNull(products);
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(0).get("relevantContext");
		assertNotNull(relevantContext1);
		Map<String, Object> relevantContext2 = (Map<String, Object>) products.get(1).get("relevantContext");
		assertNotNull(relevantContext2);
		String newField1 = (String) relevantContext1.get("newField");
		String newField2 = (String) relevantContext2.get("newField");

		assertEquals("yes", newField1);
		assertEquals("yes", newField2);
	}

	@Test
	void setIfMissingBubbleDownArray() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.missing", new SetIfMissing("../newField/missing", "yes"));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);
		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
		assertNotNull(products);
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(0).get("relevantContext");
		assertNotNull(relevantContext1);
		Map<String, Object> relevantContext2 = (Map<String, Object>) products.get(1).get("relevantContext");
		assertNotNull(relevantContext2);
		Map<String, Object> newField1 = (Map<String, Object>) relevantContext1.get("newField");
		assertNotNull(newField1);
		Map<String, Object> newField2 = (Map<String, Object>) relevantContext2.get("newField");
		assertNotNull(newField2);
		String missing1 = (String) newField1.get("missing");
		String missing2 = (String) newField2.get("missing");

		assertEquals("yes", missing1);
		assertEquals("yes", missing2);
	}

	@Test
	void setIfMissingArrayInArray() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.offers.staticId", new SetIfMissing("../../newField", true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);
		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
		assertNotNull(products);
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(0).get("relevantContext");
		assertNotNull(relevantContext1);
		Map<String, Object> relevantContext2 = (Map<String, Object>) products.get(1).get("relevantContext");
		assertNotNull(relevantContext2);
		boolean newField1 = (boolean) relevantContext1.get("newField");
		assertTrue(newField1);

		assertNull(relevantContext2.get("newField"));
	}
}
