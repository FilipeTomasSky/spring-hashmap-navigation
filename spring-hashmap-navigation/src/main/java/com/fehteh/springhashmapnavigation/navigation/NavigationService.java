package com.fehteh.springhashmapnavigation.navigation;

import com.fehteh.springhashmapnavigation.transformer.AbstractTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class NavigationService {

    /**
     * Navigates a Map<String,Object> that represents a JSON for a given path, applying the AbstractTransformer on all elements of the path.
     * The AbstractTransformer has the actions and/or conditions to either apply or not.
     *
     * @param objectMap The Map that which has key as String and value as Object. The value can be a Map, Collection, String, int, boolean
     * @param path The path relative to the Map, with path elements separated with "." and can't take array index (the AbstractTransformer can have array index information, NOT this path).
     *             The array will be navigated through all elements unless AbstractTransformer stops the navigation.
     * @param transformer The AbstractTransformer with the actions and/or conditions to apply or not to each element in the path
     *
     * @example
     * Example of objectMap
     * <pre>{@code
     * objectMap = {
     *      "metadata": {
     *          "productCount": {
     *              "count": 3
     *          }
     *          "productName": "subscriptionName",
     *          "products": [
     *              {
     *                  "relevantContext": {
     *                      "isIncluded": false,
     *                      "offers": [
     *                          {"staticId": "D2C_OFFER_MONTH"}
     *                      ]
     *                  }
     *                  "id": 11111,
     *                  "staticId": "D2C_SUBSCRIPTION_MONTH"
     *              },
     *              {
     *                  "relevantContext": {
     *                      "isIncluded": true,
     *                      "offers": [
     *                          {"staticId": "D2C_OFFER_MONTH"},
     *                          {"staticId": "D2C_OFFER_DAY_2"}
     *                      ]
     *                  }
     *                  "id": 22222,
     *                  "staticId": "D2C_SUBSCRIPTION_MONTH"
     *              },
     *              {
     *                  "relevantContext": {
     *                      "isIncluded": false
     *                  }
     *                  "id": 33333,
     *                  "staticId": "D2C_SUBSCRIPTION_MONTH"
     *              }
     *          ]
     *      }
     *  }
     * }</pre>
     *
     * Example of path:
     * <pre>{@code
     * path = "metadata.productCount.count"
     * }</pre>
     * or if it's an array:
     * <pre>{@code
     * path = "metadata.products.relevantContext.offers"
     * }</pre>
     */
    public void navigateAndApply(Map<String, Object> objectMap, String path, AbstractTransformer transformer) {
        navigateAndApplyRecursive(objectMap, new NavigationServiceContext(path), transformer);
    }

    private void navigateAndApplyRecursive(Object entryObject, NavigationServiceContext context, AbstractTransformer transformer) {
        if(entryObject instanceof Map<?, ?> map) {
            String childrenName = context.getCurrentPath();
            Object childrenObject = map.get(childrenName);
            runTransformer(childrenObject, context, transformer);
        }

        if(entryObject instanceof Collection<?> arrayList) {
            context.decIndex();

            for(context.setArrayIndex(0); context.getArrayIndex() < arrayList.size(); context.setArrayIndex(context.getArrayIndex() + 1)) {
                Object childrenObject = ((ArrayList<?>) arrayList).get(context.getArrayIndex());
                runTransformer(childrenObject, context, transformer);
                transformer.run(context.getCurrentPath(), context, entryObject);
            }

            context.setArrayIndex(context.getArrayIndex() - 1);
            context.incIndex();
        }
    }

    /**
     * Runs the AbstractTransformer and iterates the Object moving forward (level up) on the structure by calling the navigateAndApplyRecursive.
     * When returning the navigateAndApplyRecursive, decrease the index and run the AbstractTransformer to allow the Transformer to move backwards (level down) if needed.
     *
     * @param childrenObject The structure to move forward
     * @param context The context with the information of the current navigation. Increasing and decreasing the index that represents the path level
     * @param transformer Has the logic to be applied to the Object element
     */
    private void runTransformer(Object childrenObject, NavigationServiceContext context, AbstractTransformer transformer) {
        transformer.run(context.getCurrentPath(), context, childrenObject);

        if(!context.isLastElement()) {
            context.incIndex();
            navigateAndApplyRecursive(childrenObject, context, transformer);

            context.decIndex();
            transformer.run(context.getCurrentPath(), context, childrenObject);
        }
    }
}
