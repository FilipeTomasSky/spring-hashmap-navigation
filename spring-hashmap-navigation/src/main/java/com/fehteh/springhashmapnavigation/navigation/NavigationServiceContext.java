package com.fehteh.springhashmapnavigation.navigation;

import java.util.*;
import java.util.stream.Collectors;

public class NavigationServiceContext {
    public int index = 0;
    Map<Integer, Integer> arrayIndex;

    List<String> pathSplit;

    public NavigationServiceContext(String path) {
        this.pathSplit = Arrays.asList(path.split("\\.+"));
        this.arrayIndex = new HashMap<>();
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

    public void incIndex() {this.index++;}
    public void decIndex() {this.index--;}

    public int getArrayIndex() {
        return arrayIndex.size() > 0 && arrayIndex.containsKey(this.index) ? arrayIndex.get(this.index) : 0;
    }
    public void setArrayIndex(int arrayIndex) {
        this.arrayIndex.put(this.index, arrayIndex);
    }

}
