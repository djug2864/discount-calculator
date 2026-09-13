# 5割引計算機 (DiscountCalculator)

キーパッド入力で簡単に5割引（半額）後の価格を計算できるネイティブAndroidアプリです。  
端数処理（切り上げ・切り捨て）の設定変更や、ダークモードに対応しています。

---

## 📱 主な機能

* **カスタムキーパッド入力:** 大きく押しやすいテンキー（`0〜9`、`C（クリア）`、`5割引`）でスピーディに金額を入力可能
* **5割引計算:** 入力された金額の半額（50%）をワンタップで計算
* **端数処理機能:**
  * 端数処理の有効化 / 無効化の切替
  * 切り上げ（`ceil`）/ 切り捨て（`floor`）の選択
* **モーダルダイアログ表示:** 計算結果を画面中央のポップアップで分かりやすく表示
* **ダークモード対応:** 設定画面からライト/ダークテーマの切替が可能
* **アプリ情報画面:** アプリ名やバージョン情報、概要を確認できる専用画面を搭載

---

## 🛠 テクノロジー / ライブラリ

* **言語:** Kotlin (1.9.22)
* **UIフレームワーク:** Jetpack Compose / Material Design 3
* **Target SDK:** 34 (Android 14)
* **Min SDK:** 24 (Android 7.0 以上)
* **CI/CD:** GitHub Actions（手動ビルド ＆ Releaseへの自動デプロイ）

---

## 📂 プロジェクト構成

```text
.
├── .github/
│   └── workflows/
│       └── build-apk.yml        # APK作成 & Release自動公開用のGitHub Actions設定
├── app/
│   ├── build.gradle             # アプリレベルのビルド設定
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           └── java/com/example/discount/
│               ├── MainActivity.kt      # メイン画面 (キーパッド・計算・設定)
│               └── AppInfoActivity.kt   # アプリ情報画面
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── build.gradle                 # ルートビルド設定
├── gradlew                      # Gradle実行スクリプト
├── README.md
└── settings.gradle              # プロジェクト設定
```

---

## 🚀 GitHub Actions による APK の作成方法

本プロジェクトは GitHub Actions を利用して、クラウド上で自動的に `.apk` ファイルを組み立て、GitHub の Release ページに公開するように設定されています。

1. GitHub リポジトリの **「Actions」** タブを開きます。
2. 左メニューから **「Build and Release APK」** を選択します。
3. **「Run workflow」** ボタンをタップします。
4. バージョンタグ（例: `v1.0.0`）を入力し、緑色の **「Run workflow」** を押して実行します。
5. ビルド完了後、リポジトリの **「Releases」** ページから作成された APK ファイルをダウンロードできます。

---

## 📜 ライセンス

This project is open-source and available for personal use and modification.
