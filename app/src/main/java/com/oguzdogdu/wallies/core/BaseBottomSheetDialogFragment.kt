package com.oguzdogdu.wallies.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oguzdogdu.wallies.util.navigateSafe

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) :
    BottomSheetDialogFragment() {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) throw IllegalArgumentException("Binding cannot be null")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initViews()
        initListeners()
    }

    open fun observeData() {
    }

    open fun initViews() {
    }

    open fun initListeners() {
    }

    fun navigate(@IdRes id: Int, extras: Bundle?) {
        findNavController().navigateSafe(id, extras)
    }

    fun navigateBack(@IdRes destination: Int? = null) {
        if (destination != null) {
            findNavController().popBackStack(destination, false)
        } else {
            if (!findNavController().popBackStack()) {
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
