package com.oguzdogdu.wallies.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment(), Presenters {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    override fun observeData() {
    }

    override fun initViews() {
    }

    override fun initListeners() {
    }

    fun navigate(@IdRes id: Int, extras: Bundle?) {
        findNavController().navigate(id, extras)
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