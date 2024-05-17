package com.fehteh.springhashmapnavigation.navigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

public class SetIfMissing extends AbstractTransformer {
    private final Object newValue;

    /**
     * Set a value in the given target path if objValue is null and
     * if NavigationServiceContext reaches the end when navigating, by checking if the current navigation is the last element of the navigationPath and valueObj must be null.
     *
     * Target path can move levels up or down (creating new paths) relatively to the NavigationService's path.
     * @param targetPath Full path relative to the NavigationService's path for the field to be set
     * @param newValue New value for the new field if valueObj contains comparableValue
     *
     * @Example
     * <pre>
     * 1. Setting fields:
     *{@code
     * NavigationService's path = "metadata.productCount.missing"
     * targetPath = "../../newField"
     * newValue = true
     *}
     * {@code metadata.productCount = true} is created since "missing" is null
     *
     *
     * 2. Setting fields in array elements:
     *{@code
     * NavigationService's path = "metadata.products.relevantContext.isIncluded"
     * targetPath = "../newField"
     * newValue = true;
     *}
     *Since products is an array,
     * for each products's element that have "relevantContext.isIncluded" as null, "relevantContext.newField = true" is set
     * </pre>
     */
    public SetIfMissing(String targetPath, Object newValue) {
        this.targetPath = targetPath;
        this.newValue = newValue;

        resetTransformer();
    }

    @Override
    public void runTransformer(String navigationElement, NavigationServiceContext ctx) {
        if(!toApply && getValueObj() == null) {
            toApply = true;
            toApplyNextIndex = ctx.pathLevel;
        }

        if(toApply && ctx.pathLevel <= toApplyNextIndex) {
            if(!isObjValueCollectionType()) {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = ctx.pathLevel - 1;
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
