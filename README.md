## ComposeRadar
自定义的雷达图来展示各个属性的不同比例，文字根据控件大小自动换行。

## 如何使用
[详细介绍](https://juejin.cn/post/7083055968703119373)

```kotlin
private val list = listOf(
    RadarBean("基本财务", 43f),
    RadarBean("基本财务财务", 90f),
    RadarBean("基", 90f),
    RadarBean("基本财务财务", 90f),
    RadarBean("基本财务", 83f),
    RadarBean("技术择时择时", 50f),
    RadarBean("景气行业行业", 83f)
)
ComposeRadarView(
    modifier = Modifier
        .padding(horizontal = 4.dp)
        .size(180.dp),
    list
)
```

## :camera_flash: Screenshots

<img src="/snapshot/screen.gif" width="260">
