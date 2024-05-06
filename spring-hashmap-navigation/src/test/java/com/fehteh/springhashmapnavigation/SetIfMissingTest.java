package com.fehteh.springhashmapnavigation;

import com.fehteh.springhashmapnavigation.navigation.NavigationService;
import com.fehteh.springhashmapnavigation.transformer.SetIfMissing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

class SetIfMissingTest {

	@Test
	void setIfMissing() {
		Map<String, Object> metadata = SpringHashmapNavigationApplication.createStruct();

		NavigationService navigationService = new NavigationService();
		navigationService.navigateAndApply(metadata, "products.relevantContext.isIncluded", new SetIfMissing("../newField", true));

		ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
		Map<String, Object> relevantContext = (Map<String, Object>) products.get(1).get("relevantContext");
		boolean newField = (boolean) relevantContext.get("newField");

		Assertions.assertTrue(newField);
	}

}
