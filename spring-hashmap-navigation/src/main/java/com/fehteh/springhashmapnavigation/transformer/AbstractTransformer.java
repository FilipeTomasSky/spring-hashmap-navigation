package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public abstract class AbstractTransformer {
    private boolean isMapType;
    private boolean isCollectionType;
    private boolean isStringType;
    private Object valueObj;

    String targetPath;
    List<String> targetPathList;
    boolean toApply;
    int toApplyNextIndex;

    public void run(String navigationElement, NavigationServiceContext ctx, Object valueObj) {
        this.valueObj = valueObj;
        print(ctx);
        checkValueObjClassType();

        //runTransformer(navigationElement, ctx, valueObj);
        runTransformer(navigationElement, ctx);
    }

    private void checkValueObjClassType() {
        if(valueObj instanceof Map<?,?>) {
            isMapType = true;
            isCollectionType = false;
            isStringType = false;
        } else if(valueObj instanceof Collection<?>) {
            isMapType = false;
            isCollectionType = true;
            isStringType = false;
        } else if(valueObj instanceof String) {
            isMapType = false;
            isCollectionType = false;
            isStringType = true;
        } else {
            isMapType = false;
            isCollectionType = false;
            isStringType = false;
        }
    }

    private void print(NavigationServiceContext ctx) {
        int index = ctx.index;
        int arrayIndex = ctx.getArrayIndex();
        boolean lastElement = ctx.isLastElement();
        String valueClass = valueObj != null ? valueObj.getClass().getSimpleName() : "null";

        System.out.println("AbstractTransformer: " + ctx.getCurrentFullpath() + " [key:" + ctx.getCurrentPath() + "]" + " [valueObj:" + valueClass + "]" + " [index:" + index + "]" + " [arrayIndex:" + arrayIndex + "]" + " [lastElement:" + lastElement + "]");
    }

    public boolean createPath(List<String> targetPathList, int nrOfElements) {
        if(isObjValueMapType()) {
            Map<?,?> map = (Map<?, ?>) valueObj;
            int limit = targetPathList.size() - nrOfElements;

            while(targetPathList.size() != limit) {
                String pathElement = targetPathList.get(0);

                if(map.get(pathElement) == null) {
                    ((Map<String,Object>)map).put(pathElement, new HashMap<String, Object>());
                }

                map = (Map<?, ?>)map.get(pathElement);
                targetPathList = targetPathList.subList(1, targetPathList.size());
            }

            valueObj = map;
            return true;
        }
        return false;
    }

    boolean putValue(String fieldName, Object newValue, boolean overrideField) {
        if(isObjValueMapType()) {
            Map<?,?> map = (Map<?, ?>)valueObj;

            if(!overrideField || map.get(fieldName) == null) {
                ((Map<String,Object>) map).put(fieldName, newValue);
            }

            valueObj = map;
            return true;
        }
        return false;
    }

    void resetTransformer() {
        this.toApply = false;
        this.toApplyNextIndex = 0;
        this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));
    }

    abstract void runTransformer(String navigationElement, NavigationServiceContext ctx);

    public boolean isObjValueMapType() {
        return isMapType;
    }

    public boolean isObjValueCollectionType() {
        return isCollectionType;
    }

    public boolean isObjValueStringType() {
        return isStringType;
    }

    public Object getValueObj() {
        return valueObj;
    }
}
