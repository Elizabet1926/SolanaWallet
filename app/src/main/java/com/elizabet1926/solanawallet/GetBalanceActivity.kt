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

class GetBalanceActivity : AppCompatActivity() {
    private var title: TextView? = null
    private var balance: TextView? = null
    private var address: EditText? = null
    private var SPLTokenAddress: EditText? = null
    private var getBalanceBtn: Button? = null
    private var mWebView: WebView? = null
    private var solanaWeb:SolanaWeb? = null
    private var type: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.balance_layout)
        setupContent()
        getData()
    }

    private fun setupContent() {
        title = findViewById(R.id.title)
        balance = findViewById(R.id.balance)
        address = findViewById(R.id.address)
        SPLTokenAddress = findViewById(R.id.SPLTokenAddress)
        getBalanceBtn = findViewById(R.id.btn_getBalance)
        mWebView =  findViewById(R.id.webView)
        solanaWeb = SolanaWeb(this, _webView = mWebView!!)
        getBalanceBtn?.setOnClickListener{
            getBalance()
        }
    }
    private fun getBalance() {
        val onCompleted = {result : Boolean ->
            println("solanaWeb setup Completed------->>>>>")
            println(result)
            if (type == "SOL") getSOLBalance() else getSPLTokenBalance()
        }
        if (solanaWeb?.isGenerateTronWebInstanceSuccess == false) {
            solanaWeb?.setup(true,onCompleted = onCompleted)
        }  else  {
            if (type == "SOL") getSOLBalance() else  getSPLTokenBalance()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getSOLBalance() {
        val address = address?.text.toString()
        if (address.isNotEmpty()) {
            val onCompleted = {state : Boolean, amount: String,error: String ->
                this.runOnUiThread {
                    val  titleTip = if(type == "SOL") "SOL Balance: " else "SPLToken Balance: "
                    if(state){
                        balance?.text = titleTip + amount
                    } else {
                        balance?.text = error
                    }
                }
            }
            balance?.text = "fetching..."
            solanaWeb?.getSOLBalance(address,onCompleted = onCompleted)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getSPLTokenBalance() {
        val address = address?.text.toString()
        val SPLTokenAddress = SPLTokenAddress?.text.toString()
        if (address.isNotEmpty() && SPLTokenAddress.isNotEmpty()) {
            val onCompleted = {state : Boolean, amount: String,error: String ->
                this.runOnUiThread {
                    val  titleTip = if(type == "SOL") "SOL Balance: " else "SPLToken Balance: "
                    if(state){
                        balance?.text = titleTip + amount
                    } else {
                        balance?.text = error
                    }
                }
            }
            balance?.text = "fetching..."
            solanaWeb?.getSPLTokenBalance(address,onCompleted = onCompleted)
        }
    }
    private fun getData() {
        //接收传值
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            type = bundle.getString("type") ?: ""
            title?.text = if (type == "SOL") "GET SOL Balance" else "GET SPLToken Balance"
            if (type == "SOL") {
                SPLTokenAddress?.setVisibility(View.GONE)
            }
        }
    }
}