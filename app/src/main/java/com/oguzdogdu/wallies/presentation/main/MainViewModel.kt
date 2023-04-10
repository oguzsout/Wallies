package com.oguzdogdu.wallies.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(){
    val showBottomNavigation = MutableLiveData(true)
}