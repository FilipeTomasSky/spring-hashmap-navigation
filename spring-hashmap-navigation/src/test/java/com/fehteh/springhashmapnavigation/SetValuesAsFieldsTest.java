package com.fehteh.springhashmapnavigation;

import com.fehteh.springhashmapnavigation.navigation.NavigationService;
import com.fehteh.springhashmapnavigation.navigation.transformer.SetValuesAsFields;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SetValuesAsFieldsTest {
    @Test
    void setValuesAsBooleanFieldsBubbleUpField() {
        Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

        Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
        assertNotNull(metadata);

        NavigationService navigationService = new NavigationService();
        navigationService.navigateAndApply(struct, "metadata.productName.name", new SetValuesAsFields("../..", true));

        assertTrue((Boolean) metadata.get("subscriptionName"));
    }

    @Test
    void setValuesAsBooleanFieldsBubbleDownField() {
        Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

        Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
        assertNotNull(metadata);

        NavigationService navigationService = new NavigationService();
        navigationService.navigateAndApply(struct, "metadata.productName.name", new SetValuesAsFields("../newField", true));

        Map<String, Object> productName = (Map<String, Object>) metadata.get("productName");

        Map<String, Object> newField = (Map<String, Object>) productName.get("newField");

        assertTrue((Boolean) newField.get("subscriptionName"));
    }

    @Test
    void setValuesAsBooleanFieldsBubbleUpArray() {
        Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

        Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
        assertNotNull(metadata);

        ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
        String staticId = (String) products.get(0).get("staticId");
        assertEquals("D2C_SUBSCRIPTION_MONTH", staticId);

        String staticId1 = (String) products.get(1).get("staticId");
        assertEquals("D2B_SUBSCRIPTION_MONTH", staticId1);

        String staticId2 = (String) products.get(2).get("staticId");
        assertEquals("D2B_SUBSCRIPTION_YEAR", staticId2);

        NavigationService navigationService = new NavigationService();
        navigationService.navigateAndApply(struct, "metadata.products.staticId", new SetValuesAsFields("../..", true));

        boolean d2cSubscriptionMonth = (boolean) metadata.get("d2cSubscriptionMonth");
        boolean d2bSubscriptionMonth = (boolean) metadata.get("d2bSubscriptionMonth");
        boolean d2cSubscriptionYear = (boolean) metadata.get("d2bSubscriptionYear");

        assertTrue(d2cSubscriptionMonth);
        assertTrue(d2bSubscriptionMonth);
        assertTrue(d2cSubscriptionYear);
    }

    @Test
    void setValuesAsBooleanFieldsBubbleDownArray() {
        Map<String, Object> struct = SpringHashmapNavigationApplication.createStruct();

        Map<String, Object> metadata = (Map<String, Object>) struct.get("metadata");
        assertNotNull(metadata);

        ArrayList<Map<String, Object>> products = (ArrayList<Map<String, Object>>) metadata.get("products");
        String staticId = (String) products.get(0).get("staticId");
        assertEquals("D2C_SUBSCRIPTION_MONTH", staticId);

        String staticId1 = (String) products.get(1).get("staticId");
        assertEquals("D2B_SUBSCRIPTION_MONTH", staticId1);

        String staticId2 = (String) products.get(2).get("staticId");
        assertEquals("D2B_SUBSCRIPTION_YEAR", staticId2);

        NavigationService navigationService = new NavigationService();
        navigationService.navigateAndApply(struct, "metadata.products.staticId", new SetValuesAsFields("newField/staticId", true));

        Map<String, Object> newField = (Map<String, Object>) products.get(0).get("newField");
        Map<String, Object> newStaticId = (Map<String, Object>) newField.get("staticId");
        boolean d2cSubscriptionMonth = (boolean) newStaticId.get("d2cSubscriptionMonth");

        Map<String, Object> newField1 = (Map<String, Object>) products.get(1).get("newField");
        Map<String, Object> newStaticId1 = (Map<String, Object>) newField1.get("staticId");
        boolean d2bSubscriptionMonth = (boolean) newStaticId1.get("d2bSubscriptionMonth");

        Map<String, Object> newField2 = (Map<String, Object>) products.get(2).get("newField");
        Map<String, Object> newStaticId2 = (Map<String, Object>) newField2.get("staticId");
        boolean d2cSubscriptionYear = (boolean) newStaticId2.get("d2bSubscriptionYear");

        assertTrue(d2cSubscriptionMonth);
        assertTrue(d2bSubscriptionMonth);
        assertTrue(d2cSubscriptionYear);
    }
}
