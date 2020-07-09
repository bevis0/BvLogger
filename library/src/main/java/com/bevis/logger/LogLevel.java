package com.bevis.logger;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({LogLevel.VERBOSE, LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARN,
LogLevel.ERROR, LogLevel.ALL, LogLevel.NONE})
public @interface LogLevel {
    int VERBOSE = 5;
    int DEBUG = 4;
    int INFO = 3;
    int WARN = 2;
    int ERROR = 1;

    int ALL = 6;
    int NONE = 0;
}
