package com.github.johnnysc.mytaskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnnysc.mytaskmanager.model.Category;
import com.github.johnnysc.mytaskmanager.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;

import static com.github.johnnysc.mytaskmanager.model.CategoryType.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Realm mRealm;
    private static final List<Integer> TYPES = Arrays.asList(
            NOT_IMPORTANT_NOT_URGENT,
            IMPORTANT_NOT_URGENT,
            URGENT_NOT_IMPORTANT,
            URGENT_IMPORTANT
    );
    private static final List<Integer> CATEGORIES = Arrays.asList(
            R.string.first,
            R.string.second,
            R.string.third,
            R.string.forth
    );
    private static final Map<Integer, Integer> MAIN_MAP = new HashMap<>();

    static {
        MAIN_MAP.put(R.id.first, TYPES.get(0));
        MAIN_MAP.put(R.id.second, TYPES.get(1));
        MAIN_MAP.put(R.id.third, TYPES.get(2));
        MAIN_MAP.put(R.id.forth, TYPES.get(3));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRealm();
        final List<MainItem> mainItems = new ArrayList<>();
        mainItems.add(new MainItem(R.id.first));
        mainItems.add(new MainItem(R.id.second));
        mainItems.add(new MainItem(R.id.third));
        mainItems.add(new MainItem(R.id.forth));

        for (int i = 0; i < mainItems.size(); i++) {
            mainItems.get(i).setRootClickListener(this);
            final int taskType = TYPES.get(i);
            mainItems.get(i).setButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleButtonClick(taskType);
                }
            });
            Category category = getByPrimaryKey(taskType);
            String text = getString(CATEGORIES.get(i)) + "\n" + getNotDoneTasksCount(category.getTasks());
            mainItems.get(i).setText(text);
        }
    }

    private void handleButtonClick(@TaskType int taskType) {
        Toast.makeText(this, "button tapped " + taskType, Toast.LENGTH_SHORT).show();
//        startActivity(AddTaskActivity.newIntent(this, taskType));
    }

    @Override
    public void onClick(View view) {
        for (Map.Entry<Integer, Integer> entry : MAIN_MAP.entrySet()) {
            if (view.getId() == entry.getKey()) {
                Toast.makeText(this, "layout tapped " + entry.getValue(), Toast.LENGTH_SHORT).show();
//                startActivity(DetailActivity.getIntent(this, entry.getValue()));
                break;
            }
        }
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

    private Category getByPrimaryKey(int id) {
        return mRealm.where(Category.class).equalTo("id", id).findFirst();
    }

    private void initRealm() {
        mRealm = Realm.getDefaultInstance();
        if (mRealm.allObjects(Category.class).size() > 3) {
            return;
        }
        for (int i = 0; i < CATEGORIES.size(); i++) {
            mRealm.beginTransaction();
            Category category = new Category();
            category.setName(getString(CATEGORIES.get(i)));
            category.setId(TYPES.get(i));
            mRealm.copyToRealmOrUpdate(category);
            mRealm.commitTransaction();
        }
    }

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