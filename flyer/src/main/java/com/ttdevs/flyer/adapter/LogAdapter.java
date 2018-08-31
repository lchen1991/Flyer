package com.ttdevs.flyer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ttdevs.flyer.R;
import com.ttdevs.flyer.utils.Constant;

import java.util.List;

import static com.ttdevs.flyer.utils.Constant.INDEX_LOG_CONTENT;
import static com.ttdevs.flyer.utils.Constant.INDEX_LOG_LEVEL;

/**
 * @author ttdevs
 */
public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private Context mContext;
    private List<String> mData;
    private LayoutInflater mInflater;

    public LogAdapter(Context context, List<String> data) {
        mInflater = LayoutInflater.from(context);

        mData = data;

        mContext = context;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_logcat_content, parent, false);
        return new LogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        String logString = mData.get(position);

        String[] items = logString.split("\\s+");

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.log_level_verbose));
        if (items.length >= INDEX_LOG_LEVEL) {
            if (items[INDEX_LOG_CONTENT].length() > 0) {
                char flag = items[INDEX_LOG_CONTENT].toCharArray()[0];
                colorSpan = Constant.LOG_COLOR_SPAN.get(flag);
            }
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(logString);
        builder.setSpan(colorSpan, 0, logString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        holder.tvLog.setText("");
        holder.tvLog.setText(builder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class LogViewHolder extends RecyclerView.ViewHolder {
        public TextView tvLog;

        public LogViewHolder(View view) {
            super(view);

            tvLog = view.findViewById(R.id.tv_log);
        }
    }
}
