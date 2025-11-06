package edu.ucne.jendri_hidalgo_p2_ap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.jendri_hidalgo_p2_ap2.presentation.GastoScreen
import edu.ucne.jendri_hidalgo_p2_ap2.ui.theme.Jendri_Hidalgo_P2_AP2Theme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Jendri_Hidalgo_P2_AP2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    GastoScreen(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}
