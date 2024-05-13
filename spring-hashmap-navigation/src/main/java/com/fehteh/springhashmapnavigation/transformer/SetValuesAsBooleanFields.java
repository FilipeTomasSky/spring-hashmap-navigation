package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public class SetValuesAsBooleanFields extends AbstractTransformer {

    private final String targetPath;
    private final Object newValue;
    private List<String> newFieldNames = new ArrayList<>();
    private List<String> targetPathList;
    private int toApplyNextIndex;
    private boolean toApply;

    public SetValuesAsBooleanFields(String targetPath, Object newValue) {
        this.targetPath = targetPath;
        this.newValue = newValue;

        resetTransformer();
    }
    @Override
    void runTransformer(String navigationElement, NavigationServiceContext ctx, Object valueObj) {
        if(ctx.isLastElement() && valueObj instanceof String valueStr) {
            if(!toApply) {
                toApply = true;
                toApplyNextIndex = ctx.index;
            }
            newFieldNames.add(formatToCamelCase(valueStr));
        }

        if(toApply && ctx.index <= toApplyNextIndex) {
            if(valueObj instanceof Collection<?>) {
                return;
            }

            if(targetPathList.size() == 0) {
                createPath(valueObj);
            } else {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = ctx.index - 1;
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    createPath(valueObj);
                }
            }
        }
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

    private void createPath(Object valueObj) {
        if(valueObj instanceof Map<?, ?> map) {
            while(targetPathList.size() != 0) {
                Object pathElement = targetPathList.get(0);

                if(map.get(pathElement) == null) {
                    ((Map<String,Object>)map).put(targetPathList.get(0), new HashMap<String, Object>());
                }

                map = (Map<?, ?>)map.get(pathElement);
                targetPathList = targetPathList.subList(1, targetPathList.size());
            }

            for (String fieldName : newFieldNames) {
                if(map.get(fieldName) == null) {
                    ((Map<String,Object>)map).put(fieldName, newValue);
                }
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
