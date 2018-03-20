package com.github.johnnysc.mytaskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_TYPE = "extra_type";

    public static Intent getIntent(Context context, int type){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        int type = getIntent().getIntExtra(EXTRA_TYPE, 0);
    }
}
