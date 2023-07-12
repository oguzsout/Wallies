package com.oguzdogdu.wallies.presentation.settings

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel: SettingsViewModel by activityViewModels()

    private lateinit var selectedTheme: String

    private lateinit var selectedLanguages: String

    private var selectedThemeIndex: Int = 0

    private var selectedLanguageIndex: Int = 0

    private val themes = arrayOf(
        ThemeValues.LIGHT_MODE.title,
        ThemeValues.DARK_MODE.title,
        ThemeValues.SYSTEM_DEFAULT.title
    )
    private val language = arrayOf(LanguageValues.English.title, LanguageValues.Turkish.title)

    override fun initListeners() {
        super.initListeners()
        binding.cardViewThemeContainer.setOnClickListener {
            showRadioConfirmationDialog()
        }
        binding.cardViewLanguageContainer.setOnClickListener {
            showLanguageConfirmationDialog()
        }
        binding.imageViewBackArrow.setOnClickListener {
            navigateBack()
        }
    }

    private fun showRadioConfirmationDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setSingleChoiceItems(themes, themes.indexOf(selectedTheme)) { dialog, which ->
                selectedThemeIndex = which
                selectedTheme = themes[which]
                viewModel.handleUIEvent(SettingsEvent.SetNewTheme(value = selectedTheme))
            }
            .setPositiveButton("Ok") { dialog, which ->
                viewModel.handleUIEvent(SettingsEvent.ThemeChanged)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun showLanguageConfirmationDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setSingleChoiceItems(language, language.indexOf(selectedLanguages)) { dialog, which ->
                selectedLanguageIndex = which
                selectedLanguages = language[which]
                viewModel.handleUIEvent(SettingsEvent.SetNewLanguage(value = selectedLanguages))
            }
            .setPositiveButton("Ok") { dialog, which ->
                viewModel.handleUIEvent(SettingsEvent.LanguageChanged)
                requireActivity().recreate()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    override fun observeData() {
        super.observeData()
        observeThemeState()
        observeLanguageState()
    }

    private fun observeThemeState() {
        lifecycleScope.launch {
            viewModel.themeState.collect { theme ->
                selectedTheme = theme.value.ifBlank { themes[selectedThemeIndex] }
            }
        }
    }

    private fun observeLanguageState() {
        lifecycleScope.launch {
            viewModel.languageState.collectLatest { lang ->
                selectedLanguages = lang.value.ifBlank { language[selectedLanguageIndex] }
            }
        }
    }
}
