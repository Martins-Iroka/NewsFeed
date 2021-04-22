package com.martdev.android.newsfeed.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.martdev.android.newsfeed.databinding.NewsPageViewBinding

class NewsFeedWebView : Fragment() {

    private val args: NewsFeedWebViewArgs by navArgs()

    private var binding: NewsPageViewBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewsPageViewBinding.inflate(inflater, container, false)


        binding?.webView!!.run {
            settings.javaScriptEnabled = true
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress == 100) {
                        binding?.progressBar?.visibility = View.GONE
                    } else {
                        binding?.progressBar?.run {
                            visibility = View.VISIBLE
                            progress = newProgress
                        }
                    }
                }
            }
            webViewClient = WebViewClient()
            loadUrl(args.url)
        }

        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
