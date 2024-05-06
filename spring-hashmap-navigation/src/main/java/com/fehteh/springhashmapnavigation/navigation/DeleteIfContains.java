package com.fehteh.springhashmapnavigation.navigation;

import java.util.*;

public class DeleteIfContains extends AbstractTransformer {

    private String target;

    private List<String> targetPath;
    private boolean contains;

    public DeleteIfContains(String target) {
        this.target = target;

        resetTransformer();
    }

    @Override
    public void runTransfomer(String navigationElement, NavigationServiceContext ctx, Object value) {
        if(contains) {
            if(targetPath.size() > 0 && targetPath.get(0).equals("..")) {
                targetPath = targetPath.subList(1, targetPath.size());
            }
            else {
                if (value instanceof Map<?, ?> map) {
                    ((Map<String,Object>)map).remove(targetPath.get(0));
                }
                if(value instanceof Collection<?> arrayList) {
                    ((ArrayList) arrayList).remove(ctx.getArrayIndex());
                    ctx.setArrayIndex(ctx.getArrayIndex() - 1);
                }
                resetTransformer();
            }
        }

        if(ctx.isLastElement()) {
            if(value == null) {
                contains = true;
                System.out.println("DeleteIfContainsTransformer: " + ctx.getCurrentPath() + " [contains:" + true + "]");
            }
        }
    }

    private void resetTransformer() {
        this.contains = false;
        this.targetPath = new ArrayList<>(Arrays.asList(target.split("/")));
    }
}
