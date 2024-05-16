package com.fehteh.springhashmapnavigation.transformer;

import com.fehteh.springhashmapnavigation.navigation.NavigationServiceContext;
import java.util.*;

/**
 * Copies fields or arrays from a origin path to a destination path. The destination path is relative to the origin path.
 * You can move level up or level down - creating new paths - (move backward or forward) on the origin path to have a destination.
 * The field can also be an Object.
 *
 * @Example
 * <pre>
 * 1. Copying fields:
 *{@code
 * metadata.productCount.count = 3
 * path = "metadata.productCount.count"
 * targetPath = "../../newField"
 *}
 * The object will have this new path with the value {@code metadata.newField = 3}
 *
 *
 * 2. Copying Arrays in Arrays:
 *{@code
 * metadata.products.relevantContext.offers = [
 *      {staticId: D2C_OFFER_MONTH}
 *      {staticId: D2C_OFFER_MONTH}
 *      {staticId: D2C_OFFER_DAY_2}
 * ]
 * path = "metadata.products.relevantContext.offers"
 * targetPath = "../../relevantContext/newOffers"
 *}
 * Since "products" is an array, all elements of the array will have this new path if the offers array are not null.
 * Each "products" element will have this new path with the value
 *{@code metadata.product.relevantContext.newOffers = [
 *     {staticId: D2C_OFFER_MONTH}
 *     {staticId: D2C_OFFER_MONTH}
 *     {staticId: D2C_OFFER_DAY_2}
 *]}
 * Note: if "relevantContext" already exists in the path, it won't override the value, it will move forward in the path
 *
 *
 * 3. Copying Array elements to outside of the Array:
 * {@code
 * metadata.products.relevantContext.offers = [
 *      {staticId: D2C_OFFER_MONTH}
 *      {staticId: D2C_OFFER_MONTH}
 *      {staticId: D2C_OFFER_DAY_2}
 * ]
 * path = "metadata.products.relevantContext.offers"
 * targetPath = "../../../newOffers"
 *}
 * Result (if the newOffers already exists it will override the value
 *{@code metadata.newOffers = [
 *     {staticId: D2C_OFFER_MONTH}
 *     {staticId: D2C_OFFER_MONTH}
 *     {staticId: D2C_OFFER_DAY_2}
 *]}
 * </pre>
 */
public class CopyTo extends AbstractTransformer {
    private Object newValue;
    private List<Object> accumulatorArray = new ArrayList<>();


    public CopyTo(String targetPath) {
        this.targetPath = targetPath;

        resetTransformer();
    }

    @Override
    void runTransformer(String navigationElement, NavigationServiceContext ctx) {
        if(!toApply && ctx.isLastElement() || ctx.isLastElement() && isObjValueCollectionType()) {
            if(getValueObj() != null) {
                toApply = true;
                toApplyNextIndex = ctx.index;
                newValue = getValueObj();
                this.targetPathList = new ArrayList<>(Arrays.asList(targetPath.split("/")));

                if(isObjValueCollectionType()) {
                    accumulatorArray.addAll((Collection<?>)newValue);
                }
            }
        }

        if(toApply && ctx.index <= toApplyNextIndex) {
            if(ctx.isLastElement() || !(isObjValueCollectionType())) {
                if(targetPathList.get(0).equals("..")) {
                    toApplyNextIndex = ctx.index - 1;
                    targetPathList = targetPathList.subList(1, targetPathList.size());
                } else {
                    if(createPath(targetPathList, targetPathList.size()-1)) {
                        putValue(targetPathList.get(targetPathList.size()-1), newValue, true);
                        resetTransformer();
                    }
                }
            }
        }
    }

    @Override
    protected boolean putValue(String fieldName, Object newValue, boolean overrideField) {
        Map<?,?> map = (Map<?, ?>) getValueObj();

        if(!accumulatorArray.isEmpty()) {
            ArrayList<Object> array = ((Map<String, ArrayList<Object>>) map).get(fieldName);

            if(array == null) {
                array = new ArrayList<>();
                super.putValue(fieldName, array, overrideField);
            }

            array.addAll(accumulatorArray);
            accumulatorArray = new ArrayList<>();
            return true;
        } else {
            return super.putValue(fieldName, newValue, overrideField);
        }
    }
}
