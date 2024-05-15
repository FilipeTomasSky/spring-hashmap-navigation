package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class CopyTo extends AbstractTransformer {
    private Object newValue;

    private List<Object> accumulatorArray = new ArrayList<>();


    public CopyTo(String targetPath) {
        this.targetPath = targetPath;

        resetTransformer();
    }

    @Override
    void runTransformer(String navigationElement, NavigationServiceContext ctx) {
        if(!toApply && ctx.isLastElement() || ctx.isLastElement() && isObjValueCollectionType()) {
            if(getValueObj() != null) {
                toApply = true;
                toApplyNextIndex = ctx.index;
                newValue = getValueObj();
                this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));

                if(isObjValueCollectionType()) {
                    accumulatorArray.addAll((Collection<?>)newValue);
                }
            }
        }

        if(toApply && ctx.index <= toApplyNextIndex) {
            if(ctx.isLastElement() || !(isObjValueCollectionType())) {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = ctx.index - 1;
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    if(createPath(targetPathList, targetPathList.size()-1)) {
                        putValue(targetPathList.get(targetPathList.size()-1), newValue, true);
                        resetTransformer();
                    }
                }
            }
        }
    }

    @Override
    boolean putValue(String fieldName, Object newValue, boolean overrideField) {
        Map<?,?> map = (Map<?, ?>) getValueObj();

        if(!accumulatorArray.isEmpty()) {
            ArrayList<Object> array = ((Map<String, ArrayList<Object>>) map).get(fieldName);

            if(array == null) {
                array = new ArrayList<>();
                super.putValue(fieldName, array, overrideField);
            }

            array.addAll(accumulatorArray);
            accumulatorArray = new ArrayList<>();
            return true;
        } else {
            return super.putValue(fieldName, newValue, overrideField);
        }
    }
}
