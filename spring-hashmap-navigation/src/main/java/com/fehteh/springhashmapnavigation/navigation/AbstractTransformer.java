package com.fehteh.springhashmapnavigation.navigation;

public abstract class AbstractTransformer {

    public void notify(String navigationElement, NavigationServiceContext ctx, Object value) {
        int index = ctx.getIndex();
        int arrayIndex = ctx.getArrayIndex();
        boolean lastElement = ctx.isLastElement();
        String valueClass = value != null ? value.getClass().getSimpleName() : "null";

        System.out.println("AbstractTransformer: " + ctx.getCurrentFullpath() + " [key:" + ctx.getCurrentPath() + "]" + " [value:" + valueClass + "]" + " [index:" + index + "]" + " [arrayIndex:" + arrayIndex + "]" + " [lastElement:" + lastElement + "]");

        runTransfomer(navigationElement, ctx, value);
    }

    abstract void runTransfomer(String navigationElement, NavigationServiceContext ctx, Object value);
}
