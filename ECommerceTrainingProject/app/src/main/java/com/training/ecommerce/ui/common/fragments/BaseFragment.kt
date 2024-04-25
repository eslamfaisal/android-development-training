package com.training.ecommerce.ui.common.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.training.ecommerce.BR
import com.training.ecommerce.ui.common.views.ProgressDialog

abstract class BaseFragment<DB : ViewDataBinding, VM : ViewModel> : Fragment() {

    val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    protected abstract val viewModel: VM
    protected var _binding: DB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        return binding.root
    }

    /**
     * Get layout resource id which inflate in onCreateView.
     */
    @LayoutRes
    abstract fun getLayoutResId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doDataBinding()
        init()
    }

    /**
     * Do your other stuff in init after binding layout.
     */
    abstract fun init()

    private fun doDataBinding() {
        // it is extra if you want to set life cycle owner in binding
        binding.lifecycleOwner = viewLifecycleOwner
        // Here your viewModel and binding variable imlementation
        binding.setVariable(
            BR.viewmodel, viewModel
        )  // In all layout the variable name should be "viewModel"
        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
