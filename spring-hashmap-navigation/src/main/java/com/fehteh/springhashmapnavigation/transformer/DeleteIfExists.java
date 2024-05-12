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
    public void applyTransformer(String navigationElement, NavigationServiceContext ctx) {
        Object valueObj = ctx.getValueObj();
        int index = ctx.getIndex();

        if(!toApply && ctx.isLastElement()) {
            if(valueObj != null) {
                toApply = true;
                toApplyNextIndex = index;
            }
        }
        if(toApply && (index <= toApplyNextIndex || ctx.isValueObjTypeCollection())) {
            if(!targetPathList.get(0).equals("..")) {
                if (valueObj instanceof Map<?, ?> map) {

                    ((Map<String,Object>)map).remove(targetPathList.get(0));
                }
                if(valueObj instanceof Collection<?> arrayList) {

                    ((ArrayList) arrayList).remove(ctx.getArrayIndex());
                    ctx.setArrayIndex(ctx.getArrayIndex() - 1);
                }
                resetTransformer();
            } else if(!(ctx.isValueObjTypeCollection())) {
                toApplyNextIndex = index - 1;

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
