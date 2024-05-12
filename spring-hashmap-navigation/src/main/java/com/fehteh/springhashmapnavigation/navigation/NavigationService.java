package com.fehteh.springhashmapnavigation.navigation;

import com.fehteh.springhashmapnavigation.transformer.AbstractTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class NavigationService {

    public void navigateAndApply(Map<String, Object> metadata, String path, AbstractTransformer transformer) {
        navigateAndApplyRecursive(metadata, new NavigationServiceContext(path), transformer);
    }

    private void navigateAndApplyRecursive(Object entryObject, NavigationServiceContext context, AbstractTransformer transformer) {
        if (entryObject instanceof Map<?, ?> map) {
            String childrenName = context.getCurrentPath();
            Object childrenObject = map.get(childrenName);
            runTransformer(childrenObject, context, transformer);
        }

        if(entryObject instanceof Collection<?> arrayList) {
            context.decIndex();

            for(context.setArrayIndex(0); context.getArrayIndex() < arrayList.size(); context.setArrayIndex(context.getArrayIndex() + 1)) {
                Object childrenObject = ((ArrayList<?>) arrayList).get(context.getArrayIndex());
                runTransformer(childrenObject, context, transformer);
                transformer.run(context.getCurrentPath(), context, entryObject);
            }

            context.setArrayIndex(context.getArrayIndex() - 1);
            context.incIndex();
        }
    }

    private void runTransformer(Object childrenObject, NavigationServiceContext context, AbstractTransformer transformer) {
        transformer.run(context.getCurrentPath(), context, childrenObject);

        if(!context.isLastElement()) {
            context.incIndex();
            navigateAndApplyRecursive(childrenObject, context, transformer);

            context.decIndex();
            transformer.run(context.getCurrentPath(), context, childrenObject);
        }
    }
}
