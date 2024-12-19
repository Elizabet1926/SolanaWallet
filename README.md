# SolanaWallet
**SolanaWallet** is an Android toolbelt for interaction with the Solana network.

![language](https://img.shields.io/badge/Language-Kotlin-green)
![jitpack](https://img.shields.io/badge/support-jitpack-green)

![](Resource/Demo01.png)

For more specific usage, please refer to the [demo](https://github.com/Elizabet1926/SolanaWallet/tree/master/app)

### Highly Important Forward Guidance

```
I used the node "https://solana.maiziqianbao.net/" to test SOL transfers, SPL Token transfers, and other features,
all of which were successful. However, SPL Token transfers using this node are relatively slow, taking about 20 seconds. 
Therefore, I recommend purchasing nodes from https://www.quicknode.com/.
```

## JitPack.io

I strongly recommend https://jitpack.io
```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
dependencies {
    implementation 'com.github.Elizabet1926:SolanaWallet:1.0.5'
}
```

##### Setup SolanaWeb 
```kotlin
val onCompleted = {result : Boolean ->
    if (type == "SOL") SOLTransfer() else SPLTokenTransfer()
}
if (solanaweb?.isGenerateTronWebInstanceSuccess == false) {
    solanaweb?.setup(true,onCompleted)
} else  {
    if (type == "SOL") SOLTransfer() else SPLTokenTransfer()
}
```
##### Create Wallet
```Kotlin
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

```
##### Import Account From Mnemonic
```Kotlin
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

```
##### Import Account From PrivateKey
```Kotlin
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
```
##### Estimate SOL Cost
```Kotlin
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
solanaWeb?.estimatedSOLTransferCost(fromAddress,toAddress,amount,onCompleted = onCompleted)
```

##### Send SOL
```Kotlin
val privateKey = ""
val toAddress = ""
val amount = ""
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

```
##### Estimate SPL Token Cost
```Kotlin
val privateKey = ""
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
solanaWeb?.estimatedSPLTokenTransferCost(privateKey,toAddress,SPLTokenAddress,6.0,amount,onCompleted = onCompleted)
```
##### Send SPLToken
```Kotlin
val privateKey = ""
val toAddress = ""
val amount = ""
val splTokenAddress = ""
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
```
For more specific usage, please refer to the [demo](https://github.com/Elizabet1926/SolanaWallet/tree/master/app)


## License

SolanaWallet is released under the MIT license. [See LICENSE](https://github.com/Elizabet1926/SolanaWallet/blob/master/LICENSE) for details.
