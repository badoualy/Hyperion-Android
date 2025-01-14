package com.willowtreeapps.hyperion.timber.timber;

import androidx.annotation.NonNull;

import com.willowtreeapps.hyperion.timber.model.CircularBuffer;
import com.willowtreeapps.hyperion.timber.model.Level;
import com.willowtreeapps.hyperion.timber.model.LogItem;

import java.util.Date;

import timber.log.Timber;

public class TimberLogTree extends Timber.DebugTree {

    protected final CircularBuffer<LogItem> circularBuffer;

    public TimberLogTree(CircularBuffer<LogItem> circularBuffer) {
        this.circularBuffer = circularBuffer;
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        LogItem logItem = new LogItem(
                Level.forLogLevel(priority),
                new Date(),
                tag,
                message);

        circularBuffer.enqueue(logItem);
    }

}
