package com.example.vefkhistkaosani

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class AporizmebiFragment : Fragment() {

    var mWebView: WebView? = null

    private inner class JavascriptInterface
    {
        @android.webkit.JavascriptInterface
        fun copyText(text: String){


            val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(null, text)
            clipboard.setPrimaryClip(clip)
        }
        @android.webkit.JavascriptInterface
        fun shareText(text: String){
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val v: View = inflater.inflate(R.layout.fragment_aporizmebi, container, false)
        mWebView = v.findViewById<View>(R.id.view_main_apor) as WebView
        val id = Login.logged;
        mWebView!!.loadUrl("http://vefxistyaosani.ge/android/?page=aporizmebi&userid=$id")

        // Enable Javascript
        val webSettings = mWebView!!.settings
        webSettings.javaScriptEnabled = true
        mWebView?.addJavascriptInterface(JavascriptInterface(), "javascript_bridge")
        // Force links and redirects to open in the WebView instead of in a browser
        mWebView!!.webViewClient = WebViewClient()
        return v
    }
    override fun onResume() {
        super.onResume()

        // Set title bar
        (activity as Dashboard?)
                ?.setActionBarTitle("აფორიზმები")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        val navBar: BottomNavigationView = activity!!.findViewById(R.id.bottom_nav_view)
        val searchView = searchItem?.actionView as SearchView






        MenuItemCompat.setOnActionExpandListener(searchItem, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                navBar.visibility = View.GONE
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                navBar.visibility = View.VISIBLE

                val url = mWebView?.url
                val sanitizer = UrlQuerySanitizer(url)
                val inAp = sanitizer.getValue("in")
                val id = Login.logged;
                mWebView?.loadUrl("http://vefxistyaosani.ge/android/?page=aporizmebi&in=$inAp&userid=$id")
                return true
            }
        })


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus();

                return true
            }


            override fun onQueryTextChange(newText: String?): Boolean {

                val url = mWebView?.url

                val sanitizer = UrlQuerySanitizer(url)

                val inAp = sanitizer.getValue("in")


                if (newText != null) {
                    if (newText.length > 0) {
                        val id = Login.logged;
                        mWebView?.loadUrl("http://vefxistyaosani.ge/android/?page=aporizmebi&in=$inAp&q=$newText&userid=$id")
                    }
                };
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {




        val id = Login.logged;
        when (item.itemId) {

            R.id.anbani -> mWebView?.loadUrl("http://vefxistyaosani.ge/android/?page=aporizmebi&in=anbani&userid=$id")
            R.id.tavi -> mWebView?.loadUrl("http://vefxistyaosani.ge/android/?page=aporizmebi&in=tavi&userid=$id")
            R.id.tema -> mWebView?.loadUrl("http://vefxistyaosani.ge/android/?page=aporizmebi&in=tema&userid=$id")

        }


        return super.onOptionsItemSelected(item)
    }
}