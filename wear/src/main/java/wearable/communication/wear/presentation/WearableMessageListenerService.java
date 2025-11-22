package wearable.communication.wear.presentation;

import android.content.Intent;
import android.util.Log;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import androidx.annotation.NonNull;

public class WearableMessageListenerService extends WearableListenerService {

    // Константи для шляху та дії Broadcast
    private static final String MESSAGE_PATH = "/my_message_path";
    public static final String ACTION_MESSAGE_RECEIVED = "MESSAGE_RECEIVED";
    public static final String EXTRA_MESSAGE = "message";

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {

        // Перевіряємо, чи шлях повідомлення збігається з очікуваним
        if (messageEvent.getPath().equals(MESSAGE_PATH)) {

            String message = new String(messageEvent.getData());
            Log.d("WearableMsg", "Отримано повідомлення: " + message);
            Intent intent = new Intent(ACTION_MESSAGE_RECEIVED);
            intent.putExtra(EXTRA_MESSAGE, message);

            sendBroadcast(intent);
        }
    }
}