package com.fehteh.springhashmapnavigation.transformer;

public class ConvertValue {
    private ConvertValue(){
    }

    public static Double toDouble(Object rawValue, Double defaultValue) {

        if (rawValue == null) {
            return defaultValue;
        } else if (rawValue instanceof String) {
            String convertString = String.valueOf(rawValue);
            if (convertString.isBlank()) {
                return defaultValue;
            }
            return Double.valueOf(convertString);
        } else if (rawValue instanceof Double doubleValue) {
            return doubleValue;
        } else if (rawValue instanceof Integer integerValue) {
            return integerValue.doubleValue();
        }

        return defaultValue;
    }

    public static Integer toInteger(String str, Integer defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException numberFormatException) {
                return defaultValue;
            }
        }
    }

    public static Object to(String value, String type) {
        if (type == null) {
            return value;
        }

        if (type.equals("boolean")) {
            return Boolean.parseBoolean(value);
        }

        return value;
    }

    public static String pascalToSnake(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Use regular expression to match uppercase letters and insert an underscore before them.
        String snakeCase = input.replaceAll("([a-z])([A-Z])", "$1_$2");

        // Convert the result to lowercase.
        return snakeCase.toLowerCase();
    }
}
