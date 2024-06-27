package com.elizabet1926.solanawallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private var createWalletBtn: Button? = null
    private var importAccountFromMnemonicBtn: Button? = null
    private var importAccountFromPrivateKeyBtn: Button? = null
    private var estimatedSOLTransferCostBtn: Button? = null
    private var estimatedSPLTokenTransferCostBtn: Button? = null
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
        createWalletBtn = findViewById(R.id.createWallet)
        importAccountFromMnemonicBtn = findViewById(R.id.importAccountFromMnemonic)
        importAccountFromPrivateKeyBtn = findViewById(R.id.importAccountFromPrivateKey)
        estimatedSOLTransferCostBtn = findViewById(R.id.btn_estimatedSOLTransferCost)
        estimatedSPLTokenTransferCostBtn = findViewById(R.id.btn_estimatedSPLTokenTransferCost)
        getSOLBalance = findViewById(R.id.getSOLBalance)
        getSPLTokenBalance = findViewById(R.id.getSPLTokenBalance)
        getTokenAccountsByOwner = findViewById(R.id.getTokenAccountsByOwner)
        sendSOLBtn = findViewById(R.id.btn_sendSOL)
        sendSPLTokenBtn = findViewById(R.id.btn_sendSPLToken)

        createWalletBtn?.setOnClickListener{
            createWallet()
        }
        importAccountFromMnemonicBtn?.setOnClickListener{
            importAccountFromMnemonic()
        }
        importAccountFromPrivateKeyBtn?.setOnClickListener{
            importAccountFromPrivateKey()
        }
        estimatedSOLTransferCostBtn?.setOnClickListener{
            estimatedTransferCost(type = "SOLTransfer")
        }
        estimatedSPLTokenTransferCostBtn?.setOnClickListener{
            estimatedTransferCost(type = "SPLTokenTransfer")
        }
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

    private fun createWallet(){
        val intent = Intent(this@MainActivity, CreateWalletActivity::class.java)
        startActivity(intent)
    }
    private fun importAccountFromMnemonic(){
        val intent = Intent(this@MainActivity, ImportFromMnemonicActivity::class.java)
        startActivity(intent)
    }
    private fun importAccountFromPrivateKey(){
        val intent = Intent(this@MainActivity, ImportFromPrivateKeyActivity::class.java)
        startActivity(intent)
    }

    private fun estimatedTransferCost(type: String){
        val intent = Intent(this@MainActivity, EstimatedTransferCostActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
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