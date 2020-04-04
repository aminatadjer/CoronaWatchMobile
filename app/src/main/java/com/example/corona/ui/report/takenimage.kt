package com.example.corona.ui.report

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.corona.R
import kotlinx.android.synthetic.main.takenimage_fragment.*

class takenimage : Fragment() {

    companion object {
        fun newInstance() = takenimage()
    }

    private lateinit var viewModel: TakenimageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.takenimage_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TakenimageViewModel::class.java)
        // TODO: Use the ViewModel


    }

}
