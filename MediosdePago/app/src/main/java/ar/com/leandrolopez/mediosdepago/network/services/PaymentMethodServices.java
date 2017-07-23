package ar.com.leandrolopez.mediosdepago.network.services;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ar.com.leandrolopez.mediosdepago.BuildConfig;
import ar.com.leandrolopez.mediosdepago.R;
import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.PaymentMethod;

/**
 * Created by Nico on 19/7/2017.
 */

public class PaymentMethodServices {

    public static void getPaymentMethods(Context ctx, NetworkCallback<List<PaymentMethod>> listener) {
        final Type listType = new TypeToken<List<PaymentMethod>>() {
        }.getType();

        String url = ServiceUtils.getMPApiBaseUrl() + ctx.getString(R.string.url_payment_methods, ServiceUtils.getMPApiKey());

        NetworkManager.getInstance(ctx)
                .call(url, listType, listener);
    }
}