package com.fehteh.springhashmapnavigation.navigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class DeleteIfExists extends AbstractTransformer {
    /**
     * Delete a field from a given path if objValue is not null and
     * if NavigationServiceContext reaches the end when navigating, by checking if the current navigation is the last element of the navigationPath and valueObj must not be null.
     *
     * If NavigationServiceContext is navigating an array, all elements that meet the same conditions are deleted.
     * Fields of elements of arrays can also be deleted.
     * Target path can move levels up or down (creating new paths) relatively to the NavigationService's path.
     * @param targetPath Full path relative to the NavigationService's path for the field to be deleted
     *
     * @Example
     * <pre>
     * 1. Deleting fields:
     *{@code
     * metadata.productCount.count = 3
     * NavigationService's path = "metadata.productCount.count"
     * targetPath = "../../productCount"
     *}
     * {@code metadata.productCount} is deleted since {@code metadata.productCount.count} is not null
     *
     *
     * 2. Deleting element of arrays:
     *{@code
     * NavigationService's path = "metadata.products.relevantContext.isIncluded"
     * targetPath = "../../relevantContext"
     *}
     *Since products is an array,
     * for each array's element that have "relevantContext.isIncluded" the field "relevantContext" is deleted
     * </pre>
     */
    public DeleteIfExists(String targetPath) {
        this.targetPath = targetPath;

        resetTransformer();
    }

    @Override
    public void runTransformer(String navigationElement, NavigationServiceContext ctx) {
        Object valueObj = getValueObj();

        if(!toApply && ctx.isLastElement()) {
            if(valueObj != null) {
                toApply = true;
                toApplyNextIndex = ctx.pathLevel;
            }
        }

        if(toApply && (ctx.pathLevel <= toApplyNextIndex || isObjValueCollectionType())) {
            if(!targetPathList.get(0).equals("..")) {
                if (isObjValueMapType()) {
                    Map<?,?> map = (Map<?, ?>) valueObj;
                    ((Map<String,Object>)map).remove(targetPathList.get(0));
                }

                if(isObjValueCollectionType()) {
                    ArrayList<String> arrayObj = (ArrayList<String>) valueObj;
                    arrayObj.remove(ctx.getLastArrayIterationIndexMap());
                    ctx.setLastArrayIterationIndexMap(ctx.getLastArrayIterationIndexMap() - 1);
                }

                resetTransformer();
            } else if(!(isObjValueCollectionType())) {
                toApplyNextIndex = ctx.pathLevel - 1;

                if (targetPathList.get(0).equals("..")) {
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                }
            }
        }
    }
}
