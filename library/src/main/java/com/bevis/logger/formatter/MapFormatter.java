package com.bevis.logger.formatter;

import java.util.Map;

class MapFormatter extends Formatter<Map> {
    @Override
    boolean accept(Object obj) {
        return obj instanceof Map;
    }

    @Override
    void onFormat(final FormatBundle<Map> node) {
        Map map = node.obj;
        Iterable<Map.Entry> iterable = map.entrySet();
        node.onFork(iterable, new FormatPipe.FormatCallback() {
            @Override
            public void onResponse(Object response) {
                node.onResponse(response);
                return;
            }
        });
    }
}
