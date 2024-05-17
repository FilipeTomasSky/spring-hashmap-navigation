package com.fehteh.springhashmapnavigation.navigation;

import java.util.*;
import java.util.stream.Collectors;

public class NavigationServiceContext {
    public int pathLevel = 0;
    /**
     * HashMap to track array iteration indices.
     * This map helps in managing array iteration by keeping track of the current index position for each array level in the path.
     * Key: Represents the level of the array in the navigationPath
     * Value: Represents the last index of the array being iterated at that level
     */
    Map<Integer, Integer> arrayIterationIndexMap;

    List<String> navigationPath;

    public NavigationServiceContext(String path) {
        this.navigationPath = Arrays.asList(path.split("\\.+"));
        this.arrayIterationIndexMap = new HashMap<>();
    }

    public String getCurrentPath() {
        return navigationPath.get(pathLevel);
    }

    public String getCurrentFullPath() {
        return navigationPath.subList(0, pathLevel + 1).stream().collect(Collectors.joining("."));
    }

    public boolean isLastElement() {
        return navigationPath.size() - 1 == pathLevel;
    }

    public void incPathLevel() {this.pathLevel++;}
    public void decPathLevel() {this.pathLevel--;}

    public int getLastArrayIterationIndexMap() {
        return arrayIterationIndexMap.size() > 0 && arrayIterationIndexMap.containsKey(this.pathLevel) ? arrayIterationIndexMap.get(this.pathLevel) : 0;
    }
    public void setLastArrayIterationIndexMap(int arrayIterationIndexMap) {
        this.arrayIterationIndexMap.put(this.pathLevel, arrayIterationIndexMap);
    }

}
