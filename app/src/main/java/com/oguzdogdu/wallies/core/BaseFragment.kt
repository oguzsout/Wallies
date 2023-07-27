package com.oguzdogdu.wallies.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.util.CheckConnection
import com.oguzdogdu.wallies.util.navigateSafe
import com.oguzdogdu.wallies.util.navigateSafeWithDirection
import com.oguzdogdu.wallies.util.showToast
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    @Inject
    lateinit var connection: CheckConnection

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
        observeByConnection()
        initViews()
        initListeners()
    }

    private fun observeByConnection() {
        connection.observe(viewLifecycleOwner) {
            when (it) {
                true -> observeData()
                false -> requireView().showToast(requireContext(), R.string.internet_error)
                null -> TODO()
            }
        }
    }

    open fun observeData() {}

    open fun initViews() {}

    open fun initListeners() {}

    fun navigateWithDirection(navDirections: NavDirections) {
        findNavController().navigateSafeWithDirection(directions = navDirections)
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
