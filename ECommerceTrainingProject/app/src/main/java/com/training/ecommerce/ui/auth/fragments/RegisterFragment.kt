package com.training.ecommerce.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.databinding.FragmentRegisterBinding
import com.training.ecommerce.ui.auth.viewmodel.RegisterViewModel
import com.training.ecommerce.ui.auth.viewmodel.RegisterViewModelFactory
import com.training.ecommerce.ui.common.views.ProgressDialog
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(contextValue = requireContext())
    }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = registerViewModel
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initListeners()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            registerViewModel.registerState.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        progressDialog.show()
                    }

                    is Resource.Success -> {
                        progressDialog.dismiss()

                        showLoginSuccessDialog()
                    }

                    is Resource.Error -> {
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.signInTv.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    // create successful login dialog function
    private fun showLoginSuccessDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Login Success")
            .setMessage("We have sent you an email verification link. Please verify your email to login.")
            .setPositiveButton(
                "OK"
            ) { dialog, which ->
                dialog?.dismiss()
                findNavController().popBackStack()
            }.create().show()
    }

}