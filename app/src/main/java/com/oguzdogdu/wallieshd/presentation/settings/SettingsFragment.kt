package com.oguzdogdu.wallieshd.presentation.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.oguzdogdu.wallieshd.BuildConfig
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.BaseFragment
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.databinding.FragmentSettingsBinding
import com.oguzdogdu.wallieshd.presentation.authenticateduser.ProfileMenu
import com.oguzdogdu.wallieshd.util.ChoiseDialogBuilder
import com.oguzdogdu.wallieshd.util.OptionLists
import com.oguzdogdu.wallieshd.util.observeInLifecycle
import com.oguzdogdu.wallieshd.util.setupRecyclerView
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

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
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
                    clearAppCache(requireContext())
                    showMessage(
                        message = resources.getString(R.string.cache_state_string),
                        MessageType.SUCCESS
                    )
                }
            }
        }
    }

    private fun clearAppCache(context: Context) {
        val cacheDir = context.cacheDir
        when {
            cacheDir.exists() -> {
                try {
                    cacheDir.deleteRecursively()
                    Log.d("AppCache", "Cache directory deleted successfully")
                } catch (e: Exception) {
                    Log.e("AppCache", "Error deleting cache directory: $e")
                }
            }
            else -> Log.d("AppCache", "Cache directory does not exist")
        }
    }

    private fun showRadioConfirmationDialog() {
        ChoiseDialogBuilder.choiseAnyValueIntoDialogList(
            context = requireContext(),
            title = getString(R.string.choise_theme),
            selectedValue = selectedTheme,
            list = themes,
            handlerList = { dialog, which ->
                selectedThemeIndex = which
                selectedTheme = themes[which]
                viewModel.handleUIEvent(SettingsEvent.SetNewTheme(value = selectedTheme))
            },
            positive = { dialog, which ->
                viewModel.handleUIEvent(SettingsEvent.ThemeChanged)
            }

        )
    }

    private fun showLanguageConfirmationDialog() {
        ChoiseDialogBuilder.choiseAnyValueIntoDialogList(
            context = requireContext(),
            title = getString(R.string.choise_language),
            selectedValue = selectedLanguages,
            list = language,
            handlerList = { dialog, which ->
                selectedLanguageIndex = which
                selectedLanguages = language[which]
                viewModel.handleUIEvent(SettingsEvent.SetNewLanguage(value = selectedLanguages))
            },
            positive = { dialog, which ->
                viewModel.handleUIEvent(SettingsEvent.LanguageChanged)
                requireActivity().recreate()
            }
        )
    }

    override fun observeData() {
        super.observeData()
        viewModel.handleUIEvent(SettingsEvent.LanguageChanged)
        viewModel.handleUIEvent(SettingsEvent.ThemeChanged)
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
