package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class SetIfMissing extends AbstractTransformer {
    private final String targetPath;
    private final Object newValue;

    private List<String> targetPathList;
    private boolean toApply;
    private int toApplyNextIndex;

    public SetIfMissing(String targetPath, Object newValue) {
        this.targetPath = targetPath;
        this.newValue = newValue;

        resetTransformer();
    }


    @Override
    public void applyTransformer(String navigationElement, NavigationServiceContext ctx) {
        Object valueObj = ctx.getValueObj();
        int index = ctx.getIndex();

        if(!toApply && ctx.isLastElement()) {
            if(!toApply && valueObj == null) {
                toApply = true;
                toApplyNextIndex = index;
            }
        }

        if(toApply && index <= toApplyNextIndex) {
            if(!(ctx.isValueObjTypeCollection())) {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = index - 1;
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    createPath(ctx);
                }
            }
        }
    }

    private void createPath(NavigationServiceContext ctx) {
        if (ctx.isValueObjTypeMap()) {
            Map<?, ?> map = (Map<?, ?>) ctx.getValueObj();

            while(targetPathList.size() != 1) {
                Object pathElement = targetPathList.get(0);

                if(map.get(pathElement) == null) {
                    ((Map<String,Object>)map).put(targetPathList.get(0), new HashMap<String, Object>());
                }
                map = (Map<?, ?>)map.get(pathElement);

                targetPathList = targetPathList.subList(1, targetPathList.size());
            }

            ((Map<String,Object>)map).put(targetPathList.get(0), newValue);
            resetTransformer();
        }
    }

    private void resetTransformer() {
        this.toApply = false;
        this.toApplyNextIndex = 0;
        this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));
    }
}
