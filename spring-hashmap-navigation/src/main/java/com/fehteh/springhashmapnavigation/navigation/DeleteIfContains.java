package com.fehteh.springhashmapnavigation.navigation;

import java.util.*;

public class DeleteIfContains extends AbstractTransformer {

    private String target;

    private List<String> targetPath;
    private boolean toApply;
    private int toApplyNextIndex;

    public DeleteIfContains(String target) {
        this.target = target;

        resetTransformer();
    }

    @Override
    public void runTransfomer(String navigationElement, NavigationServiceContext ctx, Object value) {
        if(!toApply && ctx.isLastElement()) {
            if(value != null) {
                System.out.println("DeleteIfContains: should apply");
                toApply = true;
                toApplyNextIndex = ctx.getIndex();
            }
        }

        if(toApply && ctx.getIndex() <= toApplyNextIndex) {
            if(!targetPath.get(0).equals("..")) {
                if (value instanceof Map<?, ?> map) {
                    System.out.println("DeleteIfContains: apply 1");

                    ((Map<String,Object>)map).remove(targetPath.get(0));
                }
                if(value instanceof Collection<?> arrayList) {
                    System.out.println("DeleteIfContains: apply 2");

                    ((ArrayList) arrayList).remove(ctx.getArrayIndex());
                    ctx.setArrayIndex(ctx.getArrayIndex() - 1);
                }
                resetTransformer();
            } else if(!(value instanceof Collection<?>)) {
                toApplyNextIndex = ctx.getIndex() - 1;

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

        System.out.println("DeleteIfContains: reset");

    }
}
