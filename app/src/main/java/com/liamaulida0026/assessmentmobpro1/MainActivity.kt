package com.liamaulida0026.assessmentmobpro1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.liamaulida0026.assessmentmobpro1.navigation.SetUpNavGraph
import com.liamaulida0026.assessmentmobpro1.ui.screen.MainScreen
import com.liamaulida0026.assessmentmobpro1.ui.theme.AssessmentMobpro1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssessmentMobpro1Theme {
                SetUpNavGraph()
            }
        }
    }
}
