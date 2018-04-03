package com.github.johnnysc.mytaskmanager;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.allOf;

/**
 * @author Asatryan on 03.04.18.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest extends BaseTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        super.setUp();
    }

    /**
     * Check all the main data supposed to be shown, also try to create task and check all the fields but cancel at the end
     */
    @Test
    public void testMainAndCreate() {
        for (int i = 0; i < mCategories.size(); i++) {
            Integer layoutId = mCategoriesLayout.get(i);
            onView(withId(layoutId)).check(matches(isDisplayed()));
            checkMainText(i, 0);
            onView(allOf(withId(R.id.add_button), withParent(withId(layoutId)))).check(matches(withText(ADD)));
            onView(allOf(withId(R.id.add_button), withParent(withId(layoutId)))).perform(click());
            checkAddNewItemLayout(i);
            onView(withId(layoutId)).perform(click());
            checkToolbarText(mCategories.get(i));
            checkFabImage(android.R.drawable.ic_input_add);
            onView(withId(R.id.action_fab)).perform(click());
            checkAddNewItemLayout(i);
            pressBack(i < mCategories.size() / 2);
            checkMainText(i, 0);
        }
    }

    /**
     * Tests creating, saving, editing and deleting the task
     */
    @Test
    public void testSaveEditAndDelete() {
        for (int i = 0; i < mCategoriesLayout.size(); i++) {
            createAndEditTask(i);
            // 3. delete task
            onView(withId(R.id.data_layout)).perform(swipeLeft());
            // check disappeared
            onView(withId(R.id.data_layout)).check(doesNotExist());
            pressBack(false);
            checkMainText(i, 0);

            // 4. repeat all and delete by icon
            createAndEditTask(i);
            onView(withId(R.id.data_layout)).perform(click());
            onView(withId(R.id.action_fab)).perform(click());
            onView(withId(R.id.delete_task)).perform(click());
            // check disappeared
            onView(withId(R.id.data_layout)).check(doesNotExist());
            pressBack(false);
            checkMainText(i, 0);
        }
    }

    private void createAndEditTask(int layoutIndex) {
        final Integer firstLayoutId = mCategoriesLayout.get(layoutIndex);
        // 1. create new task
        onView(allOf(withId(R.id.add_button), withParent(withId(firstLayoutId)))).perform(click());
        checkToolbarText(CREATE);

        checkCheckBoxState(R.id.alarm_check_box, false, NOTIFY);
        setDateAndCheck(2018, 10, 15, 11, 30);
        onView(withId(R.id.title_edit_text)).perform(scrollTo(), typeText(TEXT_TO_BE_TYPED));
        onView(withId(R.id.body_edit_text)).perform(scrollTo(), typeText(TEXT_TO_BE_TYPED));
        onView(withId(R.id.action_fab)).perform(click());
        // check it was created
        checkMainText(layoutIndex, 1);

        onView(withId(firstLayoutId)).perform(click());

        onView(withId(R.id.title_text_view)).check(matches(withText(TEXT_TO_BE_TYPED)));
        onView(withId(R.id.body_text_view)).check(matches(withText(TEXT_TO_BE_TYPED)));
        // 2. edit the task
        onView(withId(R.id.title_text_view)).perform(click());
        checkToolbarText(READ);
        checkFabImage(android.R.drawable.ic_menu_edit);
        onView(withId(R.id.action_fab)).perform(click());
        checkFabImage(android.R.drawable.ic_menu_save);
        checkCheckBoxState(R.id.alarm_check_box, true, NOTIFY);
        setDateAndCheck(2019, 11, 20, 12, 15);
        onView(withId(R.id.done_check_box)).perform(click());
        onView(withId(R.id.title_edit_text)).perform(scrollTo(), clearText(), typeText(NEW_TEXT_TO_BE_TYPED));
        onView(withId(R.id.body_edit_text)).perform(scrollTo(), clearText(), typeText(NEW_TEXT_TO_BE_TYPED));
        onView(withId(R.id.action_fab)).perform(click());
        // check new data
        onView(withId(R.id.title_text_view)).check(matches(withText(NEW_TEXT_TO_BE_TYPED)));
        onView(withId(R.id.body_text_view)).check(matches(withText(NEW_TEXT_TO_BE_TYPED)));
        onView(withId(R.id.check_box)).check(matches(isChecked()));
    }

    private void checkAddNewItemLayout(int index) {
        checkToolbarText(CREATE);
        checkFabImage(android.R.drawable.ic_menu_save);
        onView(withText(TASK_CATEGORY)).check(matches(isDisplayed()));
        checkSpinnerText(mCategories.get(index));
        checkCheckBoxState(R.id.done_check_box, false, DONE);
        onView(withId(R.id.date_text_view)).check(matches(withText(startsWith(CREATED_ON))));
        checkCheckBoxState(R.id.alarm_check_box, false, NOTIFY);
        onView(withId(R.id.time_text_view)).check(matches(withText(NOTIFY_DATE_AND_TIME)));
        setDateAndCheck(2018, 10, 15, 11, 30);
        checkInputLayoutHint(R.id.title_text_input_layout, HINT_TITLE);
        checkInputLayoutHint(R.id.body_text_input_layout, HINT_DESCRIPTION);
        onView(withId(R.id.title_edit_text)).perform(typeText(TEXT_TO_BE_TYPED));
        onView(withId(R.id.body_edit_text)).perform(typeText(TEXT_TO_BE_TYPED));
        pressBack(false);
    }
}