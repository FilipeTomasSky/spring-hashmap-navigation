package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetIfMissing extends AbstractTransformer {

    private final String target;
    private final Object targetValue;

    private List<String> targetPath;
    private boolean isMissing;

    public SetIfMissing(String target, Object targetValue) {
        this.target = target;
        this.targetValue = targetValue;

        resetTransformer();
    }


    @Override
    public void runTransfomer(String navigationElement, NavigationServiceContext ctx, Object value) {
        if(ctx.isLastElement()) {
            if(value == null) {
                isMissing = true;
            }
        }

        if(isMissing) {
            if(targetPath.get(0).equals("..")) {
                targetPath = targetPath.subList(1, targetPath.size());
            }
            else {
                if (value instanceof Map<?, ?> map) {
                    ((Map<String,Object>)map).put(targetPath.get(0), targetValue);
                }
                resetTransformer();
            }
        }
    }

    private void resetTransformer() {
        this.isMissing = false;
        this.targetPath = new ArrayList<>(Arrays.asList(target.split("/")));
    }
}
