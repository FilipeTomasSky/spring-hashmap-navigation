package com.fehteh.springhashmapnavigation;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringHashmapNavigationApplication {

	public static Map<String, Object> createStruct(){
		Map<String, Object> struct = new HashMap<String, Object>();

		Map<String, Object> metadata = new HashMap<String, Object>();
		struct.put("metadata", metadata);

		Map<String,Object> productCount = new HashMap<String, Object>();
		productCount.put("count", 2);
		metadata.put("productCount", productCount);
		Map<String,Object> productName = new HashMap<>();
		metadata.put("productName", productName);
		productName.put("name", "subscriptionName");

		ArrayList<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
		Map<String,Object> product = new HashMap<String, Object>();
		Map<String,Object> relevantContext = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> offers = new ArrayList<Map<String, Object>>();
		Map<String,Object> offer = new HashMap<String,Object>();

		metadata.put("products", products);
		products.add(product);
		product.put("category","D2C");
		product.put("quantity",1);
		product.put("staticId","D2C_SUBSCRIPTION_MONTH");
		product.put("id",11111);
		product.put("relevantContext", relevantContext);
		relevantContext.put("nextRenewalDueDate", "2021-10-17T23:59:59+00:00");
		relevantContext.put("duration", "P1M");
		relevantContext.put("isIncluded", false);
		relevantContext.put("offers", offers);
		offers.add(offer);
		offer.put("staticId","D2C_OFFER_MONTH");
		//offer = new HashMap<String, Object>();
		//offers.add(offer);
		//offer.put("staticId","D2C_OFFER_DAY_1");

		product = new HashMap<String, Object>();
		products.add(product);
		product.put("category","D2B");
		product.put("quantity",1);
		product.put("staticId","D2B_SUBSCRIPTION_MONTH");
		product.put("id",22222);
		relevantContext = new HashMap<String, Object>();
		product.put("relevantContext", relevantContext);
		relevantContext.put("nextRenewalDueDate", "2021-10-17T23:59:59+00:00");
		relevantContext.put("duration", "P18M");
		//relevantContext.put("isIncluded", true);
		offers = new ArrayList<Map<String,Object>>();
		relevantContext.put("offers", offers);
		offer = new HashMap<String, Object>();
		offers.add(offer);
		offer.put("staticId","D2C_OFFER_MONTH");
		offer = new HashMap<String, Object>();
		offers.add(offer);
		offer.put("staticId","D2C_OFFER_DAY_2");

		product = new HashMap<String, Object>();
		products.add(product);
		product.put("category","D2B");
		product.put("quantity",1);
		product.put("staticId","D2B_SUBSCRIPTION_YEAR");
		product.put("id",33333);
		relevantContext = new HashMap<String, Object>();
		product.put("relevantContext", relevantContext);
		relevantContext.put("nextRenewalDueDate", "2021-10-17T23:59:59+00:00");
		relevantContext.put("duration", "P18M");

		ArrayList<Map<String, Object>> allOffers = new ArrayList<Map<String, Object>>();
		metadata.put("allOffers", allOffers);

		return struct;
	}
}
