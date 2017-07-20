package ar.com.leandrolopez.mediosdepago.network.services;

import android.content.Context;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ar.com.leandrolopez.mediosdepago.BuildConfig;
import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;

/**
 * Created by Nico on 19/7/2017.
 */

public class PaymentMethodServices {

    //TODO refactor base url y armado de parámetros
    private final static String URL_GET_PAYMENT_METHODS = "https://api.mercadopago.com/v1/payment_methods?public_key=";

    public static void getPaymentMethods(Context ctx, NetworkCallback<List<PaymentMethod>> listener) {
        final Type listType = new TypeToken<List<PaymentMethod>>() {
        }.getType();

        String url = URL_GET_PAYMENT_METHODS + BuildConfig.MP_API_KEY;

        NetworkManager.getInstance(ctx)
                .call(url, listType, listener);
    }
}