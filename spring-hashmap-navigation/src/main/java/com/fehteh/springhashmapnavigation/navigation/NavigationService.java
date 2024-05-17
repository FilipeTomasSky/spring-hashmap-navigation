package com.fehteh.springhashmapnavigation.navigation;

import com.fehteh.springhashmapnavigation.navigation.transformer.AbstractTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class NavigationService {

    /**
     * Navigates a Map<String,Object> that represents a JSON for a given navigationPath, applying the AbstractTransformer on all elements of the navigationPath.
     * The AbstractTransformer has the actions and/or conditions to either apply or not.
     *
     * The NavigationServiceContext is always in sync with Object that {@code public void run(String navigationElement, NavigationServiceContext ctx, Object valueObj)}.
     * The objectMap is being updated when iterating the navigationPath, meaning that the NavigationServiceContext holds the current information of the current objectMap,
     * holding information of the current level path, array indexes if the path as arrays, class type of the current Object
     *
     * @param objectMap The Map that which has key as String and value as Object. The value can be a Map, Collection, String, int, boolean
     * @param navigationPath The navigationPath relative to the Map, with navigationPath elements separated with "." and can't take array index (the AbstractTransformer can have array index information, NOT this navigationPath).
     *             The array will be navigated through all elements unless AbstractTransformer stops the navigation.
     * @param transformer The AbstractTransformer with the actions and/or conditions to apply or not to each element in the navigationPath
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
     * Example of navigationPath:
     * <pre>{@code
     * navigationPath = "metadata.productCount.count"
     * }</pre>
     * or if it's an array:
     * <pre>{@code
     * navigationPath = "metadata.products.relevantContext.offers"
     * }</pre>
     */

    public void navigateAndApply(Map<String, Object> objectMap, String navigationPath, AbstractTransformer transformer) {
        navigateAndApplyRecursive(objectMap, new NavigationServiceContext(navigationPath), transformer);
    }

    private void navigateAndApplyRecursive(Object entryObject, NavigationServiceContext context, AbstractTransformer transformer) {
        if(entryObject instanceof Map<?, ?> map) {
            String childrenName = context.getCurrentPath();
            Object childrenObject = map.get(childrenName);
            runTransformer(childrenObject, context, transformer);
        }

        if(entryObject instanceof Collection<?> arrayList) {
            context.decPathLevel();

            for(context.setLastArrayIterationIndexMap(0); context.getLastArrayIterationIndexMap() < arrayList.size(); context.setLastArrayIterationIndexMap(context.getLastArrayIterationIndexMap() + 1)) {
                Object childrenObject = ((ArrayList<?>) arrayList).get(context.getLastArrayIterationIndexMap());
                runTransformer(childrenObject, context, transformer);
                transformer.run(context.getCurrentPath(), context, entryObject);
            }

            context.setLastArrayIterationIndexMap(context.getLastArrayIterationIndexMap() - 1);
            context.incPathLevel();
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
            context.incPathLevel();
            // Continue to the next elements
            navigateAndApplyRecursive(childrenObject, context, transformer);

            // Run the transformer for each element already navigated
            context.decPathLevel();
            transformer.run(context.getCurrentPath(), context, childrenObject);
        }
    }
}
