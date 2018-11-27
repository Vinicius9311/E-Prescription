package eprescription.tcc.ufam.com.e_prescription.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Test extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context,intent.getStringExtra("teste"),Toast.LENGTH_SHORT).show();
    }
}
