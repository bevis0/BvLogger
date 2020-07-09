package com.bevis.logger.formatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

class MapEntryFormatter extends Formatter<Map.Entry> {
    @Override
    boolean accept(Object obj) {
        return obj instanceof Map.Entry;
    }

    @Override
    public void onFormat(FormatBundle<Map.Entry> node) {
        final JSONObject json = new JSONObject();
        Object key = node.obj.getKey();

        if (isJSONObject(key)) {
            try {
                json.put("key", key);
            } catch (JSONException ignore) {
            }
        } else {
            node.onFork(node.obj.getKey(), new FormatPipe.FormatCallback() {
                @Override
                public void onResponse(Object response) {
                    try {
                        json.put("key", response);
                    } catch (JSONException ignore) {
                    }
                }
            });
        }
        Object value = node.obj.getValue();
        if(isJSONObject(value)) {
            try {
                json.put("value", value);
            } catch (JSONException ignore) {
            }
        } else {
            node.onFork(node.obj.getValue(), new FormatPipe.FormatCallback() {
                @Override
                public void onResponse(Object response) {
                    try {
                        json.put("value", response);
                    } catch (JSONException ignore) {
                    }
                }
            });
        }
        node.onResponse(json);
    }


}
