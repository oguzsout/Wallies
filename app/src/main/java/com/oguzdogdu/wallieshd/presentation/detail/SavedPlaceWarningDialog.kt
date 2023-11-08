package com.oguzdogdu.wallieshd.presentation.detail

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.oguzdogdu.wallieshd.core.BaseBottomSheetDialogFragment
import com.oguzdogdu.wallieshd.databinding.DialogSavedPlaceWarningBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedPlaceWarningDialog : BaseBottomSheetDialogFragment<DialogSavedPlaceWarningBinding>(
    DialogSavedPlaceWarningBinding::inflate
) {

    override fun initViews() {
        super.initViews()
        this.isCancelable = false
    }

    override fun initListeners() {
        super.initListeners()
        binding.buttonContinue.setOnClickListener {
            parentFragment?.setFragmentResult(PROCESS_KEY, bundleOf(RESULT_EXTRA_KEY to true))
            this.dismiss()
        }
        binding.buttonLogin.setOnClickListener {
            setFragmentResult(PROCESS_KEY, bundleOf(RESULT_EXTRA_KEY to true))
            navigateWithDirection(SavedPlaceWarningDialogDirections.toLogin())
        }
    }

    companion object {
        const val PROCESS_KEY = "continue"
        const val RESULT_EXTRA_KEY = "extra_key"
    }
}
