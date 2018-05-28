package ndk.utils.network_task;

import android.support.v4.util.Pair;

public class REST_GET_Task {
    public static String get_Get_URL(String URL, Pair[] keys) {
        if (keys.length != 0) {
            StringBuilder URLBuilder = new StringBuilder(URL);
            for (int i = 0; i < keys.length; i++) {
                Pair key = keys[i];
                if (i == 0) {
                    URLBuilder.append("?").append(key.first).append("=").append(key.second);
                } else {
                    URLBuilder.append("&").append(key.first).append("=").append(key.second);
                }
            }
            URL = URLBuilder.toString();
        }
        return URL;
    }
}
