package com.oguzdogdu.wallies.presentation.login

import com.oguzdogdu.wallies.R
import com.oguzdogdu.wallies.core.BaseFragment
import com.oguzdogdu.wallies.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    override fun initViews() {
        super.initViews()
        binding.button.setOnClickListener {
            navigate(R.id.toMain, null)
        }
    }
}