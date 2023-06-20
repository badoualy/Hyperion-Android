package com.willowtreeapps.hyperion.timber.list;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.timber.R;
import com.willowtreeapps.hyperion.timber.TimberPlugin;
import com.willowtreeapps.hyperion.timber.model.CircularBuffer;
import com.willowtreeapps.hyperion.timber.model.LogItem;

@HyperionIgnore
public class TimberLogListActivity extends AppCompatActivity {
    Toolbar mToolbar;
    Toolbar mFilterToolbar;
    EditText mFilterEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmb_activity_timber_list);
        mToolbar = findViewById(R.id.tmb_toolbar);
        mToolbar.setNavigationIcon(R.drawable.tmb_ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mToolbar.inflateMenu(R.menu.tmb_filter);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_item_filter) {
                    fadeIn(mFilterToolbar);
                    return true;
                }
                if (item.getItemId() == R.id.menu_item_share) {
                    collectLogs();
                    return true;
                }
                return false;
            }
        });
        mFilterToolbar = findViewById(R.id.tmb_filter_toolbar);
        mFilterEditText = findViewById(R.id.tmb_filter);

        CircularBuffer<LogItem> logItemQueue = getLogItemQueue();
        final TimberLogListAdapter adapter = new TimberLogListAdapter(logItemQueue);

        int numberOfLogs = logItemQueue.size();
        mToolbar.setSubtitle(getResources().getQuantityString(R.plurals.tmb_last_logs, numberOfLogs, numberOfLogs));
        mFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.applyFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mFilterToolbar.setNavigationIcon(R.drawable.tmb_ic_close);
        mFilterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeOut(mFilterToolbar);
                mFilterEditText.getText().clear();
                adapter.applyFilter(null);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.tmb_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(adapter);
    }

    private CircularBuffer<LogItem> getLogItemQueue() {
        return TimberPlugin.logItemBuffer;
    }

    private void collectLogs() {
        CircularBuffer<LogItem> logItemQueue = getLogItemQueue();
        int size = logItemQueue.size();
        StringBuilder logs = new StringBuilder();
        for (int i = 0; i < size; i++) {
            LogItem logItem = logItemQueue.getItem(i);
            String messageWithTag = logItem.tag != null
                    ? getString(R.string.tmb_message_with_tag_format, logItem.tag, logItem.message)
                    : logItem.message;

            logs.append(logItem.level)
                    .append(" : [")
                    .append(logItem.date)
                    .append("] --> ")
                    .append(messageWithTag)
                    .append("\n");
        }
        ShareCompat.IntentBuilder
                .from(this)
                .setText(logs.toString())
                // most general text sharing MIME type
                .setType("text/plain")
                .startChooser();
    }

    private void fadeIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.animate()
                .alpha(1.0f);
    }

    private void fadeOut(final View view) {
        long duration = 250L;
        view.animate()
                .setDuration(duration)
                .alpha(0.0f);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, duration);
    }
}
