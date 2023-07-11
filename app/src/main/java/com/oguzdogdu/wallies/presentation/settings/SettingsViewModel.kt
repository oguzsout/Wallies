package com.oguzdogdu.wallies.presentation.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzdogdu.domain.repository.DataStore
import com.oguzdogdu.domain.usecase.auth.SignOutUseCase
import com.oguzdogdu.domain.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val showBottomNavigation = MutableLiveData(true)

    private val _eventChannel = Channel<SettingsEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    private val _themeState = MutableStateFlow<SettingsState.ThemeValue?>(null)
    val themeState = _themeState.filterNotNull()

    private val _languageState = MutableStateFlow<SettingsState.LanguageValue?>(null)
    val languageState = _languageState.filterNotNull()

    // Oturum sonlandırma işlemini başlatmak için gerekli zaman (30 dakika)
    private val LOGOUT_DELAY = TimeUnit.MINUTES.toMillis(1)

    // Oturum sonlandırma işlemi için zamanlayıcı
    private var logoutTimer: Timer? = null

    // Kullanıcının oturum açtığı zamanı saklamak için kullanılacak değişken
    private var lastSignInTime: Long? = null

    init {
        getThemeValue()
        getLanguageValue()
    }

    // Kullanıcı oturum açtığında bu fonksiyonu çağırın ve lastSignInTime değerini güncelleyin
    private fun setLastSignInTime() {
        lastSignInTime = System.currentTimeMillis()
        startLogoutTimer()
    }

    // Oturumu sonlandırmak için zamanlayıcıyı başlatır
    private fun startLogoutTimer() {
        // Eğer zamanlayıcı zaten çalışıyorsa, durdur ve yeniden başlat
        stopLogoutTimer()

        logoutTimer = Timer()
        logoutTimer?.schedule(
            object : TimerTask() {
                override fun run() {
                    logoutUser()
                }
            },
            LOGOUT_DELAY
        )
    }

    // Oturumu sonlandırmak için çağırılacak fonksiyon
    private fun logoutUser() {
        viewModelScope.launch {
            signOutUseCase.invoke()
        } // Diğer temizleme işlemlerini gerçekleştirin (ör. verileri sıfırlama)
    }

    // Zamanlayıcıyı durdurur
    private fun stopLogoutTimer() {
        logoutTimer?.cancel()
        logoutTimer = null
    }

    // Uygulama başlatıldığında veya kullanıcı oturum açtığında bu fonksiyonu çağırın
    private fun checkLogoutStatus() {
        val currentTime = System.currentTimeMillis()
        if (lastSignInTime != null && currentTime - lastSignInTime!! >= LOGOUT_DELAY) {
            logoutUser()
        } else {
            startLogoutTimer()
        }
    }
    fun handleUIEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetNewTheme -> {
                setThemeValue(event.value)
            }

            is SettingsEvent.ThemeChanged -> {
                getThemeValue()
            }

            is SettingsEvent.LanguageChanged -> {
                getLanguageValue()
            }

            is SettingsEvent.SetNewLanguage -> {
                setLanguageValue(event.value)
            }
        }
    }

    private fun setThemeValue(value: String) = runBlocking {
        dataStore.putThemeStrings(key = "theme", value = value)
    }

    private fun getThemeValue() {
        viewModelScope.launch {
            dataStore.getThemeStrings(key = "theme").collect { value ->
                when (value) {
                    is Resource.Success -> {
                        _themeState.update {
                            SettingsState.ThemeValue(value.data.orEmpty())
                        }
                    }

                    is Resource.Error -> {
                    }

                    else -> {
                    }
                }
            }
        }
    }

    private fun setLanguageValue(value: String) = runBlocking {
        dataStore.putLanguageStrings(key = "language", value = value)
    }

    private fun getLanguageValue() {
        viewModelScope.launch {
            dataStore.getLanguageStrings(key = "language").collect { value ->
                when (value) {
                    is Resource.Success -> {
                        _languageState.update {
                            SettingsState.LanguageValue(value.data.orEmpty())
                        }
                    }

                    is Resource.Error -> {
                    }

                    else -> {
                    }
                }
            }
        }
    }
}
