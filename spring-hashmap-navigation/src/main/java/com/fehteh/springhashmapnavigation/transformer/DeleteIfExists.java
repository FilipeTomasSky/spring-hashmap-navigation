package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class DeleteIfExists extends AbstractTransformer {

    private String target;

    private List<String> targetPath;
    private boolean toApply;
    private int toApplyNextIndex;

    public DeleteIfExists(String target) {
        this.target = target;

        resetTransformer();
    }

    @Override
    public void runTransformer(String navigationElement, NavigationServiceContext ctx, Object value) {
        if(!toApply && ctx.isLastElement()) {
            if(value != null) {
                System.out.println("DeleteIfExists: should apply");
                toApply = true;
                toApplyNextIndex = ctx.index;
            }
        }

        if(toApply && ctx.index <= toApplyNextIndex) {
            if(!targetPath.get(0).equals("..")) {
                if (value instanceof Map<?, ?> map) {
                    System.out.println("DeleteIfExists: apply 1");

                    ((Map<String,Object>)map).remove(targetPath.get(0));
                }
                if(value instanceof Collection<?> arrayList) {
                    System.out.println("DeleteIfExists: apply 2");

                    ((ArrayList) arrayList).remove(ctx.getArrayIndex());
                    ctx.setArrayIndex(ctx.getArrayIndex() - 1);
                }
                resetTransformer();
            } else if(!(value instanceof Collection<?>)) {
                toApplyNextIndex = ctx.index - 1;

                if (targetPath.get(0).equals("..")) {
                    targetPath = targetPath.subList(1, targetPath.size());
                }
            }
        }
    }

    private void resetTransformer() {
        this.toApply = false;
        this.toApplyNextIndex = 0;
        this.targetPath = new ArrayList<>(Arrays.asList(target.split("/")));

        System.out.println("DeleteIfExists: reset");

    }
}
