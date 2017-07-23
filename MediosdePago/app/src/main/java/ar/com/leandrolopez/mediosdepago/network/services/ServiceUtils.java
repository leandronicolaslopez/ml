package ar.com.leandrolopez.mediosdepago.network.services;

import ar.com.leandrolopez.mediosdepago.BuildConfig;

/**
 * Created by Nico on 22/7/2017.
 */

public class ServiceUtils {
    public static String getMPApiKey() {
        return BuildConfig.MP_API_KEY;
    }

    public static String getMPApiBaseUrl() {
        return BuildConfig.MP_BASE_URL;
    }
}
