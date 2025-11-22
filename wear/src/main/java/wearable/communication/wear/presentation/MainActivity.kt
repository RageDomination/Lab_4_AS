package wearable.communication.wear.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import wearable.communication.wear.R
import wearable.communication.wear.presentation.theme.Lab_1Theme

private const val ACTION_MESSAGE_RECEIVED = "MESSAGE_RECEIVED"
private const val EXTRA_MESSAGE = "message"
private const val ACTION_DATA_RECEIVED = "DATA_RECEIVED"
private const val EXTRA_DATA_ITEM = "data_item"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp(initialMessage = getString(R.string.awaiting_message))
        }
    }
}

@Composable
fun WearApp(initialMessage: String) {
    Lab_1Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            MessageDisplay(initialMessage = initialMessage)
        }
    }
}

@Composable
fun MessageDisplay(initialMessage: String) {
    val context = LocalContext.current

    var currentMessage by remember { mutableStateOf(initialMessage) }

    DisposableEffect(context) {
        val messageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    ACTION_MESSAGE_RECEIVED -> {
                        // Обробка MessageClient
                        val message = intent.getStringExtra(EXTRA_MESSAGE)
                        currentMessage = message ?: initialMessage
                    }
                    ACTION_DATA_RECEIVED -> {
                        // Обробка DataClient (DataItem)
                        val dataItem = intent.getStringExtra(EXTRA_DATA_ITEM)
                        currentMessage = dataItem ?: initialMessage
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(ACTION_MESSAGE_RECEIVED)
            addAction(ACTION_DATA_RECEIVED)
        }

        onDispose {
            context.unregisterReceiver(messageReceiver)
        }
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = currentMessage
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    Lab_1Theme {
        WearApp(initialMessage = "Очікування повідомлення...")
    }
}