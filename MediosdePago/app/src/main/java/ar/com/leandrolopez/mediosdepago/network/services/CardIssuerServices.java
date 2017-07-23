package ar.com.leandrolopez.mediosdepago.network.services;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ar.com.leandrolopez.mediosdepago.R;
import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.CardIssuer;

/**
 * Created by Nico on 19/7/2017.
 */

public class CardIssuerServices {

    public static void getCardIssuers(Context ctx, NetworkCallback<List<CardIssuer>> listener, String paymentMethodId) {

        String url = ServiceUtils.getMPApiBaseUrl() + ctx.getString(R.string.url_card_issuers, ServiceUtils.getMPApiKey(), paymentMethodId);

        final Type listType = new TypeToken<List<CardIssuer>>() {
        }.getType();

        NetworkManager.getInstance(ctx)
                .call(url, listType, listener);
    }
}