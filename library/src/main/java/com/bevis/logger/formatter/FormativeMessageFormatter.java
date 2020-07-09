package com.bevis.logger.formatter;

import com.bevis.logger.LogMessage;

class FormativeMessageFormatter extends Formatter<LogMessage.FormativeMessage> {
    @Override
    boolean accept(Object obj) {
        return obj instanceof LogMessage.FormativeMessage;
    }

    @Override
    void onFormat(FormatBundle<LogMessage.FormativeMessage> node) {
        String string = node.obj.args == null?node.obj.message:String.format(node.obj.message, node.obj.args);
        node.onNext(string);
    }

}
