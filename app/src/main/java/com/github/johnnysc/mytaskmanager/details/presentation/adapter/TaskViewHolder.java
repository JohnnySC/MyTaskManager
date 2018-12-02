package com.github.johnnysc.mytaskmanager.details.presentation.adapter;

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

    private final LinearLayout mDataLayout;
    private final TextView mTitle;
    private final TextView mBody;
    private final CheckBox mCheckBox;

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
        mTitle.setPaintFlags(mTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        mBody.setPaintFlags(mBody.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
    }
}