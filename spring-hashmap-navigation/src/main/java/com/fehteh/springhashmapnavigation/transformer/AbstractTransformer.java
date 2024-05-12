package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

public abstract class AbstractTransformer {

    public void apply(String navigationElement, NavigationServiceContext ctx) {
        print(ctx);

        applyTransformer(navigationElement, ctx);
    }

    abstract void applyTransformer(String navigationElement, NavigationServiceContext ctx);

    private static void print(NavigationServiceContext ctx) {
        int index = ctx.getIndex();
        int arrayIndex = ctx.getArrayIndex();
        boolean lastElement = ctx.isLastElement();
        Object valueObj = ctx.getValueObj();
        String valueClass = valueObj != null ? valueObj.getClass().getSimpleName() : "null";

        System.out.println("AbstractTransformer: " + ctx.getCurrentFullpath() + " [key:" + ctx.getCurrentPath() + "]" + " [valueObj:" + valueClass + "]" + " [index:" + index + "]" + " [arrayIndex:" + arrayIndex + "]" + " [lastElement:" + lastElement + "]");
    }

}
