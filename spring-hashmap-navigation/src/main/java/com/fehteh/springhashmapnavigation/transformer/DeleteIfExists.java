package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class DeleteIfExists extends AbstractTransformer {
    private String targetPath;

    private List<String> targetPathList;
    private boolean toApply;
    private int toApplyNextIndex;

    public DeleteIfExists(String targetPath) {
        this.targetPath = targetPath;

        resetTransformer();
    }

    @Override
    public void runTransformer(String navigationElement, NavigationServiceContext ctx, Object valueObj) {
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

    private void resetTransformer() {
        this.toApply = false;
        this.toApplyNextIndex = 0;
        this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));
    }
}
