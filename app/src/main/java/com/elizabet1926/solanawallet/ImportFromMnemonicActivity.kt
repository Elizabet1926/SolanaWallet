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

class ImportFromMnemonicActivity : AppCompatActivity() {
    private var title: TextView? = null
    private var mnemonic: EditText? = null
    private var accountDetail: EditText? = null
    private var importAccountFromMnemonicBtn: Button? = null
    private var mWebView: WebView? = null
    private var solanaWeb:SolanaWeb? = null
    private var type: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.import_mnemonic_layout)
        setupContent()
        getData()
    }

    private fun setupContent() {
        title = findViewById(R.id.title)
        mnemonic = findViewById(R.id.mnemonic)
        accountDetail = findViewById(R.id.account_detail)
        importAccountFromMnemonicBtn = findViewById(R.id.btn_importAccountFromMnemonic)
        mWebView =  findViewById(R.id.webView)
        solanaWeb = SolanaWeb(this, _webView = mWebView!!)
        importAccountFromMnemonicBtn?.setOnClickListener{
            importAccountFromMnemonic()
        }
    }
    private fun importAccountFromMnemonic() {
        val onCompleted = {result : Boolean ->
            println("solanaWeb setup Completed------->>>>>")
            println(result)
            importAccountFromMnemonicAction()
        }
        if (solanaWeb?.isGenerateTronWebInstanceSuccess == false) {
            solanaWeb?.setup(true,onCompleted = onCompleted)
        }  else  {
            importAccountFromMnemonicAction()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun importAccountFromMnemonicAction() {
        val mnemonic = mnemonic?.text.toString()
        if (mnemonic.isNotEmpty()) {
            val onCompleted = {state : Boolean, address : String, privateKey:String,error: String ->
                this.runOnUiThread {
                    if(state){
                        val text = "address: " + address + "\n\n" +
                                "mnemonic: " + mnemonic + "\n\n" +
                                "privateKey: " + privateKey
                        accountDetail?.setText(text)
                    } else {
                        accountDetail?.setText(error)
                    }
                }
            }
            solanaWeb?.importAccountFromMnemonic(mnemonic,onCompleted = onCompleted)
        }
    }

    private fun getData() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            type = bundle.getString("type") ?: ""
        }
    }
}