package com.oguzdogdu.wallies.presentation.settings

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.oguzdogdu.wallies.BuildConfig
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentSettingsBinding
import com.oguzdogdu.wallies.presentation.authenticateduser.ProfileMenu
import com.oguzdogdu.wallies.util.ChoiseDialogBuilder
import com.oguzdogdu.wallies.util.OptionLists
import com.oguzdogdu.wallies.util.observeInLifecycle
import com.oguzdogdu.wallies.util.setupRecyclerView
import com.oguzdogdu.wallies.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
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
                    clearAppCache(requireContext())
                    requireView().showToast(requireContext(), R.string.cache_state_string)
                }
            }
        }
    }

    private fun clearAppCache(context: Context) {
        val cacheDir = context.cacheDir
        deleteDir(cacheDir)
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir == null || !dir.isDirectory) {
            return false
        }
        val children = dir.list() ?: return false
        for (child in children) {
            val childDir = File(dir, child)
            val success = deleteDir(childDir)
            if (!success) {
                return false
            }
        }
        return dir.delete()
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
