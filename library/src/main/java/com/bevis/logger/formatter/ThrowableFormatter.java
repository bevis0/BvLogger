package com.bevis.logger.formatter;

class ThrowableFormatter extends Formatter<Throwable> {
    public static final String lineSeparator = System.getProperty("line.separator");

    @Override
    boolean accept(Object obj) {
        return obj instanceof Throwable;
    }

    @Override
    public void onFormat(
            FormatBundle<Throwable> node) {
        StringBuilder taskName = new StringBuilder();
        StackTraceElement[] traceElements = node.obj.getStackTrace();
        if (traceElements != null) {
            for(StackTraceElement element:traceElements){
                taskName.append(element.toString()).append(lineSeparator);
            }
        }
        node.onResponse(taskName.toString());
    }
}
