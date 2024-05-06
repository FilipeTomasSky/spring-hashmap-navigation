package com.fehteh.springhashmapnavigation.navigation;

import com.fehteh.springhashmapnavigation.transformer.AbstractTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class NavigationService {

    public void navigateAndApply(Map<String, Object> metadata, String path, AbstractTransformer transformer) {
        navigateAndApply(metadata, new NavigationServiceContext(path), transformer);
    }

    private void navigateAndApply(Object entryObject, NavigationServiceContext context, AbstractTransformer transformer) {
        if (entryObject instanceof Map<?, ?> map) {
            String childrenName = context.getCurrentPath();
            Object childrenObject = map.get(childrenName);
            navigateApplyAndNotify(childrenObject, context, transformer);
        }

        if(entryObject instanceof Collection<?> arrayList) {
            context.decIndex();
            for(context.setArrayIndex(0); context.getArrayIndex() < arrayList.size(); context.setArrayIndex(context.getArrayIndex() + 1))
            {
                Object childrenObject = ((ArrayList<?>) arrayList).get(context.getArrayIndex());
                navigateApplyAndNotify(childrenObject, context, transformer);
                transformer.notify(context.getCurrentPath(), context, entryObject);
            }
            context.incIndex();
            context.setArrayIndex(0);
        }
    }

    private void navigateApplyAndNotify(Object childrenObject, NavigationServiceContext context, AbstractTransformer transformer) {
        transformer.notify(context.getCurrentPath(), context, childrenObject);

        if(!context.isLastElement()) {
            context.incIndex();
            navigateAndApply(childrenObject, context, transformer);
            context.decIndex();
            transformer.notify(context.getCurrentPath(), context, childrenObject);
        }
    }
}
