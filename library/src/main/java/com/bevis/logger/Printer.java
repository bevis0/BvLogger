package com.bevis.logger;

import android.support.annotation.NonNull;

public interface Printer {
    void v(@NonNull LogMessage message);
    void d(@NonNull LogMessage message);
    void i(@NonNull LogMessage message);
    void w(@NonNull LogMessage message);
    void e(@NonNull LogMessage message);
}
