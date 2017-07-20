package ar.com.leandrolopez.mediosdepago.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Type;

/**
 * Created by Nico on 19/7/2017.
 */

public class NetworkManager {
    private static NetworkManager mInstance;

    private RequestQueue mVolleyQueue;

    private NetworkManager(Context ctx) {
        mVolleyQueue = Volley.newRequestQueue(ctx);
    }

    public static NetworkManager getInstance(Context ctx) {
        if (mInstance == null)
            mInstance = new NetworkManager(ctx);

        return mInstance;
    }

    public <T> void call(String url, Type gsonType, NetworkCallback<T> listener) {
        GsonRequest request = new GsonRequest<T>(url, gsonType, listener);
        mVolleyQueue.add(request);
    }
}
