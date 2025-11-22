package Wearable.Communication.Kotlin

import android.content.Intent
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import android.util.Log

// Константа, що відповідає DATA_PATH на телефоні
private const val DATA_PATH = "/my_data_path"
private const val DATA_KEY = "my_data_key"

class WearableMessageListenerService : WearableListenerService() {

    // 1. Обробка повідомлень
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/my_message_path") {
            val message = String(messageEvent.data)
            Log.d("WearableService", "Отримано Message: $message")

            val intent = Intent("MESSAGE_RECEIVED").apply {
                putExtra("message", "MessageClient: $message")
            }
            sendBroadcast(intent)
        }
    }

    // 2. Обробка змін даних (DataItems)
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem


                if (dataItem.uri.path == DATA_PATH) {
                    val dataMapItem = DataMapItem.fromDataItem(dataItem)
                    val dataString = dataMapItem.dataMap.getString(DATA_KEY)
                    Log.d("WearableService", "Отримано DataItem: $dataString")
                    val intent = Intent("DATA_RECEIVED").apply {
                        putExtra("data_item", "DataItem: $dataString")
                    }
                    sendBroadcast(intent)
                }
            }
        }
    }
}