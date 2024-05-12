package com.fehteh.springhashmapnavigation.navigation;

import com.fehteh.springhashmapnavigation.transformer.AbstractTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class NavigationService {

    public void navigateAndApplyRecursive(Map<String, Object> entryObj, String path, AbstractTransformer transformer) {
        navigateAndApplyRecursive(new NavigationServiceContext(path, entryObj), transformer);
    }

    private void navigateAndApplyRecursive(NavigationServiceContext context, AbstractTransformer transformer) {
        Object entryObj = context.getValueObj();

        if (entryObj instanceof Map<?, ?> map) {
            String childrenName = context.getCurrentPath();
            Object childrenObject = map.get(childrenName);
            context.setValueObj(childrenObject);
            navigateApplyAndNotify(context, transformer);
        }

        if(entryObj instanceof Collection<?> arrayList) {
            context.decIndex();

            for(context.setArrayIndex(0); context.getArrayIndex() < arrayList.size(); context.setArrayIndex(context.getArrayIndex() + 1)) {
                Object childrenObject = ((ArrayList<?>) arrayList).get(context.getArrayIndex());

                context.setValueObj(childrenObject);
                navigateApplyAndNotify(context, transformer);

                context.setValueObj(entryObj);
                transformer.apply(context.getCurrentPath(), context);
            }

            context.setArrayIndex(context.getArrayIndex() - 1);
            context.incIndex();
        }
    }

    private void navigateApplyAndNotify(NavigationServiceContext context, AbstractTransformer transformer) {
        transformer.apply(context.getCurrentPath(), context);

        if(!context.isLastElement()) {
            context.incIndex();
            navigateAndApplyRecursive(context, transformer);
            context.decIndex();
            transformer.apply(context.getCurrentPath(), context);
        }
    }
}
