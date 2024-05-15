package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class SetIfMissing extends AbstractTransformer {
    private final Object newValue;

    public SetIfMissing(String targetPath, Object newValue) {
        this.targetPath = targetPath;
        this.newValue = newValue;

        resetTransformer();
    }

    @Override
    public void runTransformer(String navigationElement, NavigationServiceContext ctx) {
        if(!toApply && getValueObj() == null) {
            toApply = true;
            toApplyNextIndex = ctx.index;
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
