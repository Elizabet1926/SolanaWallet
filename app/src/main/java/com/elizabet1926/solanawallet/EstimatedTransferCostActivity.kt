package com.elizabet1926.solanawallet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.elizabet1926.solanaweb.SolanaMainNet
import com.elizabet1926.solanaweb.SolanaMainNet1
import com.elizabet1926.solanaweb.SolanaWeb

class EstimatedTransferCostActivity : AppCompatActivity() {
    private var title: TextView? = null
    private var estimateCostTextView: TextView? = null
    private var senderAddress: EditText? = null
    private var receiveAddress: EditText? = null
    private var amount: EditText? = null
    private var SPLTokenAddress: EditText? = null
    private var estimateCostBtn: Button? = null
    private var mWebView: WebView? = null
    private var solanaWeb:SolanaWeb? = null
    private var type: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.estimate_cost_layout)
        setupContent()
        getData()
        showMyDialog()
    }
    private fun showMyDialog() {
        DialogUtil.showAlertDialog(this)
    }

    private fun setupContent() {
        title = findViewById(R.id.title)
        estimateCostTextView = findViewById(R.id.estimate_cost_textView)
        senderAddress = findViewById(R.id.send_address)
        receiveAddress = findViewById(R.id.receive_address)
        amount = findViewById(R.id.amount)
        SPLTokenAddress = findViewById(R.id.SPLTokenAddress)
        estimateCostBtn = findViewById(R.id.btn_estimate_cost)
        mWebView = findViewById(R.id.webView)
        solanaWeb = SolanaWeb(this, _webView = mWebView!!)
        estimateCostBtn?.setOnClickListener{
            preEstimateCost()
        }
    }
    private fun preEstimateCost() {
        val onCompleted = {result : Boolean ->
            println("solanaWeb setup Completed------->>>>>")
            println(result)
            if (type == "SOLTransfer") estimateSOLCost() else estimateSPLTokenCost()
        }
        if (solanaWeb?.isGenerateTronWebInstanceSuccess == false) {
            solanaWeb?.setup(true,onCompleted = onCompleted)
        }  else  {
            if (type == "SOLTransfer") estimateSOLCost() else  estimateSPLTokenCost()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun estimateSOLCost() {
        val fromAddress = senderAddress?.text.toString()
        val toAddress = receiveAddress?.text.toString()
        val amount = amount?.text.toString()
        if (fromAddress.isNotEmpty() && toAddress.isNotEmpty() && amount.isNotEmpty()) {
            val onCompleted = {state : Boolean, cost: String,error: String ->
                this.runOnUiThread {
                    val  titleTip = if(type == "SOLTransfer") "SOL Transfer: " else "SPLToken Transfer: "
                    if(state){
                        estimateCostTextView?.text = titleTip + "estimated cost $cost SOL"
                    } else {
                        estimateCostTextView?.text = error
                    }
                }
            }
            solanaWeb?.estimatedSOLTransferCost(fromAddress,toAddress,amount,endpoint = SolanaMainNet1,onCompleted = onCompleted)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun estimateSPLTokenCost() {
        val p1 = "55r9gYDZPnRPyaBZfZvLnFzUTkox16bqC7byKPonJj1M7C"
        val p2 = "BXQHXhBs6duKYZGgHbrHpaSDeUrBrai3mhAGVQf8Ca"
        val privateKey = p1 + p2
        val toAddress = receiveAddress?.text.toString()
        val amount = amount?.text.toString()
        val SPLTokenAddress = SPLTokenAddress?.text.toString()
        if (SPLTokenAddress.isNotEmpty() && toAddress.isNotEmpty() && amount.isNotEmpty()) {
            val onCompleted = {state : Boolean, cost: String,error: String ->
                this.runOnUiThread {
                    val  titleTip = if(type == "SOLTransfer") "SOL Transfer: " else "SPLToken Transfer: "
                    if(state){
                        estimateCostTextView?.text = titleTip + "estimated cost $cost SOL"
                    } else {
                        estimateCostTextView?.text = error
                    }
                }
            }
            solanaWeb?.estimatedSPLTokenTransferCost(privateKey,toAddress,SPLTokenAddress,6.0,amount, endpoint = SolanaMainNet1 ,onCompleted = onCompleted)
        }
    }
    private fun getData() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            type = bundle.getString("type") ?: ""
            title?.text = if (type == "SOLTransfer") "SOLTransfer Estimate Cost" else "SPLToken Transfer Estimate Cost"
            if (type == "SOLTransfer") {
                SPLTokenAddress?.setVisibility(View.GONE)
            }
        }
    }
}