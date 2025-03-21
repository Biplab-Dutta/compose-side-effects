package com.zoroxnekko.compose.sideeffects.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zoroxnekko.compose.sideeffects.CollectWithLifecycle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = viewModel<DemoViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle()
            val context = LocalContext.current

            viewModel.effects.CollectWithLifecycle { effect ->
                when (effect) {
                    is DemoSideEffects.ShowToast -> {
                        Toast.makeText(
                            context,
                            effect.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { viewModel.incrementCount() }
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Increment Icon")
                    }
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.value.count.toString(),
                        style = TextStyle(fontSize = 50.sp)
                    )
                }
            }

        }
    }
}
