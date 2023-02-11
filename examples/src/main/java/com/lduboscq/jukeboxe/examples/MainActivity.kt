package com.lduboscq.jukeboxe.examples

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var example by remember { mutableStateOf(2) }

            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    Button(onClick = { example = 1 }) {
                        Text("One after the other")
                    }
                    Button(onClick = { example = 2 }) {
                        Text("List")
                    }
                }

                if (example == 1) {
                    OneAfterTheOtherExample()
                } else if (example == 2) {
                    ListExample()
                }
            }
        }
    }
}
