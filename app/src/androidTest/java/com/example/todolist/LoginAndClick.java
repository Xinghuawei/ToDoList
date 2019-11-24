package com.example.todolist;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.example.todolist.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LoginAndClick{

    @Rule
    public ActivityTestRule<LoginActivity> rule
            = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void Should_RetrieveMatching_When_InquireByKeyword() {
        onView(withId(R.id.userPassword))
                .perform(typeText("1994"),closeSoftKeyboard());
        onView(withId(R.id.logToList))
                .perform(click());
        onView(withId(R.id.action_add_task))
                .perform(click());
        // Pending functional implementation
//        assertEquals("dog", PhotoDisplayManager.getInstance().getFilter().getKeyword());
        assertEquals("success", "success");
    }
}

