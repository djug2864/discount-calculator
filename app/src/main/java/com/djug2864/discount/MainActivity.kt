package com.djug2864.discount

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.ceil
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private lateinit var priceDisplay: TextView

    private var inputPrice = ""

    private var isDarkMode = false
    private var isRoundingEnabled = true
    private var isCeil = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        priceDisplay = findViewById(R.id.priceDisplay)

        setupKeypad()

        findViewById<Button>(R.id.settingsButton).setOnClickListener {
            showSettingsDialog()
        }

        updateDisplay()
    }

    private fun setupKeypad() {

        val buttons = mapOf(
            R.id.button7 to "7",
            R.id.button8 to "8",
            R.id.button9 to "9",
            R.id.button4 to "4",
            R.id.button5 to "5",
            R.id.button6 to "6",
            R.id.button1 to "1",
            R.id.button2 to "2",
            R.id.button3 to "3",
            R.id.button0 to "0"
        )

        buttons.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener {
                if (inputPrice.length < 8) {
                    inputPrice += value
                    updateDisplay()
                }
            }
        }

        // クリア
        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            inputPrice = ""
            updateDisplay()
        }

        // 5割引
        findViewById<Button>(R.id.buttonDiscount).setOnClickListener {
            calculateDiscount()
        }
    }

    private fun updateDisplay() {

        priceDisplay.text =
            if (inputPrice.isEmpty()) {
                "0 円"
            } else {
                "$inputPrice 円"
            }
    }

    private fun calculateDiscount() {

        val price = inputPrice.toDoubleOrNull()

        if (price == null) {
            return
        }

        val rawDiscounted = price * 0.5

        val finalPrice = if (isRoundingEnabled) {

            if (isCeil) {
                ceil(rawDiscounted).toInt()
            } else {
                floor(rawDiscounted).toInt()
            }

        } else {
            rawDiscounted.toInt()
        }

        showResultDialog(finalPrice)
    }

    private fun showResultDialog(price: Int) {

        AlertDialog.Builder(this)
            .setTitle("割引後の値段")
            .setMessage("$price 円")
            .setPositiveButton("閉じる", null)
            .show()
    }

    private fun showSettingsDialog() {

        val options = arrayOf(
            "ダークモード",
            "端数処理を有効化",
            "切り上げモード",
            "アプリ情報を見る"
        )

        AlertDialog.Builder(this)
            .setTitle("設定")
            .setItems(options) { _, which ->

                when (which) {

                    0 -> {
                        isDarkMode = !isDarkMode
                        showSettingsDialog()
                    }

                    1 -> {
                        isRoundingEnabled = !isRoundingEnabled
                        showSettingsDialog()
                    }

                    2 -> {
                        if (isRoundingEnabled) {
                            isCeil = !isCeil
                        }
                        showSettingsDialog()
                    }

                    3 -> {
                        val intent =
                            Intent(this, AppInfoActivity::class.java)

                        startActivity(intent)
                    }
                }
            }
            .setNegativeButton("閉じる", null)
            .show()
    }
}
