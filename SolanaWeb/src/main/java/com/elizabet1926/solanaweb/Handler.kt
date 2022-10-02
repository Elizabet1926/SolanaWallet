package com.elizabet1926.solanaweb

interface Handler {
    fun handler(map: HashMap<String, Any>?, callback: Callback)
}