package com.fehteh.springhashmapnavigation;

import com.fehteh.springhashmapnavigation.navigation.NavigationService;
import com.fehteh.springhashmapnavigation.transformer.SetIfMissing;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SetIfMissingTest {
	@Test
	void setIfMissingBubbleUpField() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.productCount.missing", new SetIfMissing("../../newField", true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		boolean newField = (boolean) metadata.get("newField");
		assertTrue(newField);
	}

	@Test
	void setIfMissingBubbleDownField() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.missing", new SetIfMissing("newField/newField1", true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, Object> newField = (Map<String, Object>) metadata.get("newField");

		boolean newField1 = (boolean) newField.get("newField1");
		assertTrue(newField1);
	}

	@Test
	void setIfMissingBubbleUpAndDownField() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.productCount.missing", new SetIfMissing("../../newField/newField1", true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		Map<String, Object> newField = (Map<String, Object>) metadata.get("newField");

		boolean newField1 = (boolean) newField.get("newField1");
		assertTrue(newField1);
	}

	@Test
	void setIfMissingBubbleUpAndDownArrayExistingPath() {
		Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.offers.missing", new SetIfMissing("../../../relevantContext/newField/missing", true));

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);

		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");

		Map<String, Object> relevantContext = (Map<String, Object>) products.get(0).get("relevantContext");
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(1).get("relevantContext");

		String duration = (String) relevantContext.get("duration");
		assertNotNull(duration); // check if didn't overwrite

		Map<String, Object> newField = (Map<String, Object>) relevantContext.get("newField");
		Map<String, Object> newField1 = (Map<String, Object>) relevantContext1.get("newField");

		boolean missing = (boolean) newField.get("missing");
		boolean missing1 = (boolean) newField1.get("missing");

		assertTrue(missing);
		assertTrue(missing1);
	}

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
	void setIfMissingBubbleUpSetOutsideArray() { //This checks only for the first element that which the field is missing. Since if sets outside of the array, it looses the context of the index of the next array
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
		navigationService.navigateAndApply(struct, "metadata.products.relevantContext.offers.staticId", new SetIfMissing("../newField", true));
		//TODO cant it be targetPath "../../newField" because even offers that exists in product 2

		Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
		assertNotNull(metadata);
		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
		assertNotNull(products);

		//product 0 has staticId
		Map<String, Object> relevantContext = (Map<String, Object>) products.get(0).get("relevantContext");
		ArrayList<Map<String, Object>> offers = (ArrayList<Map<String, Object>>) relevantContext.get("offers");
		assertNotNull(offers.get(0).get("staticId"));

		//product 1 has staticId
		Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(1).get("relevantContext");
		ArrayList<Map<String, Object>> offers1 = (ArrayList<Map<String, Object>>) relevantContext.get("offers");
		assertNotNull(offers1.get(0).get("staticId"));

		//product 1 has no staticId and no offers
		Map<String, Object> relevantContext2 = (Map<String, Object>) products.get(2).get("relevantContext");
		ArrayList<Map<String, Object>> offers2 = (ArrayList<Map<String, Object>>) relevantContext2.get("offers");
		assertNull(offers2);

		boolean newField2 = (boolean) relevantContext2.get("newField");
		assertTrue(newField2);

	}
}
