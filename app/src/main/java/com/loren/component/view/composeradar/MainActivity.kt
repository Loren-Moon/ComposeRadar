package com.loren.component.view.composeradar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {

    private val list = listOf(
        RadarBean("基本财务", 43f),
        RadarBean("基本财务财务", 90f),
        RadarBean("基", 90f),
        RadarBean("基本财务财务", 90f),
        RadarBean("基本财务", 83f),
        RadarBean("技术择时择时", 50f),
        RadarBean("景气行业行业", 83f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Row(modifier = Modifier.padding(top = 16.dp).horizontalScroll(rememberScrollState())) {
                ComposeRadarView(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(180.dp),
                    list.take(3)
                )
                ComposeRadarView(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(180.dp),
                    list.take(4)
                )
                ComposeRadarView(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(180.dp),
                    list.take(5),
                    specialHandle = true
                )
                ComposeRadarView(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(180.dp),
                    list.take(6)
                )
                ComposeRadarView(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(180.dp),
                    list
                )
            }
        }
    }
}