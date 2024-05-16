package com.training.ecommerce.ui.account.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.training.ecommerce.R

class AccountFragment : Fragment() {

    private var dataInitialized = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (isVisible && !dataInitialized) {
            dataInitialized = true
            Log.d(TAG, "onViewCreated: AccountFragment")
            initViews()
            initViewModel()
        }
    }

    private fun initViewModel() {

    }

    private fun initViews() {

    }

    companion object {
        private const val TAG = "AccountFragment"
    }
}