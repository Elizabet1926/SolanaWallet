package com.elizabet1926.solanawallet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.elizabet1926.solanaweb.SolanaWeb

class ImportFromPrivateKeyActivity : AppCompatActivity() {
    private var title: TextView? = null
    private var privateKey: EditText? = null
    private var accountDetail: EditText? = null
    private var importAccountFromPrivateKeyBtn: Button? = null
    private var mWebView: WebView? = null
    private var solanaWeb:SolanaWeb? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.import_private_key_layout)
        setupContent()
    }

    private fun setupContent() {
        title = findViewById(R.id.title)
        privateKey = findViewById(R.id.privateKey)
        accountDetail = findViewById(R.id.account_detail)
        importAccountFromPrivateKeyBtn = findViewById(R.id.btn_importAccountFromPrivateKey)
        mWebView =  findViewById(R.id.webView)
        solanaWeb = SolanaWeb(this, _webView = mWebView!!)
        importAccountFromPrivateKeyBtn?.setOnClickListener{
            importAccountFromPrivateKey()
        }
    }
    private fun importAccountFromPrivateKey() {
        val onCompleted = {result : Boolean ->
            println("solanaWeb setup Completed------->>>>>")
            println(result)
            importAccountFromPrivateKeyAction()
        }
        if (solanaWeb?.isGenerateTronWebInstanceSuccess == false) {
            solanaWeb?.setup(true,onCompleted = onCompleted)
        }  else  {
            importAccountFromPrivateKeyAction()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun importAccountFromPrivateKeyAction() {
        val privateKey = privateKey?.text.toString()
        if (privateKey.isNotEmpty()) {
            val onCompleted = {state : Boolean, address: String,error: String ->
                this.runOnUiThread {
                    if(state){
                        val text = "address: " + address + "\n\n" +
                                "privateKey: " + privateKey
                        accountDetail?.setText(text)
                    } else {
                        accountDetail?.setText(error)
                    }
                }
            }
            solanaWeb?.importAccountFromPrivateKey(privateKey,onCompleted = onCompleted)
        }
    }
}