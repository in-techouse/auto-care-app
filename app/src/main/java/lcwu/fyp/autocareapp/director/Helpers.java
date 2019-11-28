package lcwu.fyp.autocareapp.director;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shreyaspatil.MaterialDialog.MaterialDialog;

import lcwu.fyp.autocareapp.LoginActivity;
import lcwu.fyp.autocareapp.R;

public class Helpers {

    public boolean isConnected(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            connected = true;
        else
            connected = false;
        return  connected;
    }

    public void showNoInternetError(Activity activity){
        final MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .setTitle("ERROR!")
                .setMessage("No internet connection found.\nConnect to a network and try again.")
                .setCancelable(false)
                .setPositiveButton("Okay", R.drawable.ic_okay, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Close", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        // Show Dialog
        dialog.show();

    }
}
