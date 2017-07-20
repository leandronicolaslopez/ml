package ar.com.leandrolopez.mediosdepago.network;

import com.android.volley.VolleyError;

/**
 * Created by Nico on 19/7/2017.
 */

public class NetworkError {

    public NetworkError(VolleyError volleyError){
        if(volleyError != null) {
            setErrorMessage(volleyError.getMessage());
        }
    }

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
