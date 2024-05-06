package com.fehteh.springhashmapnavigation.navigation;

import java.util.*;

public class SetIfMissing extends AbstractTransformer {

    private final String target;
    private final Object targetValue;

    private List<String> targetPath;
    private boolean toApply;
    private int toApplyNextIndex;

    public SetIfMissing(String target, Object targetValue) {
        this.target = target;
        this.targetValue = targetValue;

        resetTransformer();
    }


    @Override
    public void runTransfomer(String navigationElement, NavigationServiceContext ctx, Object value) {
        if(!toApply && ctx.isLastElement()) {
            if(value == null) {
                toApply = true;
                toApplyNextIndex = ctx.getIndex();
            }
        }

        if(toApply && ctx.getIndex() <= toApplyNextIndex) {
            if(!(value instanceof Collection<?>)) {
                toApplyNextIndex = ctx.getIndex() - 1;

                if(targetPath.get(0).equals("..")) {
                    targetPath = targetPath.subList(1, targetPath.size());
                } else {
                    if (value instanceof Map<?, ?> map) {
                        ((Map<String,Object>)map).put(targetPath.get(0), targetValue);
                        resetTransformer();
                    }
                }
            }
        }
    }

    private void resetTransformer() {
        this.toApply = false;
        this.toApplyNextIndex = 0;
        this.targetPath = new ArrayList<>(Arrays.asList(target.split("/")));
    }
}
