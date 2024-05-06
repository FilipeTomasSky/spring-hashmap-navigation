package com.fehteh.springhashmapnavigation.navigation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationServiceContext {
    private int index = 0;
    private int arrayIndex = 0;

    private List<String> pathSplit;

    public NavigationServiceContext(String path) {
        this.pathSplit = Arrays.asList(path.split("\\.+"));
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

    public void incIndex() {index++;}
    public void decIndex() {index--;}

    public int getArrayIndex() {
        return arrayIndex;
    }
    public void setArrayIndex(int index) {
        this.arrayIndex = index;
    }

}
