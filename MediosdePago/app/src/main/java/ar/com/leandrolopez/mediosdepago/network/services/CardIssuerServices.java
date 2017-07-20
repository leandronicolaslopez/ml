package ar.com.leandrolopez.mediosdepago.network.services;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.CardIssuer;

/**
 * Created by Nico on 19/7/2017.
 */

public class CardIssuerServices {

    public static void getCardIssuers(Context ctx, NetworkCallback<List<CardIssuer>> listener, String paymentMethodId) {
        String base = "https://api.mercadopago.com/v1/payment_methods/card_issuers?public_key=444a9ef5-8a6b-429f-abdf-587639155d88";

        String url = base + "&payment_method_id=" + paymentMethodId;
        final Type listType = new TypeToken<List<CardIssuer>>() {
        }.getType();


        NetworkManager.getInstance(ctx)
                .call(url, listType, listener);
    }
}
