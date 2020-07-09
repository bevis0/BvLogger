package com.bevis.logger.formatter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

class StackTraceElementsFormatter extends Formatter<Collection<StackTraceElement>> {
    public static final String lineSeparator = System.getProperty("line.separator");

    @Override
    boolean accept(Object obj) {
        if(obj instanceof Collection) {
            ParameterizedType type = (ParameterizedType) obj.getClass().getGenericSuperclass();
            Type[] arguments = type.getActualTypeArguments();
            if(arguments != null && arguments.length > 0 && arguments[0] == obj.getClass()) {
                return true;
            }
        }
        return false;
    }

    @Override
    void onFormat(FormatBundle<Collection<StackTraceElement>> node) {
        Object result = null;
        Collection<StackTraceElement> linkStackTraces = node.obj;
        if(linkStackTraces == null || linkStackTraces.isEmpty()) {
            result = "";
        }

        if(result == null) {
            StringBuilder stackTranceLog = new StringBuilder();
            for (StackTraceElement traceElement : linkStackTraces) {
                stackTranceLog.append(traceElement.toString());
                stackTranceLog.append(lineSeparator);
            }

            if (stackTranceLog.length() > 0) {
                stackTranceLog.delete(stackTranceLog.length() - lineSeparator.length(), stackTranceLog.length());
            }
            result = stackTranceLog.toString();
        }

        node.onResponse(result == null?"":result);
    }
}
