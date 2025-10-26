# Predictive Back Gesture Switch / 预测性返回手势开关

A minimal Jetpack Compose app that demonstrates enabling/disabling Android’s predictive back gesture via manifest configuration and an in-app toggle with immediate restart.  
一个使用 Jetpack Compose 的最小示例，通过 Manifest 配置与应用内按钮切换启用/停用预测性返回手势，并通过重启立即生效。

## Principle / 原理
- Official docs state: to opt out of predictive back, set `android:enableOnBackInvokedCallback="false"` on the `<application>` or at the `<activity>` level. We use the activity-level approach. [Android Developers][0]  
  官方文档说明：如需选择停用预测性返回手势，可在 `<application>` 或 `<activity>` 中将 `android:enableOnBackInvokedCallback="false"` 设置为停用。本项目采用按 Activity 配置的做法。[Android 开发者文档][0]
- We define two activities with identical UI:
  - `MainActivityUseGesture` with `android:enableOnBackInvokedCallback="true"`.
  - `MainActivityNoGesture` with `android:enableOnBackInvokedCallback="false"`.  
  我们定义两个内容一致的 Activity：
  - `MainActivityUseGesture`：`android:enableOnBackInvokedCallback="true"`
  - `MainActivityNoGesture`：`android:enableOnBackInvokedCallback="false"`
- On launch, `MainActivity` reads `SharedPreferences` (`use_gesture`) and routes to the corresponding activity.  
  启动时，`MainActivity` 读取 `SharedPreferences`（`use_gesture`），跳转到对应的 Activity。
- The UI provides a button that toggles `use_gesture`, saves synchronously, shows a toast, waits 1 second, and restarts the app to apply changes.  
  页面提供一个按钮切换 `use_gesture`，同步保存、提示，等待 1 秒后重启应用使设置立即生效。

### Manifest snippet / Manifest 片段
```xml
<!-- app/src/main/AndroidManifest.xml -->
<activity
    android:name=".activity.MainActivityNoGesture"
    android:exported="false"
    android:enableOnBackInvokedCallback="false"
    tools:targetApi="33" />
<activity
    android:name=".activity.MainActivityUseGesture"
    android:exported="false"
    android:enableOnBackInvokedCallback="true"
    tools:targetApi="33" />
```

## Flow / 流程
- Entry: `MainActivity` decides target based on `use_gesture`.  
  入口：`MainActivity` 基于 `use_gesture` 决定目标 Activity。
- UI toggle button: flips `use_gesture`, persists with `commit=true`, shows toast, waits 1s, restarts with `FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK`.  
  UI 切换按钮：翻转 `use_gesture`，`commit=true` 同步持久化，提示，延时 1 秒，使用 `FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK` 重启。
- After restart, the chosen activity hosts the same navigation (`AppNav`) and UI.  
  重启后进入对应 Activity，承载相同的导航（`AppNav`）与 UI。

## Components / 组件
- `MainActivity`: entry router reading `use_gesture`.  
  `MainActivity`：入口，根据 `use_gesture` 路由。
- `MainActivityUseGesture` / `MainActivityNoGesture`: identical UI, differ only by `enableOnBackInvokedCallback`.  
  两个 Activity：UI 一致，仅在 `enableOnBackInvokedCallback` 上不同。
- `MainContent`: shows state and provides the toggle button; also includes a demo button to navigate to a blank screen.  
  `MainContent`：显示当前状态并提供切换按钮；同时包含跳转到空白页的演示按钮。
- `AppNav` + `BlankScreen`: simple Compose navigation.  
  `AppNav` + `BlankScreen`：简单的 Compose 导航。

## Requirements / 环境要求
- minSdk: 24, targetSdk: 36, compileSdk: 36  
- Java/Kotlin: Java 11, Kotlin JVM target 11  
- Android Studio（推荐 Hedgehog+）

## Run / 运行
- Open in Android Studio and run `app`.  
  在 Android Studio 中打开项目并运行 `app`。

```bash
./gradlew assembleDebug
```

- Install from `app/build/outputs/apk/debug/app-debug.apk`.  
  从 `app/build/outputs/apk/debug/app-debug.apk` 安装。

## Notes / 说明
- The 1-second delay improves UX so the toast is visible before restart.  
  1 秒延时可提升体验，让提示不会被立即打断。
- The approach mirrors the guidance to opt-out by setting `enableOnBackInvokedCallback` at the activity level, keeping UI identical while behavior changes.  
  此方案遵循官方建议，在 Activity 级别通过 `enableOnBackInvokedCallback` 控制行为，UI 保持一致。

[0]: https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture?hl=zh-cn