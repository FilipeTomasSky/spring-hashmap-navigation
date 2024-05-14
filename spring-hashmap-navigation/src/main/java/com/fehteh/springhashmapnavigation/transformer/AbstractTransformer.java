package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractTransformer {
    private boolean isMapType;
    private boolean isCollectionType;

    public void run(String navigationElement, NavigationServiceContext ctx, Object valueObj) {
        print(ctx, valueObj);
        checkClassType(valueObj);

        runTransformer(navigationElement, ctx, valueObj);
    }

    private void checkClassType(Object valueObj) {
        if(valueObj instanceof Map<?,?>) {
            isMapType = true;
            isCollectionType = false;
        } else if(valueObj instanceof Collection<?>) {
            isMapType = false;
            isCollectionType = true;
        } else {
            isMapType = false;
            isCollectionType = false;
        }
    }

    private static void print(NavigationServiceContext ctx, Object valueObj) {
        int index = ctx.index;
        int arrayIndex = ctx.getArrayIndex();
        boolean lastElement = ctx.isLastElement();
        String valueClass = valueObj != null ? valueObj.getClass().getSimpleName() : "null";

        System.out.println("AbstractTransformer: " + ctx.getCurrentFullpath() + " [key:" + ctx.getCurrentPath() + "]" + " [valueObj:" + valueClass + "]" + " [index:" + index + "]" + " [arrayIndex:" + arrayIndex + "]" + " [lastElement:" + lastElement + "]");
    }

    abstract void runTransformer(String navigationElement, NavigationServiceContext ctx, Object valueObj);

    public boolean isObjValueMapType() {
        return isMapType;
    }

    public boolean isObjValueCollectionType() {
        return isCollectionType;
    }
}
