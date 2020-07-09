package com.bevis.logger.formatter;

class NullFormatter extends Formatter<Object> {
    @Override
    public boolean accept(Object obj) {
        return obj == null;
    }

    @Override
    public void onFormat(FormatBundle<Object> node) {
        node.onResponse("null");
    }
}
