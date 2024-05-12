package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

public abstract class AbstractTransformer {
    public void run(String navigationElement, NavigationServiceContext ctx, Object valueObj) {
        print(ctx, valueObj);

        runTransformer(navigationElement, ctx, valueObj);
    }

    private static void print(NavigationServiceContext ctx, Object valueObj) {
        int index = ctx.index;
        int arrayIndex = ctx.getArrayIndex();
        boolean lastElement = ctx.isLastElement();
        String valueClass = valueObj != null ? valueObj.getClass().getSimpleName() : "null";

        System.out.println("AbstractTransformer: " + ctx.getCurrentFullpath() + " [key:" + ctx.getCurrentPath() + "]" + " [valueObj:" + valueClass + "]" + " [index:" + index + "]" + " [arrayIndex:" + arrayIndex + "]" + " [lastElement:" + lastElement + "]");
    }

    abstract void runTransformer(String navigationElement, NavigationServiceContext ctx, Object value);
}
