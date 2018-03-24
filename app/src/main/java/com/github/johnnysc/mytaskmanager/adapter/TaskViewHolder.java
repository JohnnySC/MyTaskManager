package com.github.johnnysc.mytaskmanager.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnnysc.mytaskmanager.R;

/**
 * @author JohnnySC on 24.03.18.
 */

final class TaskViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout mDataLayout;
    private TextView mTitle;
    private TextView mBody;
    private CheckBox mCheckBox;

    TaskViewHolder(View itemView) {
        super(itemView);
        mDataLayout = itemView.findViewById(R.id.data_layout);
        mTitle = itemView.findViewById(R.id.title_text_view);
        mBody = itemView.findViewById(R.id.body_text_view);
        mCheckBox = itemView.findViewById(R.id.check_box);
    }

    void setLayoutClickListener(View.OnClickListener listener) {
        mDataLayout.setOnClickListener(listener);
    }

    void setTitle(String title) {
        mTitle.setText(title);
    }

    void setBody(String body) {
        mBody.setText(body);
    }

    void setCheckBoxChecked(boolean checked) {
        mCheckBox.setChecked(checked);
    }

    void setCheckBoxChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mCheckBox.setOnCheckedChangeListener(listener);
    }

    void setDataStrikeThrough() {
        mTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mBody.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    void setDataCleared() {
        mTitle.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        mBody.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
    }
}