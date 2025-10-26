package com.compose.predictivebackgestureswitch

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.compose.predictivebackgestureswitch.activity.MainActivityNoGesture
import com.compose.predictivebackgestureswitch.activity.MainActivityUseGesture

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 根据 SharedPreferences 中的 use_gesture 决定跳转到不同 Activity：
        val sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE)
        val useGesture = sharedPreferences.getBoolean("use_gesture", true) // 默认启用手势

        val targetActivity = if (useGesture) {
            MainActivityUseGesture::class.java
        } else {
            MainActivityNoGesture::class.java
        }

        // 保留原始 Intent 的数据，确保外部唤起行为不丢失
        val targetIntent = Intent(this, targetActivity).apply {
            // 传递原始 Intent 的数据
            action = intent.action
            data = intent.data
            intent.extras?.let { putExtras(it) }
        }

        startActivity(targetIntent)
        finish() // 结束当前 Activity
    }
}