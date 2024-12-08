package com.example.p6firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> currentIntake = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> targetIntake = new MutableLiveData<>(2000);

    public LiveData<Integer> getCurrentIntake() {
        return currentIntake;
    }

    public void setCurrentIntake(int intake) {
        currentIntake.setValue(intake);
    }

    public LiveData<Integer> getTargetIntake() {
        return targetIntake;
    }

    public void setTargetIntake(int target) {
        targetIntake.setValue(target);
    }
}
