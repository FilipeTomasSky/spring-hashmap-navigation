package com.fehteh.springhashmapnavigation;

import com.fehteh.springhashmapnavigation.navigation.NavigationService;
import com.fehteh.springhashmapnavigation.transformer.DeleteIfContains;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

public class DeleteIfContainsTest {

    @Test
    void deleteIfMissing_1() {
        Map<String, Object> metadata = SpringHashmapNavigationApplication.createStruct();

        NavigationService navigationService = new NavigationService();
        navigationService.navigateAndApply(metadata, "products.relevantContext.isIncluded", new DeleteIfContains("../../relevantContext"));
        ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
        Map<String, Object> relevantContext = (Map<String, Object>) products.get(0).get("relevantContext");

        Assertions.assertNull(relevantContext);
    }

    @Test
    void deleteIfMissing_2() {
        Map<String, Object> metadata = SpringHashmapNavigationApplication.createStruct();

        NavigationService navigationService = new NavigationService();
        navigationService.navigateAndApply(metadata, "products.relevantContext.isIncluded", new DeleteIfContains("../../../products"));
        ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");

        Assertions.assertEquals(1, products.size());
    }

}
