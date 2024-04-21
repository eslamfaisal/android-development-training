package com.training.ecommerce.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.training.ecommerce.BuildConfig
import com.training.ecommerce.R
import com.training.ecommerce.data.models.Resource
import com.training.ecommerce.databinding.FragmentRegisterBinding
import com.training.ecommerce.ui.auth.viewmodel.RegisterViewModel
import com.training.ecommerce.ui.auth.viewmodel.RegisterViewModelFactory
import com.training.ecommerce.ui.common.views.ProgressDialog
import com.training.ecommerce.ui.showSnakeBarError
import com.training.ecommerce.utils.CrashlyticsUtils
import com.training.ecommerce.utils.RegisterException
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(contextValue = requireContext())
    }

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // ActivityResultLauncher for the sign-in intent
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignUpResult(task)
            } else {
                view?.showSnakeBarError(getString(R.string.google_sign_in_field_msg))
            }
        }

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
        binding.googleSignupBtn.setOnClickListener {
            registerWithGoogleRequest()
        }
        binding.facebookSignupBtn.setOnClickListener {
//            loginWithFacebook()
        }

    }

    private fun registerWithGoogleRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.clientServerId).requestEmail().requestProfile()
            .requestServerAuthCode(BuildConfig.clientServerId).build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun handleSignUpResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: Exception) {
            view?.showSnakeBarError(e.message ?: getString(R.string.generic_err_msg))
            val msg = e.message ?: getString(R.string.generic_err_msg)
            logAuthIssueToCrashlytics(msg, "Google")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        registerViewModel.signUpWithGoogle(idToken)
    }


    private fun logAuthIssueToCrashlytics(msg: String, provider: String) {
        CrashlyticsUtils.sendCustomLogToCrashlytics<RegisterException>(
            msg,
            CrashlyticsUtils.REGISTER_KEY to msg,
            CrashlyticsUtils.LOGIN_PROVIDER to provider,
        )
    }


    // create successful login dialog function
    private fun showLoginSuccessDialog() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Register Success")
            .setMessage("We have sent you an email verification link. Please verify your email to login.")
            .setPositiveButton(
                "OK"
            ) { dialog, which ->
                dialog?.dismiss()
                findNavController().popBackStack()
            }.create().show()
    }

}