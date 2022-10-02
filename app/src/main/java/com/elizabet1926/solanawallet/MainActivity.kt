package com.elizabet1926.solanawallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private var getSOLBalance: Button? = null
    private var getSPLTokenBalance: Button? = null
    private var getTokenAccountsByOwner: Button? = null

    private var sendSOLBtn: Button? = null
    private var sendSPLTokenBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupContent()
    }
    private fun setupContent(){
        getSOLBalance = findViewById(R.id.getSOLBalance)
        getSPLTokenBalance = findViewById(R.id.getSPLTokenBalance)
        getTokenAccountsByOwner = findViewById(R.id.getTokenAccountsByOwner)
        sendSOLBtn = findViewById(R.id.btn_sendSOL)
        sendSPLTokenBtn = findViewById(R.id.btn_sendSPLToken)
        getSOLBalance?.setOnClickListener{
            getBalance(type = "SOL")
        }
        getSPLTokenBalance?.setOnClickListener{
            getBalance(type = "SPLToken")
        }
        getTokenAccountsByOwner?.setOnClickListener{
            getTokenAccountsByOwner()
        }
        sendSOLBtn?.setOnClickListener{
            transfer(type = "SOL")
        }
        sendSPLTokenBtn?.setOnClickListener{
            transfer(type = "SPLToken")
        }
    }
    private fun transfer(type: String){
        val intent = Intent(this@MainActivity, TransferActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
    }
    private fun getBalance(type: String){
        val intent = Intent(this@MainActivity, GetBalanceActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
    }
    private fun getTokenAccountsByOwner(){
        val intent = Intent(this@MainActivity, GetTokenAccountsByOwnerActivity::class.java)
        startActivity(intent)
    }
}