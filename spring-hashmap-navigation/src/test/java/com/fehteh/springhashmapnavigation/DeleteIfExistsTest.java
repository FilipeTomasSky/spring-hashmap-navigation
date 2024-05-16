package com.fehteh.springhashmapnavigation;

import com.fehteh.springhashmapnavigation.navigation.NavigationService;
import com.fehteh.springhashmapnavigation.navigation.transformer.DeleteIfExists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteIfExistsTest {

    @Test
    void deleteIfExistsBubbleUpField() {
        Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

        NavigationService navigationService = new NavigationService();
        Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");

        assertNotNull(metadata.get("productCount"));
        navigationService.navigateAndApply(struct, "metadata.productCount.count", new DeleteIfExists("../../productCount"));
        assertNull(metadata.get("productCount"));
    }

    @Test
    void deleteIfExistsFieldOfElementOfArray() {
        Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

        NavigationService navigationService = new NavigationService();
        navigationService.navigateAndApply(struct, "metadata.products.relevantContext.isIncluded", new DeleteIfExists("../../relevantContext"));

        Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
        ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
        Map<String, Object> relevantContext1 = (Map<String, Object>) products.get(0).get("relevantContext");
        Map<String, Object> relevantContext2 = (Map<String, Object>) products.get(1).get("relevantContext");

        Assertions.assertNull(relevantContext1);
        Assertions.assertNotNull(relevantContext2);
    }

    @Test
    void deleteIfExistsElementOfArray() {
        Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

        NavigationService navigationService = new NavigationService();
        Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
        ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");

        Assertions.assertEquals(3, products.size());

        navigationService.navigateAndApply(struct, "metadata.products.relevantContext.isIncluded", new DeleteIfExists("../../../products"));

        Assertions.assertEquals(2, products.size());

        Map<String, Object> relevantContext = (Map<String, Object>) products.get(0).get("relevantContext");
        assertNull(relevantContext.get("isIncluded"));
    }

}
