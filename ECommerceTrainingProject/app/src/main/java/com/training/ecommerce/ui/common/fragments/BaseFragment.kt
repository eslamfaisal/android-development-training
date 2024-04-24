package com.training.ecommerce.ui.common.fragments

import androidx.fragment.app.Fragment
import com.training.ecommerce.ui.common.views.ProgressDialog

abstract class BaseFragment : Fragment() {

    val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

}