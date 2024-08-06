package com.training.ecommerce.ui.offers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.training.ecommerce.R


class OffersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: OffersFragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: OffersFragment")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG, "onHiddenChanged: OffersFragment = $hidden")
    }

    companion object {
        private const val TAG = "OffersFragment"
    }
}