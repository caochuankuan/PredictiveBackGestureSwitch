package com.compose.predictivebackgestureswitch.ui.components

import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainContent(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var useGesture by remember { mutableStateOf(true) }

    // 首次进入时从 SharedPreferences 加载已保存的手势设置
    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("app_settings", MODE_PRIVATE)
        useGesture = sharedPreferences.getBoolean("use_gesture", true)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 72.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "预测性返回手势",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = if (useGesture) "已启用" else "已禁用",
                    fontSize = 12.sp,
                    color = if (useGesture) Color(0xFF4CAF50) else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Switch(
                checked = useGesture,
                onCheckedChange = { newValue ->
                    useGesture = newValue
                    val sharedPreferences = context.getSharedPreferences("app_settings", MODE_PRIVATE)
                    sharedPreferences.edit(commit = true) { putBoolean("use_gesture", newValue) }
                    Toast.makeText(context, "即将重启以应用设置", Toast.LENGTH_SHORT).show()

                    scope.launch {
                        // 等待 1 秒，便于用户看清提示
                        delay(1000)
                        val restartIntent = Intent(
                            context,
                            com.compose.predictivebackgestureswitch.MainActivity::class.java
                        ).apply {
                            // 使用 CLEAR_TASK + NEW_TASK 确保新设置的目标页成为根页面
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        }
                        context.startActivity(restartIntent)
                        // 结束当前任务栈，避免旧页面残留
                        (context as? Activity)?.finishAffinity()
                    }
                }
            )
        }

        // 导航示例按钮：点击跳转到空白页（BlankScreen）
        androidx.compose.material3.Button(onClick = { navController.navigate("blank") }) {
            Text(text = "Navigate to Blank Screen")
        }
    }
}
