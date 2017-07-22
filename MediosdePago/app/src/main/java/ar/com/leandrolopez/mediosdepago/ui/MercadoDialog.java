package ar.com.leandrolopez.mediosdepago.ui;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Nico on 22/7/2017.
 */

public class MercadoDialog {

    public static ProgressDialog newProgress(Context ctx) {
        final ProgressDialog pd = new ProgressDialog(ctx);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        return pd;
    }

    public static void showAlert() {

    }
}
