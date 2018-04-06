package com.github.johnnysc.mytaskmanager;

import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.uiautomator.UiDevice;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;

/**
 * @author Asatryan on 03.04.18.
 */

abstract class BaseTest {
    static final String ADD = "Добавить";
    static final String CREATE = "Создать задачу";
    static final String READ = "Просмотр задачи";
    static final String TASK_CATEGORY = "Категория задачи";
    static final String DONE = "Готово";
    static final String NOTIFY = "Оповестить";
    static final String CREATED_ON = "Создано ";
    static final String NOTIFY_DATE_AND_TIME = "Дата и время оповещеня";
    static final String HINT_TITLE = "Заголовок";
    static final String HINT_DESCRIPTION = "Описание";
    static final String TEXT_TO_BE_TYPED = "some text here";
    static final String NEW_TEXT_TO_BE_TYPED = "NEW text here";

    List<String> mCategories;
    List<Integer> mCategoriesLayout;
    UiDevice mDevice;
    
    void setUp(){
        mCategories = Arrays.asList(
                "Несрочно и неважно",
                "Несрочно, но важно",
                "Срочно, но неважно",
                "Срочно и важно"
        );
        mCategoriesLayout = Arrays.asList(
                R.id.first_category_layout,
                R.id.second_category_layout,
                R.id.third_category_layout,
                R.id.forth_category_layout
        );
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    void checkMainText(int position, int count) {
        onView(allOf(withId(R.id.info_text_view), withParent(withId(mCategoriesLayout.get(position))))).check(matches(withText(mCategories.get(position) + "\n" + count)));
    }

    void setDateAndCheck(int year, int month, int day, int hours, int minutes) {
        setDate(R.id.time_text_view, year, month, day, hours, minutes);
        checkDate(year, month, day, hours, minutes);
    }

    void checkDate(int year, int month, int day, int hours, int minutes) {
        String date = String.format("%d/%d/%d %d:%d", day, month, year, hours, minutes);
        onView(withId(R.id.time_text_view)).check(matches(withText(date)));
    }

    void checkInputLayoutHint(int id, String text) {
        onView(withId(id)).perform(scrollTo()).check(matches(hasTextInputLayoutHintText(text)));
    }

    void checkCheckBoxState(int id, boolean checked, String text) {
        onView(withId(id)).perform(scrollTo()).check(matches(checked ? isChecked() : isNotChecked()));
        onView(withId(id)).perform(click()).check(matches(withText(text)));
    }

    void checkToolbarText(String text) {
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar)))).check(matches(withText(text)));
    }

    void checkFabImage(int expectedId) {
        onView(withId(R.id.action_fab)).check(matches(new DrawableMatcher(expectedId)));
    }

    void checkSpinnerText(String text) {
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(text))));
    }

    void pressBack(boolean hard) {
        if (hard) {
            mDevice.pressBack();
        } else {
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        }
    }

    void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth, int hours, int minutes) {
        onView(withId(datePickerLaunchViewId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click()); // CAUTION! WORKING FOR NOT ALL DEVICES (4.4.4 FAILED)
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hours, minutes));
        onView(withId(android.R.id.button1)).perform(click());
    }

    Matcher<View> hasTextInputLayoutHintText(final String hintText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence layoutHint = ((TextInputLayout) view).getHint();

                if (layoutHint == null) {
                    return false;
                }

                String hint = layoutHint.toString();

                return hintText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}

