package com.bevis.logger.formatter;

import org.json.JSONArray;

import java.lang.reflect.Array;

class ArrayFormatter extends Formatter<Object> {
    @Override
    boolean accept(Object obj) {
        return obj.getClass().isArray();
    }

    @Override
    void onFormat(FormatBundle<Object> node) {
        final JSONArray array = new JSONArray();
        final int length = Array.getLength(node.obj);
        for(int i=0;i<length;i++) {
            Object element = Array.get(node.obj, i);
            if(isJSONObject(element)) {
                array.put(element);
            } else {
                node.onFork(element, new FormatPipe.FormatCallback() {
                    @Override
                    public void onResponse(Object response) {
                        array.put(response);
                    }
                });
            }
        }

        node.onResponse(array);
    }
}
