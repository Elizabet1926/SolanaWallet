package com.elizabet1926.solanawallet
import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.net.Uri
import android.content.Intent

object DialogUtil {

    fun showAlertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val title = "Friendly Reminder"
        val message = "Some Solana endpoints may respond slowly and unreliably. Please purchase a paid node on QuickNode to ensure fast and stable responses; otherwise, you may frequently encounter \"Load failed\" messages."
        val positiveButtonText = "Let me see"
        val negativeButtonText = "Buy later"
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText){ dialog, which ->
                openUrlInBrowser(context, "https://www.quicknode.com/")
            }
            .setNegativeButton(negativeButtonText) { dialog, which ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    // Method to open URL in browser
    private fun openUrlInBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            println("The current phone does not have a browser installed")
        }
    }
}
