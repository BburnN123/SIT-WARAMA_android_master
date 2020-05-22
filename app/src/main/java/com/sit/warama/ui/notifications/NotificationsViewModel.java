package com.sit.warama.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Information: \n" +
                "This app works with your phone's bluetooth and location features in order to perform beacon scanning."
                + "\n\n Background will flash red or yellow depending on how close you are to a known accident hotspot.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}