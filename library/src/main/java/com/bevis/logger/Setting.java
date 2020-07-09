package com.bevis.logger;

public class Setting {
    private boolean asyncOutput = true;
    private Object gson;
    private boolean enableGson = true;

    public boolean isAsyncOutput() {
        return asyncOutput;
    }

    public Setting setAsyncOutput(boolean asyncOutput) {
        this.asyncOutput = asyncOutput;
        return this;
    }

    public Object getGson() {
        return gson;
    }

    public Setting setGson(Object gson) {
        this.gson = gson;
        return this;
    }

    public boolean enableGson() {
        return enableGson;
    }

    public Setting setEnableGson(boolean enableGson) {
        this.enableGson = enableGson;
        return this;
    }
}
