package com.fehteh.springhashmapnavigation.navigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class SetValuesAsFields extends AbstractTransformer {
    private final Object newValue;
    private List<String> newFieldNames = new ArrayList<>();

    /**
     * Set valueObj as a field formatting to camel case with a given value, if NavigationService's path exists (reached full path when navigating)
     * In arrays if the path exists in several elements it will be set for all the elements
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
     * 2. Setting element of arrays:
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
                toApplyNextIndex = ctx.index;
            }
            newFieldNames.add(formatToCamelCase((String) getValueObj()));
        }

        if(toApply && ctx.index <= toApplyNextIndex) {
            if(isObjValueCollectionType()) {
                return;
            }

            if(targetPathList.size() == 0) {
                putValue(null, newValue, false);
                resetTransformer();
            } else {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = ctx.index - 1;
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
