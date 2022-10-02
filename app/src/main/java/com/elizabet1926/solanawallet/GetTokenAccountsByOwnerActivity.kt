package com.elizabet1926.solanawallet

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.elizabet1926.solanaweb.SolanaWeb

class GetTokenAccountsByOwnerActivity : AppCompatActivity() {
    private var title: TextView? = null
    private var address: EditText? = null
    private var accountDetail: EditText? = null
    private var getDetailBtn: Button? = null
    private var solanaWeb:SolanaWeb? = null
    private var mWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_layout)
        setupContent()
    }
    private fun setupContent() {
        title = findViewById(R.id.title)
        accountDetail = findViewById(R.id.account_detail)
        address = findViewById(R.id.address)
        getDetailBtn = findViewById(R.id.btn_getTokenAccountsByOwner)
        mWebView =  findViewById(R.id.webView)
        solanaWeb = SolanaWeb(this, _webView = mWebView!!)
        getDetailBtn?.setOnClickListener{
            setup()
        }
    }
    private fun setup() {
        val onCompleted = {result : Boolean ->
            println("solanaWeb setup Completed------->>>>>")
            println(result)
            getTokenAccountsByOwner()
        }
        if (solanaWeb?.isGenerateTronWebInstanceSuccess == false) {
            solanaWeb?.setup(true,onCompleted = onCompleted)
        }  else  {
            getTokenAccountsByOwner()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getTokenAccountsByOwner() {
        val address = address?.text.toString()
        if (address.isNotEmpty()) {
            val onCompleted = {result : Boolean, tokenAccounts: String ->
                this.runOnUiThread {
                    accountDetail?.setText(tokenAccounts)
                }
            }
            accountDetail?.setText("fetching...")
            solanaWeb?.getTokenAccountsByOwner(address,onCompleted = onCompleted)
        }
    }
}