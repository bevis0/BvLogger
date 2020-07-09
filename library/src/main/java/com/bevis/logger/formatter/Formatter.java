package com.bevis.logger.formatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Formatter<T> {
    public static <T> String format(T obj) {
        final FormatResult result = new FormatResult("");
        FormatBundle node = new FormatBundle<>(obj);
        node.getPipe().callResponse(new FormatPipe.FormatCallback() {
            @Override
            public void onResponse(Object response) {
                result.setOutput(response);
            }
        });
        node.format();
        return result.getOutputString();
    }

    static String toObjectString(Object obj) {
        if(isOriginalFormativeObject(obj)) {
            String objString = obj.toString();
            try {
                if (objString.startsWith("{")) {
                    return new JSONObject(objString).toString(4);
                } else if (objString.startsWith("[")) {
                    return new JSONArray(objString).toString(4);
                }
            } catch (JSONException ignore) {
            }
            return objString;
        } else  {
            throw new RuntimeException("toObjectString only accepts types JSONObject, JSONArray, and String");
        }
    }

    static boolean isOriginalFormativeObject(Object object) {
        return object instanceof JSONObject || object instanceof JSONArray || object instanceof String;
    }

    static boolean isJSONObject(Object object) {
        return object instanceof JSONObject || object instanceof JSONArray;
    }

    abstract boolean accept(Object obj);

    abstract void onFormat(FormatBundle<T> node);



    static class FormatResult {
        private Object output;

        public FormatResult(Object defValue) {
            output = defValue;
        }

        public void setOutput(Object output) {
            this.output = output;
        }

        public String getOutputString() {
            if(!isOriginalFormativeObject(output)) {
               return "";
            }
            return toObjectString(output);
        }
    }
}

