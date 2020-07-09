package com.bevis.logger.formatter;

import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

class BundleFormatter extends Formatter<Bundle> {
    @Override
    boolean accept(Object obj) {
        return obj instanceof Bundle;
    }

    @Override
    void onFormat(FormatBundle<Bundle> node) {
        final JSONObject json = new JSONObject();
        Set<String> allKey = node.obj.keySet();
        if(!allKey.isEmpty()) {
            for(final String key: allKey) {
                Object value = node.obj.get(key);
                if(isJSONObject(value)) {
                    try {
                        json.put(key, value);
                    } catch (JSONException ignore) {
                    }
                } else {
                    node.onFork(value, new FormatPipe.FormatCallback() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                json.put(key, response);
                            } catch (JSONException ignore) {
                            }
                        }
                    });
                }
            }
        }
        node.onResponse(json);
    }
}
