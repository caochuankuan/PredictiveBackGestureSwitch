# 预测性返回手势开关 / Predictive Back Gesture Switch

一个使用 Jetpack Compose 的最小示例，通过 Manifest 配置与应用内按钮切换启用/停用预测性返回手势，并通过重启立即生效。

## 原理
- 官方文档说明：如需选择停用预测性返回手势，可在 `<application>` 或 `<activity>` 中将 `android:enableOnBackInvokedCallback="false"` 设置为停用。本项目采用按 Activity 配置的做法。[Android 开发者文档][0]
- 定义两个内容一致的 Activity：
  - `MainActivityUseGesture`：`android:enableOnBackInvokedCallback="true"`
  - `MainActivityNoGesture`：`android:enableOnBackInvokedCallback="false"`
- 启动时，`MainActivity` 读取 `SharedPreferences`（键：`use_gesture`），根据值路由到对应 Activity。
- 页面提供一个按钮切换 `use_gesture`，同步保存（`commit=true`）、展示提示、协程中延时 1 秒，然后使用 `FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK` 重启应用，使设置立即生效。

### Manifest 片段
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

## 流程
- 入口：`MainActivity` 读取 `use_gesture` 并决定目标 Activity。
- 切换按钮：翻转 `use_gesture` -> 同步保存 -> Toast 提示 -> 延时 1 秒 -> 清栈重启至 `MainActivity`。
- 重启后进入对应 Activity，承载相同的导航（`AppNav`）与 UI。

## 组件
- `MainActivity`：入口，根据 `use_gesture` 路由到启用/停用预测性返回的 Activity。
- `MainActivityUseGesture` / `MainActivityNoGesture`：UI 一致，仅在 `enableOnBackInvokedCallback` 不同。
- `MainContent`：展示当前状态与切换按钮，并包含跳转空白页的示例按钮。
- `AppNav` + `BlankScreen`：简单的 Compose 导航。

## 环境要求
- minSdk: 24, targetSdk: 36, compileSdk: 36
- Java/Kotlin: Java 11, Kotlin JVM target 11
- Android Studio（推荐 Hedgehog+）

## 运行
- 在 Android Studio 中打开项目并运行 `app`。

```bash
./gradlew assembleDebug
```

- 从 `app/build/outputs/apk/debug/app-debug.apk` 安装。

## 说明
- 1 秒延时可提升体验，让提示在重启前被用户看到。
- 方案遵循官方建议，在 Activity 级别通过 `enableOnBackInvokedCallback` 控制行为，UI 保持一致。

## 参考
- [Android 官方文档：添加对预测性返回手势的支持][0]

---

# Predictive Back Gesture Switch (English Version)

A minimal Jetpack Compose app to demonstrate enabling/disabling Android’s predictive back gesture via manifest configuration and an in-app toggle with immediate restart.

## Principle
- Official docs: to opt out of predictive back, set `android:enableOnBackInvokedCallback="false"` on `<application>` or at `<activity>` level. This project uses the activity-level approach. [Android Developers][0]
- Two activities with identical UI:
  - `MainActivityUseGesture`: `android:enableOnBackInvokedCallback="true"`
  - `MainActivityNoGesture`: `android:enableOnBackInvokedCallback="false"`
- On launch, `MainActivity` reads `SharedPreferences` (`use_gesture`) and routes to the corresponding activity.
- The UI provides a toggle button: it saves synchronously (`commit=true`), shows a toast, waits 1 second, and restarts with `FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK` to apply changes immediately.

### Manifest snippet
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

## Flow
- Entry: `MainActivity` decides target based on `use_gesture`.
- Toggle button: flip `use_gesture` -> persist synchronously -> toast -> delay 1s -> restart to `MainActivity` with a cleared task.
- After restart, the chosen activity hosts the same navigation (`AppNav`) and UI.

## Components
- `MainActivity`: entry router reading `use_gesture`.
- `MainActivityUseGesture` / `MainActivityNoGesture`: identical UI, differ only by `enableOnBackInvokedCallback`.
- `MainContent`: shows state and provides the toggle; includes a demo button to navigate to a blank screen.
- `AppNav` + `BlankScreen`: simple Compose navigation.

## Requirements
- minSdk: 24, targetSdk: 36, compileSdk: 36
- Java/Kotlin: Java 11, Kotlin JVM target 11
- Android Studio (Hedgehog+ recommended)

## Run
- Open the project in Android Studio and run `app`.

```bash
./gradlew assembleDebug
```

- Install from `app/build/outputs/apk/debug/app-debug.apk`.

## Notes
- The 1-second delay improves UX so the toast remains visible before restart.
- This approach mirrors the guidance to opt-out at the activity level via `enableOnBackInvokedCallback`, keeping UI identical while behavior changes.

## Reference
- [Android Developers: Add support for predictive back gesture][0]

[0]: https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture?hl=zh-cn