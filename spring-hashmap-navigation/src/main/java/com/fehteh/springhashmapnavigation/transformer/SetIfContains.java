package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class SetIfContains extends AbstractTransformer{
    private final String targetPath;
    private final String comparableValue;
    private final String newValue;

    private List<String> targetPathList;
    private boolean toApply;
    private int toApplyNextIndex;


    public SetIfContains(String targetPath, String comparableValue, String newValue) {
        this.targetPath = targetPath;
        this.comparableValue = comparableValue;
        this.newValue = newValue;

        resetTransformer();
    }

    @Override
    public void runTransformer(String navigationElement, NavigationServiceContext ctx, Object value) {
        if(!toApply && ctx.isLastElement()) {
            if(((String)value).contains(comparableValue)) {
                toApply = true;
                toApplyNextIndex = ctx.index;
            }
        }

        if(toApply && ctx.index <= toApplyNextIndex) { //check if we are moving backwards in path to apply (by checking indexes)
            if(!(value instanceof Collection<?>)) {
                toApplyNextIndex = ctx.index - 1;

                if(targetPathList.get(0).equals("..")) {
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    if (value instanceof Map<?, ?> map) {
                        ((Map<String,Object>)map).put(targetPathList.get(0), newValue);
                        resetTransformer();
                    }
                }
            }
        }
    }

    private void resetTransformer() { //TODO abstract
        this.toApply = false;
        this.toApplyNextIndex = 0;
        this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));
    }
}
