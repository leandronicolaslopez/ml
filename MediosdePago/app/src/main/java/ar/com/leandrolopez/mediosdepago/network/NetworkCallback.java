package ar.com.leandrolopez.mediosdepago.network;

import com.android.volley.VolleyError;

/**
 * Created by Nico on 19/7/2017.
 */

public interface NetworkCallback<T> {
    void onSuccess(T response);
    void onError(NetworkError error);
}
