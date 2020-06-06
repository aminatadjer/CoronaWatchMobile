package com.example.corona.ui.post

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient

import com.example.corona.R
import kotlinx.android.synthetic.main.article_page_fragment.*

class ArticlePage : Fragment() {

    companion object {
        fun newInstance() = ArticlePage()
    }

    private lateinit var sfeArgs:ArticlePageArgs
    private lateinit var viewModel: ArticlePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_page_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArticlePageViewModel::class.java)

        sfeArgs=ArticlePageArgs.fromBundle(arguments!!)

        if(sfeArgs.url!="null"){
            web_view_page.webViewClient= WebViewClient()
            web_view_page.loadUrl(sfeArgs.url)
            val web_settings:WebSettings=web_view_page.settings
            web_settings.javaScriptEnabled=true
        }
    }

}
