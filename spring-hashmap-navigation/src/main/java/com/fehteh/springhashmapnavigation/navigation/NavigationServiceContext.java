package com.fehteh.springhashmapnavigation.navigation;

import java.util.*;
import java.util.stream.Collectors;

public class NavigationServiceContext {

    int index = 0;

    private Object valueObj;
    Map<Integer, Integer> arrayIndex; // key -> index of where the array is; arrayIndex -> index inside of the array

    List<String> pathSplit;

    public NavigationServiceContext(String path, Object entryObj) {
        this.pathSplit = Arrays.asList(path.split("\\.+"));
        this.arrayIndex = new HashMap<>();
        this.valueObj = entryObj;
    }

    public String getCurrentPath() {
        return pathSplit.get(index);
    }

    public String getCurrentFullpath() {
        return pathSplit.subList(0, index + 1).stream().collect(Collectors.joining("."));
    }

    public boolean isLastElement() {
        return pathSplit.size() - 1 == index;
    }

    public int getIndex() {
        return index;
    }

    public void incIndex() {this.index++;}
    public void decIndex() {this.index--;}

    public int getArrayIndex() {
        return arrayIndex.size() > 0 && arrayIndex.containsKey(this.index) ? arrayIndex.get(this.index) : 0;
    }

    public void setArrayIndex(int arrayIndex) {
        this.arrayIndex.put(this.index, arrayIndex);
    }

    public Object getValueObj() {
        return valueObj;
    }

    public void setValueObj(Object valueObj) {
        this.valueObj = valueObj;
    }

    public boolean isValueObjTypeMap() {
        return valueObj instanceof Map<?, ?>;
    }

    public boolean isValueObjTypeCollection() {
        return valueObj instanceof Collection<?>;
    }
}
