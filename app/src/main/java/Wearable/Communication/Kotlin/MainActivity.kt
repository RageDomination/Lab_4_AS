package Wearable.Communication.Kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import Wearable.Communication.Kotlin.ui.theme.MyApplicationTheme
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.PutDataMapRequest

private const val DATA_PATH = "/my_data_path"
private const val DATA_KEY = "my_data_key"

class MainActivity : ComponentActivity() {

    private val MESSAGE_PATH = "/my_message_path"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                WearCommunicationScreen(
                    onSendMessage = ::sendDataToWear
                )
            }
        }
    }

    private fun sendDataToWear(setStatus: (String) -> Unit) {
        val dataToSend = "Повідомлення від DataItem: ${System.currentTimeMillis()}"

        lifecycleScope.launch {
            setStatus("Статус: Надсилання DataItem...")
            try {
                val dataMapRequest = PutDataMapRequest.create(DATA_PATH).apply {
                    dataMap.putString(DATA_KEY, dataToSend)
                }

                val request = dataMapRequest.asPutDataRequest().setUrgent()

                Tasks.await(Wearable.getDataClient(this@MainActivity).putDataItem(request))

                setStatus("Статус: Дані синхронізовано! ($dataToSend)")
            } catch (e: Exception) {
                setStatus("Статус: Помилка синхронізації: ${e.message}")
            }
        }
    }

    private fun sendMessageToWear(setStatus: (String) -> Unit) {
        lifecycleScope.launch {
            setStatus("Статус: Надсилання MessageClient...")
            try {
                val nodes =
                    Tasks.await(Wearable.getNodeClient(this@MainActivity).connectedNodes)

                if (nodes.isEmpty()) {
                    setStatus("Статус: Пристрої Wear не підключено!")
                    return@launch
                }

                nodes.forEach { node: Node ->
                    Tasks.await(
                        Wearable.getMessageClient(this@MainActivity).sendMessage(
                            node.id,
                            MESSAGE_PATH,
                            "Привіт з телефону!".toByteArray()
                        )
                    )
                }
                setStatus("Статус: Повідомлення відправлено MessageClient!")
            } catch (e: Exception) {
                setStatus("Статус: Помилка відправки: ${e.localizedMessage}")
            }
        }
    }
}

@Composable
fun WearCommunicationScreen(
    onSendMessage: (setStatus: (String) -> Unit) -> Unit
) {
    var statusText by remember { mutableStateOf("Статус: Готово до відправки") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = statusText,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = {
                    onSendMessage { newStatus ->
                        statusText = newStatus
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Відправити дані на Wear")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WearCommunicationScreenPreview() {
    MyApplicationTheme {
        WearCommunicationScreen(onSendMessage = {})
    }
}