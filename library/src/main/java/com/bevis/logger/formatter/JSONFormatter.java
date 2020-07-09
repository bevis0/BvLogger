package com.bevis.logger.formatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONFormatter extends Formatter<Object> {
    @Override
    public boolean accept(Object object) {
        return object instanceof JSONObject || object instanceof JSONArray;
    }

    @Override
    public void onFormat(FormatBundle<Object> node) {
        String json = node.obj.toString();
        try {
            if (json.startsWith("{")) {
                node.onResponse(new JSONObject(json).toString(4));
                return;
            } else if (json.startsWith("[")) {
                node.onResponse(new JSONArray(json).toString(4));
                return;
            }
        } catch (JSONException ignore) {
        }
        node.onResponse(json);
    }
}
