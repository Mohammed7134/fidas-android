package com.example.fidas.ui.locationFinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationFinderViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public LocationFinderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is finder fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
