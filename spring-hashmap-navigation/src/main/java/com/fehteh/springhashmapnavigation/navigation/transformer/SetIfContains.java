package com.fehteh.springhashmapnavigation.navigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

public class SetIfContains extends AbstractTransformer {
    private final String comparableValue;
    private final Object newValue;

    /**
     * If the valueObj contains comparableValue, set a newValue in the targetPath, if NavigationService's path exists (reached full path when navigating)
     * Target path can move levels up or down (creating new paths) relatively to the NavigationService's path.
     * @param targetPath Full path relative to the NavigationService's path for the field to be set
     * @param comparableValue Value to be compared with the valueObj
     * @param newValue New value for the new field if valueObj contains comparableValue
     *
     * @Example
     * <pre>
     * 1. Deleting fields:
     *{@code
     * metadata.productCount.count = 3
     * NavigationService's path = "metadata.productCount.count"
     * targetPath = "../../newField"
     * comparableValue = 3
     * newValue = true
     *}
     * {@code metadata.newField = true} is created
     *
     *
     * 2. Deleting element of arrays:
     *{@code
     * NavigationService's path = "metadata.products.relevantContext.offers.staticId"
     * targetPath = "../../../relevantContext/newField"
     * comparableValue = D2C_OFFER_MONTH
     * newValue = true;
     *}
     *Since products is an array,
     * for each products's element that have "D2C_OFFER_MONTH" the field "relevantContext.newField = true" is set
     * </pre>
     */
    public SetIfContains(String targetPath, Object comparableValue, Object newValue) {
        this.targetPath = targetPath;
        this.comparableValue = comparableValue.toString();
        this.newValue = newValue;

        resetTransformer();
    }

    @Override
    void runTransformer(String navigationElement, NavigationServiceContext ctx) {
        if(!toApply && ctx.isLastElement()) {
            if(getValueObj() != null && (getValueObj().toString()).contains(comparableValue)) {
                toApply = true;
                toApplyNextIndex = ctx.index;
            }
        }

        if(toApply && ctx.index <= toApplyNextIndex) {
            if(!isObjValueCollectionType()) {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = ctx.index - 1;
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    if(createPath(targetPathList, targetPathList.size()-1)) {
                        putValue(targetPathList.get(targetPathList.size()-1), newValue, false);
                        resetTransformer();
                    }
                }
            }
        }
    }

}
