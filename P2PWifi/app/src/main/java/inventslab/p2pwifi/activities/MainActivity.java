package inventslab.p2pwifi.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import inventslab.p2pwifi.R;

public class MainActivity extends AppCompatActivity {

    private final IntentFilter intentFilter = new IntentFilter();

    WifiP2pManager mManager ;
    BroadcastReceiver mReceiver;
    WifiP2pManager.Channel mChannel;
    static TextView text;
    private boolean isWifiP2pEnabled = false;
    static WifiP2pDevice device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        device = new WifiP2pDevice();

        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new Reciever(mManager, mChannel, this);


        Log.d("Mainact", "Hello");
        Toast.makeText(this,"Hello", Toast.LENGTH_SHORT).show();


        ((Button) findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("DiscoverPeers", "onSuccess: Success");
                        Toast.makeText(getApplicationContext(),"DiscoverPeeers Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Log.d("DiscoverPeers", "onFailure: failed");
                        Toast.makeText(getApplicationContext(),"DiscoverPeeers Failure", Toast.LENGTH_SHORT).show();

                    }
                });

                //WifiP2pDevice device;
                WifiP2pConfig config = new WifiP2pConfig();
                if(!MainActivity.device.deviceAddress.isEmpty() && !MainActivity.device.deviceAddress.equals(null)) {
                    config.deviceAddress = MainActivity.device.deviceAddress;
                    mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                        @Override
                        public void onSuccess() {
                            //success logic
                            Log.d("ConnectPeers", "Success: connected");
                            Toast.makeText(getApplicationContext(), "Success: connected", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int reason) {
                            //failure logic
                            Log.d("ConnectPeers", "onFailure: failed");
                            Toast.makeText(getApplicationContext(), "DiscoverPeeers Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });




    }
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
        Log.d("DiscoverPeers", "setIsWifiP2pEnabled: " + isWifiP2pEnabled);
        Toast.makeText(getApplicationContext(),"WifiP2P enables" + isWifiP2pEnabled, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver,intentFilter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
