package com.bevis.logger.formatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONStringFormatter extends Formatter<String> {
    @Override
    boolean accept(Object obj) {
        return obj instanceof String && isJSON((String) obj);
    }

    private boolean isJSON(String json) {
        Object tryObj = null;
        try {
            if (json.trim().startsWith("{")) {
                tryObj = new JSONObject(json);
            } else if (json.trim().startsWith("[")) {
                tryObj = new JSONArray(json);
            }
        } catch (JSONException e) {
            tryObj = null;
        }
        return tryObj != null;
    }

    @Override
    void onFormat(FormatBundle<String> node) {
        String json = node.obj;
        try {
            if (json.startsWith("{")) {
                node.onResponse(new JSONObject(json));
                return;
            } else if (json.startsWith("[")) {
                node.onResponse(new JSONArray(json));
                return;
            }
        } catch (JSONException ignore) {
        }
        node.onResponse(json);
    }
}
