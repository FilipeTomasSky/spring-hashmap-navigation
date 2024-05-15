package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class SetValuesAsBooleanFields extends AbstractTransformer {
    private final Object newValue;
    private List<String> newFieldNames = new ArrayList<>();

    public SetValuesAsBooleanFields(String targetPath, Object newValue) {
        this.targetPath = targetPath;
        this.newValue = newValue;

        resetTransformer();
    }

    @Override
    void runTransformer(String navigationElement, NavigationServiceContext ctx) {
        if(ctx.isLastElement() && isObjValueStringType()) {
            if(!toApply) {
                toApply = true;
                toApplyNextIndex = ctx.index;
            }
            newFieldNames.add(formatToCamelCase((String) getValueObj()));
        }

        if(toApply && ctx.index <= toApplyNextIndex) {
            if(isObjValueCollectionType()) {
                return;
            }

            if(targetPathList.size() == 0) {
                putValue(null, newValue, false);
                resetTransformer();
            } else {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = ctx.index - 1;
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    if(createPath(targetPathList, targetPathList.size())) {
                        putValue(null, newValue, false);
                        resetTransformer();
                    }
                }
            }
        }
    }

    @Override
    boolean putValue(String fieldName, Object newValue, boolean overrideField) {
        for (String field : newFieldNames) {
            super.putValue(field, newValue, false);
        }
        return false;
    }

    public static String formatToCamelCase(String input) {
        if (!input.contains("_") && !input.contains(" ")) {
            return input;
        }

        String[] parts = input.split("[_\\s]+");
        StringBuilder formattedString = new StringBuilder();
        formattedString.append(parts[0].toLowerCase());

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            formattedString.append(Character.toUpperCase(part.charAt(0)));
            formattedString.append(part.substring(1).toLowerCase());
        }

        return formattedString.toString();
    }
}
