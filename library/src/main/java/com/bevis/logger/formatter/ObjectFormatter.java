package com.bevis.logger.formatter;

class ObjectFormatter extends Formatter<Object> {
    @Override
    public boolean accept(Object obj) {
        return true;
    }

    @Override
    public void onFormat(FormatBundle<Object> node) {
        if(isOriginalFormativeObject(node.obj)){
            node.onResponse(node.obj);
        } else {
            node.onResponse(node.obj.toString());
        }

    }
}
