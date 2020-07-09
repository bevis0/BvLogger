package com.bevis.logger.formatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * 用于提前拦截一些可以自己处理的内容
 */
class IterableFormatter extends Formatter<Iterable> {
    @Override
    boolean accept(Object obj) {
        return obj instanceof Iterable;
    }
    @Override
    public void onFormat(FormatBundle<Iterable> node) {
        final JSONArray parent = new JSONArray();
        Iterator iterator = node.obj.iterator();
        if(iterator != null) {
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if(obj instanceof JSONObject || obj instanceof JSONArray) {
                    parent.put(obj);
                } else  {
                    formatChild(node, obj, new FormatPipe.FormatCallback() {
                        @Override
                        public void onResponse(Object response) {
                            parent.put(response);
                        }
                    });

                }
            }
        }
        node.onResponse(parent);
    }

    private void formatChild(FormatBundle node,
                             Object obj,
                             FormatPipe.FormatCallback callback) {

        if(isJSONObject(obj)) {
            callback.onResponse(obj);
        } else  {
            node.onFork(obj, callback);
        }
    }
}
