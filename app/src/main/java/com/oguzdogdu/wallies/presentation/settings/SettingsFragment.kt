package com.oguzdogdu.wallies.presentation.settings

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.oguzdogdu.wallies.BuildConfig
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSettingsBinding
import com.oguzdogdu.wallies.presentation.authenticateduser.ProfileMenu
import com.oguzdogdu.wallies.util.OptionLists
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel: SettingsViewModel by activityViewModels()

    private val appOptionsAdapter by lazy { SettingsAdapter() }

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

    override fun initViews() {
        super.initViews()
        binding.rvAppOptions.setupRecyclerView(
            layout = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            ),
            adapter = appOptionsAdapter,
            hasFixedSize = true,
            addDivider = true,
            onScroll = {}
        )
        setDataIntoRV()
        binding.toolbarSettings.setTitle(
            title = getString(R.string.settings),
            titleStyleRes = R.style.ToolbarTitleText
        )
        binding.textViewBuildVersion.text = "${resources.getString(R.string.version)} ${BuildConfig.VERSION_NAME}"
    }

    private fun setDataIntoRV() {
        val optionList = OptionLists.appOptionsList
        appOptionsAdapter.submitList(optionList)
        appOptionsAdapter.onBindToDivider = { binding, position ->
            setItemBackground(
                items = optionList,
                itemSize = optionList.size,
                adapter = appOptionsAdapter,
                position = position,
                binding = binding
            )
        }
    }

    override fun initListeners() {
        super.initListeners()
        appOptionsAdapter.setOnItemClickListener { option ->
            when (option?.titleRes) {
                R.string.theme_text -> showRadioConfirmationDialog()

                R.string.language_title_text -> showLanguageConfirmationDialog()

                R.string.clear_cache_title -> {
                    requireContext().filesDir.deleteRecursively()
                    requireView().showToast(requireContext(), R.string.cache_state_string)
                }
            }
        }
    }

    private fun showRadioConfirmationDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setSingleChoiceItems(themes, themes.indexOf(selectedTheme)) { dialog, which ->
                selectedThemeIndex = which
                selectedTheme = themes[which]
                viewModel.handleUIEvent(SettingsEvent.SetNewTheme(value = selectedTheme))
            }
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                viewModel.handleUIEvent(SettingsEvent.ThemeChanged)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
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
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                viewModel.handleUIEvent(SettingsEvent.LanguageChanged)
                requireActivity().recreate()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
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
            viewModel.themeState.observeInLifecycle(viewLifecycleOwner, observer = { theme ->
                selectedTheme = theme?.value?.ifBlank { themes[selectedThemeIndex] }.toString()
            })
        }
    }

    private fun observeLanguageState() {
        lifecycleScope.launch {
            viewModel.languageState.observeInLifecycle(viewLifecycleOwner, observer = { lang ->
                selectedLanguages = lang?.value?.ifBlank { language[selectedLanguageIndex] }.toString()
            })
        }
    }

    private fun setItemBackground(
        itemSize: Int,
        position: Int,
        items: List<ProfileMenu>,
        adapter: SettingsAdapter,
        binding: View
    ) {
        adapter.currentList.indexOf(items[position])

        when (itemSize) {
            1 -> binding.background = ContextCompat.getDrawable(
                binding.context,
                R.drawable.bg_clickable_all_radius_10
            )
            else -> {
                when (position) {
                    0 -> {
                        binding.background = ContextCompat.getDrawable(
                            binding.context,
                            R.drawable.bg_clickable_top_radius_10
                        )
                    }

                    itemSize - 1 -> binding.background = ContextCompat.getDrawable(
                        binding.context,
                        R.drawable.bg_clickable_bottom_radius_10
                    )

                    else -> {
                        binding.background = ContextCompat.getDrawable(
                            binding.context,
                            R.drawable.bg_clickable_no_radius
                        )
                    }
                }
            }
        }
    }
}
