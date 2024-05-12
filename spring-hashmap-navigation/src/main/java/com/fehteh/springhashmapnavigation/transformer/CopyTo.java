package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class CopyTo extends AbstractTransformer {
    private final String targetPath;
    private Object newValue;

    private List<String> targetPathList;
    private boolean toApply;
    private int toApplyNextIndex;

    private List<Object> accumulatorArray = new ArrayList<>();


    public CopyTo(String targetPath) {
        this.targetPath = targetPath;

        resetTransformer();
    }

    @Override
    public void applyTransformer(String navigationElement, NavigationServiceContext ctx) {
        Object valueObj = ctx.getValueObj();
        int index = ctx.getIndex();
        
        if(!toApply && ctx.isLastElement() || ctx.isLastElement() && ctx.isValueObjTypeCollection()) {
            if(valueObj != null) {
                toApply = true;
                toApplyNextIndex = index;
                newValue = valueObj;
                this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));

                if(ctx.isValueObjTypeCollection()) {
                    if(newValue instanceof Collection<?>) {
                        accumulatorArray.addAll((Collection<?>) newValue);
                    } else {
                        accumulatorArray.add(newValue);
                    }
                }
            }
        }

        if(toApply && index <= toApplyNextIndex) {
            System.out.println("trying to apply");

            if(ctx.isLastElement() || !ctx.isValueObjTypeCollection()) {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = index - 1;
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    createPath(ctx);
                }
            }
        }
    }

    private void createPath(NavigationServiceContext ctx) {
        if (ctx.isValueObjTypeMap()) {
            Map<?, ?> map = (Map<?, ?>) ctx.getValueObj();;
            String pathElement = targetPathList.get(0);

            while(targetPathList.size() != 1) {
                if(map.get(pathElement) == null) {
                    ((Map<String,Object>)map).put(targetPathList.get(0), new HashMap<String, Object>());
                }

                map = (Map<?, ?>)map.get(pathElement);
                targetPathList = targetPathList.subList(1, targetPathList.size());
            }

            if(!accumulatorArray.isEmpty()) {
                ArrayList<Object> array = ((Map<String, ArrayList<Object>>) map).get(pathElement);
                if(array == null) {
                    array = new ArrayList<>();
                    ((Map<String, ArrayList<Object>>) map).put(targetPathList.get(0), array);
                }
                array.addAll(accumulatorArray);
                accumulatorArray = new ArrayList<>();
            } else {
                ((Map<String,Object>)map).put(targetPathList.get(0), newValue);
            }

            resetTransformer();
        }
    }

    private void resetTransformer() {
        this.toApply = false;
        this.toApplyNextIndex = 0;
        this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));
    }
}
