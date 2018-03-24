package com.github.johnnysc.mytaskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.johnnysc.mytaskmanager.model.Category;
import com.github.johnnysc.mytaskmanager.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

import static com.github.johnnysc.mytaskmanager.model.CategoryType.CATEGORY_TYPES;
import static com.github.johnnysc.mytaskmanager.model.CategoryType.TaskType;

/**
 * Contains 4 categories equal parts on screen and buttons on them,
 * when return need to update data about tasks count that not done yet
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    private static final Map<Integer, Integer> MAIN_MAP = new HashMap<>();
    private static final List<MainActivity.MainItem> MAIN_ITEMS = new ArrayList<>();
    private static final List<Integer> LAYOUT_ID_LIST = Arrays.asList(
            R.id.first_category_layout,
            R.id.second_category_layout,
            R.id.third_category_layout,
            R.id.forth_category_layout
    );

    static {
        for (int i = 0; i < LAYOUT_ID_LIST.size(); i++) {
            MAIN_MAP.put(LAYOUT_ID_LIST.get(i), CATEGORY_TYPES.get(i));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRealm();
        initMainItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            updateMainItemsTexts();
        }
    }

    @Override
    public void onClick(View view) {
        for (Map.Entry<Integer, Integer> entry : MAIN_MAP.entrySet()) {
            if (view.getId() == entry.getKey()) {
                startActivityForResult(DetailActivity.newIntent(this, entry.getValue()), REQUEST_CODE);
                break;
            }
        }
    }

    //region private methods

    private void initMainItems() {
        for (int i = 0; i < LAYOUT_ID_LIST.size(); i++) {
            MAIN_ITEMS.add(new MainItem(LAYOUT_ID_LIST.get(i)));
            MAIN_ITEMS.get(MAIN_ITEMS.size() - 1).setRootClickListener(this);
            final int taskType = CATEGORY_TYPES.get(i);
            MAIN_ITEMS.get(MAIN_ITEMS.size() - 1).setButtonClickListener(view -> handleButtonClick(taskType));
        }
        updateMainItemsTexts();
    }

    private void updateMainItemsTexts() {
        for (int i = 0; i < MAIN_ITEMS.size(); i++) {
            Category category = getCategoryByPrimaryKey(CATEGORY_TYPES.get(i));
            String text = getString(CATEGORY_STRINGS.get(i)) + "\n" + getNotDoneTasksCount(category.getTasks());
            MAIN_ITEMS.get(i).setText(text);
        }
    }

    private void handleButtonClick(@TaskType int taskType) {
        startActivityForResult(CRUDTaskActivity.newIntent(this, taskType, CRUDTaskActivity.CREATE), REQUEST_CODE);
    }

    private int getNotDoneTasksCount(RealmList<Task> tasks) {
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (!tasks.get(i).isDone()) {
                count++;
            }
        }
        return count;
    }

    private void initRealm() {
        if (mRealm.allObjects(Category.class).size() > 3) {
            Log.wtf(TAG, "initRealm: something went wrong!");
            return;
        }
        for (int i = 0; i < CATEGORY_STRINGS.size(); i++) {
            mRealm.beginTransaction();
            Category category = new Category();
            category.setName(getString(CATEGORY_STRINGS.get(i)));
            category.setId(CATEGORY_TYPES.get(i));
            mRealm.copyToRealmOrUpdate(category);
            mRealm.commitTransaction();
        }
    }

    //endregion

    class MainItem {
        private RelativeLayout mRootLayout;
        private TextView mInfoTextView;
        private Button mAddButton;

        MainItem(int id) {
            mRootLayout = findViewById(id);
            mInfoTextView = mRootLayout.findViewById(R.id.info_text_view);
            mAddButton = mRootLayout.findViewById(R.id.add_button);
        }

        void setText(String text) {
            mInfoTextView.setText(text);
        }

        void setButtonClickListener(View.OnClickListener listener) {
            mAddButton.setOnClickListener(listener);
        }

        void setRootClickListener(View.OnClickListener listener) {
            mRootLayout.setOnClickListener(listener);
        }
    }

}