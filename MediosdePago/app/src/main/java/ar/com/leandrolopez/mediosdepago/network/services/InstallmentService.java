package ar.com.leandrolopez.mediosdepago.network.services;

import android.app.Service;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ar.com.leandrolopez.mediosdepago.BuildConfig;
import ar.com.leandrolopez.mediosdepago.R;
import ar.com.leandrolopez.mediosdepago.network.NetworkCallback;
import ar.com.leandrolopez.mediosdepago.network.NetworkManager;
import ar.com.leandrolopez.mediosdepago.network.model.InstallmentsModel;

/**
 * Created by Nico on 20/7/2017.
 */

public class InstallmentService {

    public static void getInstallments(Context ctx, NetworkCallback<List<InstallmentsModel>> listener, float amount, String paymentMethodId, String issuerId) {

        String url = ServiceUtils.getMPApiBaseUrl() + ctx.getString(R.string.url_installments, ServiceUtils.getMPApiKey(), String.valueOf(amount), paymentMethodId, issuerId);

        final Type listType = new TypeToken<List<InstallmentsModel>>() {
        }.getType();

        NetworkManager.getInstance(ctx)
                .call(url, listType, listener);
    }

}
