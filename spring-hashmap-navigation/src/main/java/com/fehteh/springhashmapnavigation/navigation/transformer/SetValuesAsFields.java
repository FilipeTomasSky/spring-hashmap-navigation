package com.fehteh.springhashmapnavigation.navigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class SetValuesAsFields extends AbstractTransformer {
    private final Object newValue;
    private List<String> newFieldNames = new ArrayList<>();

    /**
     * Set valueObj as a new field's name formatted to camel case with a given value in a given path and
     * if NavigationServiceContext reaches the end when navigating, by checking if the current navigation is the last element of the navigationPath and valueObj must be a string and not null.
     *
     * If NavigationServiceContext is navigating an array, all elements will be iterated and all the fields that meet the same conditions are set.
     * Does not override existing fields.
     * Target path can move levels up or down (creating new paths) relatively to the NavigationService's path.
     *
     * @param targetPath Full path relative to the NavigationService's path for the field to be set
     * @param newValue New value for the new field
     *
     * @Example
     * <pre>
     * 1. Setting fields:
     *{@code
     * metadata.productName.name = subscriptionName
     * NavigationService's path = "metadata.productName.name"
     * targetPath = "../.."s
     * newValue = true
     *}
     * {@code metadata.subscriptionName = true} is created
     *
     *
     * 2. Setting fields from elements of arrays:
     *{@code metadata.products = [
     *     {staticId: D2C_SUBSCRIPTION_MONTH}
     *     {staticId: D2B_SUBSCRIPTION_MONTH}
     *     {staticId: D2B_SUBSCRIPTION_YEAR}
     *]}
     *{@code
     * NavigationService's path = "metadata.products.staticId"
     * targetPath = "../.."
     * newValue = true;
     *}
     * {@code metadata.d2cSubscriptionMonth = true} is created
     * {@code metadata.d2bSubscriptionMonth = true} is created
     * {@code metadata.d2bSubscriptionYear = true} is created
     * </pre>
     */

    public SetValuesAsFields(String targetPath, Object newValue) {
        this.targetPath = targetPath;
        this.newValue = newValue;

        resetTransformer();
    }

    @Override
    void runTransformer(String navigationElement, NavigationServiceContext ctx) {
        if(ctx.isLastElement() && isObjValueStringType()) {
            if(!toApply) {
                toApply = true;
                toApplyNextIndex = ctx.pathLevel;
            }
            newFieldNames.add(formatToCamelCase((String) getValueObj()));
        }

        if(toApply && ctx.pathLevel <= toApplyNextIndex) {
            if(isObjValueCollectionType()) {
                return;
            }

            if(targetPathList.size() == 0) {
                putValue(null, newValue, false);
                resetTransformer();
            } else {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = ctx.pathLevel - 1;
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    if(createPath(targetPathList, targetPathList.size())) {
                        putValue(null, newValue, false);
                        resetTransformer();
                    }
                }
            }
        }
    }

    @Override
    protected boolean putValue(String fieldName, Object newValue, boolean overrideField) {
        for (String field : newFieldNames) {
            super.putValue(field, newValue, false);
        }
        return false;
    }

    public static String formatToCamelCase(String input) {
        if (!input.contains("_") && !input.contains(" ")) {
            return input;
        }

        String[] parts = input.split("[_\\s]+");
        StringBuilder formattedString = new StringBuilder();
        formattedString.append(parts[0].toLowerCase());

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            formattedString.append(Character.toUpperCase(part.charAt(0)));
            formattedString.append(part.substring(1).toLowerCase());
        }

        return formattedString.toString();
    }
}
