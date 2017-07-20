package ar.com.leandrolopez.mediosdepago.network;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

/**
 * Created by Nico on 19/7/2017.
 */

public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private Type mGsonType;
    private final NetworkCallback<T> mListener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     */
    public GsonRequest(String url, Type gsonType, final NetworkCallback<T> listener) {
        super(Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    NetworkError networkError = new NetworkError(error);
                    listener.onError(networkError);
                }
            }
        });
        this.mGsonType = gsonType;
        this.mListener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onSuccess(response);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            T responseObj = gson.fromJson(json, mGsonType);

            return Response.success(
                    responseObj,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}