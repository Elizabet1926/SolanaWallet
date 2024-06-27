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

class CreateWalletActivity : AppCompatActivity() {
    private var title: TextView? = null
    private var wallet: EditText? = null
    private var createWalletBtn: Button? = null
    private var mWebView: WebView? = null
    private var solanaWeb:SolanaWeb? = null
    private var type: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_wallet_layout)
        setupContent()
    }

    private fun setupContent() {
        title = findViewById(R.id.title)
        wallet = findViewById(R.id.wallet)
        createWalletBtn = findViewById(R.id.btn_createWallet)
        mWebView =  findViewById(R.id.webView)
        solanaWeb = SolanaWeb(this, _webView = mWebView!!)
        createWalletBtn?.setOnClickListener{
            createWallet()
        }
    }
    private fun createWallet() {
        val onCompleted = {result : Boolean ->
            println("solanaWeb setup Completed------->>>>>")
            println(result)
            createWalletAction()
        }
        if (solanaWeb?.isGenerateTronWebInstanceSuccess == false) {
            solanaWeb?.setup(false,onCompleted = onCompleted)
        }  else  {
            createWalletAction()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun createWalletAction() {
        val onCompleted = {state : Boolean, address : String, privateKey:String, mnemonic:String,error: String ->
            this.runOnUiThread {
                if(state){
                  val text = "address: " + address + "\n\n" +
                          "mnemonic: " + mnemonic + "\n\n" +
                          "privateKey: " + privateKey
                    wallet?.setText(text)
                } else {
                    wallet?.setText(error)
                }
            }
        }
        solanaWeb?.createWallet(onCompleted = onCompleted)
    }
}