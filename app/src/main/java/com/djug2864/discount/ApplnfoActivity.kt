package com.djug2864.discount

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AppInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)

        // 戻るボタンの処理
        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }
    }
}
