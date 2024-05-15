package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class SetIfContains extends AbstractTransformer {
    private final String comparableValue;
    private final Object newValue;

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
