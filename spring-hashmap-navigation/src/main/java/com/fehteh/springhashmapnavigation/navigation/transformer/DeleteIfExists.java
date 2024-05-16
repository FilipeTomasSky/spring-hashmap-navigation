package com.fehteh.springhashmapnavigation.navigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class DeleteIfExists extends AbstractTransformer {
    /**
     * Deletes a field for a path if another objValue exists, if NavigationService's path exists (reached full path when navigating).
     * If the field is an array, it will delete only the array elements where the {@code path} exists
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
     * {@code metadata.productCount} is deleted if {@code metadata.productCount.count} is not null
     *
     *
     * 2. Deleting element of arrays:
     *{@code
     * NavigationService's path = "metadata.products.relevantContext.isIncluded"
     * targetPath = "../../relevantContext"
     *}
     *Since products is an array,
     * for each array's element that have "relevantContext.isIncluded"relevantContext" the field "relevantContext" is deleted
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
                toApplyNextIndex = ctx.index;
            }
        }

        if(toApply && (ctx.index <= toApplyNextIndex || isObjValueCollectionType())) {
            if(!targetPathList.get(0).equals("..")) {
                if (isObjValueMapType()) {
                    Map<?,?> map = (Map<?, ?>) valueObj;
                    ((Map<String,Object>)map).remove(targetPathList.get(0));
                }

                if(isObjValueCollectionType()) {
                    ArrayList<String> arrayObj = (ArrayList<String>) valueObj;
                    arrayObj.remove(ctx.getArrayIndex());
                    ctx.setArrayIndex(ctx.getArrayIndex() - 1);
                }

                resetTransformer();
            } else if(!(isObjValueCollectionType())) {
                toApplyNextIndex = ctx.index - 1;

                if (targetPathList.get(0).equals("..")) {
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                }
            }
        }
    }
}
