package com.example.recyclerview;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    //INITIALIZE VARIABLES
    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    //CREATE SET TEXT METHOD
    public void setText(String s) {
        //SET VALUE
        mutableLiveData.setValue(s);
    }

    //CREATE GET TEXT METHOD
    public MutableLiveData<String> getText() {
        return mutableLiveData;
    }
}
