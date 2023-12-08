package com.oguzdogdu.wallieshd.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.oguzdogdu.wallieshd.R
import com.oguzdogdu.wallieshd.core.snackbar.CustomSnackbarImpl
import com.oguzdogdu.wallieshd.core.snackbar.MessageType
import com.oguzdogdu.wallieshd.util.CheckConnection
import com.oguzdogdu.wallieshd.util.navigateSafe
import com.oguzdogdu.wallieshd.util.navigateSafeWithDirection
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null
    val binding: VB
        get() = _binding as VB

    @Inject
    lateinit var connection: CheckConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstExecution()
    }

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
        initViews()
        observeByConnection()
        initListeners()
    }

    private fun observeByConnection() {
        connection.observe(viewLifecycleOwner) {
            when (it) {
                true -> observeData()
                false -> showMessage(
                    message = resources.getString(R.string.internet_error),
                    type = MessageType.ERROR
                )

                else -> {}
            }
        }
    }

    open fun firstExecution() {}

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

    fun showMessage(message: String, type: MessageType) {
        CustomSnackbarImpl.build(this, type, message, null).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
