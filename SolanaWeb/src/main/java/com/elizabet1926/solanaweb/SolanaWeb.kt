package com.elizabet1926.solanaweb

import android.content.Context
import android.graphics.Bitmap
import android.webkit.*
import java.lang.reflect.InvocationTargetException

public const val SolanaMainNet: String = "https://solana-mainnet.phantom.tech/"
public const val SolanaMainNet1: String = "https://solana.maiziqianbao.net"
public const val SolanaMainNet2: String = "https://api.mainnet-beta.solana.com"
public const val SolanaMainNet3: String = "https://solana-api.projectserum.com"
public const val SolanaMainNet4: String = "https://rpc.ankr.com/solana"

public const val SPLTokenUSDT: String = "Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB"

public class SolanaWeb(context: Context, _webView: WebView) {
    private val webView = _webView
    public var isGenerateTronWebInstanceSuccess: Boolean = false
    private var bridge = WebViewJavascriptBridge(_context = context,_webView = webView)
    var onCompleted = { _: Boolean  -> }
    private var showLog: Boolean = false
    init {
        setAllowUniversalAccessFromFileURLs(webView)
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
    }
    public fun setup(showLog: Boolean = true, onCompleted: (Boolean) -> Unit) {
        this.showLog = showLog
        this.onCompleted = onCompleted
        webView.webViewClient = webClient
        if (showLog) {
            bridge.consolePipe = object : ConsolePipe {
                override fun post(string : String){
                    println("Next line is javascript console.log->>>")
                    println(string)
                }
            }
        }
        webView.loadUrl("file:///android_asset/SolanaIndex.html")
        val handler = object :Handler {
            override fun handler(map: HashMap<String, Any>?, callback: Callback) {
                isGenerateTronWebInstanceSuccess = true
                onCompleted(true)
            }
        }
        bridge.register("generateSolanaWeb3",handler)
    }
    public fun getSOLBalance(address: String,
                              endpoint: String = SolanaMainNet,
                              onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["address"] = address
        data["endpoint"] = endpoint
        bridge.call("getSOLBalance", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["result"] as Boolean
                val balance = if (!state) "error" else  map["balance"] as String
                onCompleted(state,balance)
            }
        })
    }
    public fun getSPLTokenBalance(address: String,
                                  SPLTokenAddress: String = SPLTokenUSDT ,
                                  decimalPoints: Double = 6.0,
                                  endpoint: String = SolanaMainNet,
                                  onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["address"] = address
        data["SPLTokenAddress"] = SPLTokenAddress
        data["endpoint"] = endpoint
        data["decimalPoints"] = decimalPoints
        bridge.call("getSPLTokenBalance", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["result"] as Boolean
                val balance = if (!state) "error" else  map["balance"] as String
                onCompleted(state,balance)
            }
        })
    }
    public fun getTokenAccountsByOwner(address: String,
                                  endpoint: String = SolanaMainNet,
                                  onCompleted: (Boolean,String) -> Unit) {
        val data = java.util.HashMap<String, Any>()
        data["address"] = address
        data["endpoint"] = endpoint
        bridge.call("getTokenAccountsByOwner", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["result"] as Boolean
                val tokenAccountsJson = if (!state) "error" else  map["tokenAccounts"] as String
                onCompleted(state,tokenAccountsJson)
            }
        })
    }
    public fun solanaTransfer(privateKey: String,
                                     toAddress: String,
                                        amount: String,
                                      endpoint: String = SolanaMainNet,
                                     onCompleted: (Boolean,String) -> Unit) {
        val number = amount.toDouble() * Math.pow(10.0,9.0)
        val data = java.util.HashMap<String, Any>()
        data["toPublicKey"] = toAddress
        data["amount"] = number
        data["endpoint"] = endpoint
        data["secretKey"] = privateKey
        bridge.call("solanaMainTransfer", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["result"] as Boolean
                val txid = if (!state) "error" else  map["txid"] as String
                onCompleted(state,txid)
            }
        })
    }
    public fun solanaTokenTransfer(privateKey: String,
                                   toAddress: String,
                                   mintAuthority:String = SPLTokenUSDT,
                                   endpoint: String = SolanaMainNet,
                                        amount: String,
                                   decimalPoints:Double = 6.0,
                                   onCompleted: (Boolean,String) -> Unit) {
        val number = amount.toDouble() * Math.pow(10.0,decimalPoints)
        val data = java.util.HashMap<String, Any>()
        data["toPublicKey"] = toAddress
        data["amount"] = number
        data["mintAuthority"] = mintAuthority
        data["endpoint"] = endpoint
        data["decimals"] = decimalPoints
        data["secretKey"] = privateKey
        bridge.call("solanaTokenTransfer", data, object : Callback {
            override fun call(map: HashMap<String, Any>?){
                if (showLog) {
                    println(map)
                }
                val state =  map!!["result"] as Boolean
                val txid = if (!state) "error" else  map["txid"] as String
                onCompleted(state,txid)
            }
        })
    }
    private val webClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            println("shouldOverrideUrlLoading")
            return false
        }
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            println("onPageStarted")
            bridge.injectJavascript()
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            println("onPageFinished")
        }
    }
    //Allow Cross Domain
    private fun setAllowUniversalAccessFromFileURLs(webView: WebView) {
        try {
            val clazz: Class<*> = webView.settings.javaClass
            val method = clazz.getMethod(
                "setAllowUniversalAccessFromFileURLs", Boolean::class.javaPrimitiveType
            )
            method.invoke(webView.settings, true)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}