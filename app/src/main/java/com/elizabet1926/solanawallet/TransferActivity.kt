package com.elizabet1926.solanawallet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.elizabet1926.solanaweb.SolanaMainNet
import com.elizabet1926.solanaweb.SolanaWeb

class TransferActivity : AppCompatActivity() {
    private var title: TextView? = null
    private var hashValue: TextView? = null
    private var privateKeyEditText: EditText? = null
    private var receiveEditText: EditText? = null
    private var amountEditText: EditText? = null
    private var SPLTokenEditText: EditText? = null
    private var transferBtn: Button? = null
    private var detailBtn: Button? = null
    private var solanaweb: SolanaWeb? = null
    private var mWebView: WebView? = null
    private var type: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transfer_layout)
        setupContent()
        getData()
    }
    private fun setupContent(){
        SPLTokenEditText  = findViewById(R.id.SPLTokenAddress)
        privateKeyEditText = findViewById(R.id.private_key)
        receiveEditText = findViewById(R.id.receive_address)
        amountEditText = findViewById(R.id.amount)
        title = findViewById(R.id.title)
        hashValue = findViewById(R.id.hashValue)
        transferBtn = findViewById(R.id.btn_transfer)
        detailBtn = findViewById(R.id.btn_detail)
        mWebView = findViewById(R.id.webView)
        solanaweb = SolanaWeb(context = this, _webView = mWebView!!)
        transferBtn?.setOnClickListener{
            transfer()
        }
        detailBtn?.setOnClickListener{
            val hash = hashValue?.text.toString()
            if (hash.length < 20) { return@setOnClickListener}
            val urlString = "https://solscan.io/tx/$hash"
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(urlString))
                startActivity(intent)
            } catch (e: Exception) {
                println("The current phone does not have a browser installed")
            }
        }

    }
    private fun transfer(){
        val onCompleted = {result : Boolean ->
            if (type == "SOL") SOLTransfer() else SPLTokenTransfer()
        }
        if (solanaweb?.isGenerateTronWebInstanceSuccess == false) {
            solanaweb?.setup(true,onCompleted)
        } else  {
            if (type == "SOL") SOLTransfer() else SPLTokenTransfer()
        }
    }
    private fun SOLTransfer() {
        val privateKey = privateKeyEditText?.text.toString()
        val toAddress = receiveEditText?.text.toString()
        val amount = amountEditText?.text.toString()
        if (toAddress.isNotEmpty() && amount.isNotEmpty() && privateKey.isNotEmpty()) {
            val onCompleted = {state : Boolean, txid: String,error:String ->
                this.runOnUiThread {
                    if (state){
                        hashValue?.text = txid
                    } else {
                        hashValue?.text = error
                    }
                }
            }
            solanaweb?.solanaTransfer(privateKey,toAddress,amount, endpoint = SolanaMainNet,onCompleted)

        }
    }
    private fun SPLTokenTransfer() {
        val privateKey = privateKeyEditText?.text.toString()
        val toAddress = receiveEditText?.text.toString()
        val amount = amountEditText?.text.toString()
        val splTokenAddress = SPLTokenEditText?.text.toString()
        if (toAddress.isNotEmpty() && amount.isNotEmpty() && privateKey.isNotEmpty() && splTokenAddress.isNotEmpty()) {
            val onCompleted = {state : Boolean, txid: String,error:String ->
                this.runOnUiThread {
                    if (state){
                        hashValue?.text = txid
                    } else {
                        hashValue?.text = error
                    }
                }
            }
            solanaweb?.solanaTokenTransfer(privateKey,
                toAddress,
                splTokenAddress,
                endpoint = SolanaMainNet,
                amount,
                decimalPoints = 6.0,
                onCompleted)
        }
    }

    private fun getData() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            type = bundle.getString("type") ?: ""
            title?.text = if (type == "SOL") "SOL Transfer" else "SPLToken Transfer"
            if (type == "SOL") {
                SPLTokenEditText?.setVisibility(View.GONE)
            }
        }
    }

}