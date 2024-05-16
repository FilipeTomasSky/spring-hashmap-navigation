package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;

import java.util.*;

public abstract class AbstractTransformer {
    private boolean isMapType;
    private boolean isCollectionType;
    private boolean isStringType;

    private Object valueObj;

    /**
     * {@code targetPath} - String Path in which the Transformer can navigate to. Element's path are separated by / and
     * .. to move back (move up one level).
     * Can add new levels to the path.
     * The format String must be relative to the {@code path} from NavigationService
     *
     * @Example
     *
     * To move to the path "metadata.products"
     *<pre>{@code
     * path = "metadata.products.relevantContext.offers"
     * targetPath = "../.."
     *}</pre>
     *
     * To move to the path "metadata.product.name.id" creating the path "name.id" that doesn't exist
     *<pre>{@code
     * path = "metadata.product"
     * targetPath = "name/id"
     *}</pre>
     *
     *
     */
    String targetPath;
    List<String> targetPathList;

    boolean toApply;
    int toApplyNextIndex;

    public void run(String navigationElement, NavigationServiceContext ctx, Object valueObj) {
        this.valueObj = valueObj;
        print(ctx);
        checkValueObjClassType();

        runTransformer(navigationElement, ctx);
    }

    /**
     * Checks actions and/or conditions to apply for the current valueObj.
     * It can navigate the valueObj by updating the NavigationServiceContext.
     *
     * @param navigationElement Current navigation element
     * @param ctx NavigationServiceContext that allows to have a navigation in the valueObj
     */
    abstract void runTransformer(String navigationElement, NavigationServiceContext ctx);

    /**
     * Creates a path for the current valueObj. The valueObj is set with the path inserted accordingly.
     * If a path element already exists, it won't create/override it, it will move forward to check the rest of the path elements
     *
     * @param targetPathList List with the path elements
     * @param nrOfElements Number of path elements. If nrOfElements = 3 and targetPathList has 5 elements, it will insert
     * @return {@code true} if base valueObj is a Map, {@code false} otherwise
     */
    protected boolean createPath(List<String> targetPathList, int nrOfElements) {
        if(isObjValueMapType()) {
            Map<?,?> map = (Map<?, ?>) valueObj;
            int limit = targetPathList.size() - nrOfElements;

            while(targetPathList.size() != limit) {
                String pathElement = targetPathList.get(0);

                if(map.get(pathElement) == null) {
                    ((Map<String,Object>)map).put(pathElement, new HashMap<String, Object>());
                }

                map = (Map<?, ?>)map.get(pathElement);
                targetPathList = targetPathList.subList(1, targetPathList.size());
            }

            valueObj = map;
            return true;
        }
        return false;
    }

    /**
     * Puts a value in the current valueObj if valueObj for a given field. It can either override the field or not if the field already exists.
     *
     * @param fieldName The name of the field (key) to insert
     * @param newValue The value to insert for the given field
     * @param overrideField true if field exists, and it's to be overridden or false otherwise
     * @return {@code true} if base valueObj is a Map, {@code false} otherwise
     */

    protected boolean putValue(String fieldName, Object newValue, boolean overrideField) {
        if(isObjValueMapType()) {
            Map<?,?> map = (Map<?, ?>)valueObj;

            if(!overrideField || map.get(fieldName) == null) {
                ((Map<String,Object>) map).put(fieldName, newValue);
            }

            valueObj = map;
            return true;
        }
        return false;
    }

    /**
     * Resets {@code toApply}, {@code toApplyNextIndex}, {@code targetPathList}
     */
    void resetTransformer() {
        this.toApply = false;
        this.toApplyNextIndex = 0;
        this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));
    }


    /**
     * Checks if the objValue is instance of Map<?,?>
     *
     * @return  {@code true} if valueObj is Map, {@code false} otherwise.
     */
    public boolean isObjValueMapType() {
        return isMapType;
    }

    /**
     * Checks if the objValue is instance of Collection<?>
     *
     * @return  {@code true} if objValue is Collection, {@code false} otherwise.
     */
    public boolean isObjValueCollectionType() {
        return isCollectionType;
    }

    /**
     * Checks if the objValue is instance of String
     *
     * @return  {@code true} if objValue is String, {@code false} otherwise.
     */
    public boolean isObjValueStringType() {
        return isStringType;
    }

    public Object getValueObj() {
        return valueObj;
    }

    private void checkValueObjClassType() {
        if(valueObj instanceof Map<?,?>) {
            isMapType = true;
            isCollectionType = false;
            isStringType = false;
        } else if(valueObj instanceof Collection<?>) {
            isMapType = false;
            isCollectionType = true;
            isStringType = false;
        } else if(valueObj instanceof String) {
            isMapType = false;
            isCollectionType = false;
            isStringType = true;
        } else {
            isMapType = false;
            isCollectionType = false;
            isStringType = false;
        }
    }

    private void print(NavigationServiceContext ctx) {
        int index = ctx.index;
        int arrayIndex = ctx.getArrayIndex();
        boolean lastElement = ctx.isLastElement();
        String valueClass = valueObj != null ? valueObj.getClass().getSimpleName() : "null";

        System.out.println("AbstractTransformer: " + ctx.getCurrentFullpath() + " [key:" + ctx.getCurrentPath() + "]" + " [valueObj:" + valueClass + "]" + " [index:" + index + "]" + " [arrayIndex:" + arrayIndex + "]" + " [lastElement:" + lastElement + "]");
    }
}
