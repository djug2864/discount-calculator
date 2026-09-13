// パッケージ宣言（アプリの識別名）
package com.example.discount

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// アプリ情報画面のアクティビティ（画面本体）
class AppInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // アプリ全体のテーマ（見た目）を適用
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 情報表示画面のレイアウトを呼び出し（戻るボタンで画面を閉じる）
                    AppInfoScreen(onBackClick = { finish() })
                }
            }
        }
    }
}

// 画面の見た目をつくる処理
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen(onBackClick: () -> Unit) {
    Scaffold(
        // 上部のタイトルバー設定
        topBar = {
            TopAppBar(
                title = { Text("アプリ情報") },
                navigationIcon = {
                    // 左上の「戻るボタン」
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { paddingValues ->
        // アプリ情報を並べて表示するレイアウト
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // アプリ名
            Text(
                text = "5割引計算機",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // バージョン情報
            Text(
                text = "バージョン: 1.0.0",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 説明テキスト
            Text(
                text = "このアプリは、キーパッドから入力した金額の「5割引」と「端数処理」を簡単に計算できるアプリです。",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
