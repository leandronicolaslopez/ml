package ar.com.leandrolopez.mediosdepago.network.services;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.InstallmentsModel;

/**
 * Created by Nico on 20/7/2017.
 */

public class InstallmentService {

    public static void getInstallments(Context ctx, NetworkCallback<List<InstallmentsModel>> listener, float amount, String paymentMethodId, String issuerId) {
        String base = "https://api.mercadopago.com/v1/payment_methods/installments?public_key=444a9ef5-8a6b-429f-abdf-587639155d88";

        String url = base + "&amount=" + String.valueOf(amount) + "&payment_method_id=" + paymentMethodId + "&issuer.id=" + issuerId;
        final Type listType = new TypeToken<List<InstallmentsModel>>() {
        }.getType();

        NetworkManager.getInstance(ctx)
                .call(url, listType, listener);
    }

}
