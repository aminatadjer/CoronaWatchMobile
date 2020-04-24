package com.example.corona.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.corona.R
import kotlinx.android.synthetic.main.login_fragment.*


class login : Fragment() {

    companion object {
        fun newInstance() = login()
    }

    var login =true
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel


            loginFacebook.setOnClickListener {


                if(login){
                    val reportAction = loginDirections.reportAction()
                    Navigation.findNavController(it).navigate(reportAction)
                }



            }
            loginGmail.setOnClickListener {


                if(login){
                    val reportAction = loginDirections.reportAction()
                    Navigation.findNavController(it).navigate(reportAction)
                }

            }





    }

}
