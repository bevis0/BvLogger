package com.bevis.logger.formatter;

class StackTraceElementFormatter extends Formatter<StackTraceElement> {
    @Override
    boolean accept(Object obj) {
        return obj instanceof StackTraceElement;
    }

    @Override
    void onFormat(FormatBundle<StackTraceElement> node) {
        node.onResponse(node.obj.toString());
    }
}
