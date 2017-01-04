package com.sendbird.android.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import android.os.Vibrator;
/**
 * SendBird Android Sample UI
 */
public class MainActivity extends FragmentActivity {
    public static String VERSION = "3.0.7.0";

    private enum State {DISCONNECTED, CONNECTING, CONNECTED}

    /**
     * To test push notifications with your own appId, you should replace google-services.json with yours.
     * Also you need to set Server API Token and Sender ID in SendBird dashboard.
     * Please carefully read "Push notifications" section in SendBird Android documentation
     */
    private static final String appId = "65A822BC-2764-4395-BBBC-F1CBBBBF0662"; /* Sample SendBird Application */
    public static String sUserId;
    private String mNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sUserId = getPreferences(Context.MODE_PRIVATE).getString("user_id", "");
        mNickname = getPreferences(Context.MODE_PRIVATE).getString("nickname", "");
        findViewById(R.id.btn_group_channel_list).setEnabled(false);
        SendBird.init(appId, this);

        ((EditText) findViewById(R.id.etxt_user_id)).setText(sUserId);
        ((EditText) findViewById(R.id.etxt_user_id)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sUserId = s.toString();
            }
        });

        ((EditText) findViewById(R.id.etxt_nickname)).setText(mNickname);
        ((EditText) findViewById(R.id.etxt_nickname)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mNickname = s.toString();
            }
        });

        final Button sndBtn = (Button) findViewById(R.id.btn_connect);
        findViewById(R.id.btn_connect).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button btn = (Button) v;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sndBtn.setBackgroundResource(R.drawable.oressed);
                        return false;
                    case MotionEvent.ACTION_UP:
                        sndBtn.setBackgroundResource(R.drawable.notpressed);
                        return false;
                }
                return false;
            }
        });

        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                if (btn.getText().equals("Connect")) {
                    connect();
                } else {
                    disconnect();
                }

                Helper.hideKeyboard(MainActivity.this);
            }
        });
////////////////////////////////////
        final Button tmpBtn = (Button) findViewById(R.id.btn_group_channel_list);
        findViewById(R.id.btn_group_channel_list).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button btn = (Button) v;
                if(btn.isEnabled()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            tmpBtn.setBackgroundResource(R.drawable.oressed);
                            return false;
                        case MotionEvent.ACTION_UP:
                            tmpBtn.setBackgroundResource(R.drawable.notpressed);
                            return false;
                    }
                }
                return false;
            }
        });

        findViewById(R.id.btn_group_channel_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                if (btn.isEnabled()) {
                    Intent intent = new Intent(MainActivity.this, SendBirdGroupChannelListActivity.class);
                    startActivity(intent);
                }

                Helper.hideKeyboard(MainActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SendBird.disconnect(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * If the minimum SDK version you support is under Android 4.0,
         * you MUST uncomment the below code to receive push notifications.
         */
        SendBird.notifyActivityResumedForOldAndroids();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * If the minimum SDK version you support is under Android 4.0,
         * you MUST uncomment the below code to receive push notifications.
         */
         SendBird.notifyActivityPausedForOldAndroids();
    }

    private void setState(State state) {
        switch (state) {
            case DISCONNECTED:
                ((Button) findViewById(R.id.btn_connect)).setText("Connect");
                findViewById(R.id.btn_connect).setEnabled(true);
                findViewById(R.id.btn_group_channel_list).setEnabled(false);
                findViewById(R.id.btn_group_channel_list).setBackgroundResource(R.drawable.disable);

                break;

            case CONNECTING:
                ((Button) findViewById(R.id.btn_connect)).setText("Connecting...");
                findViewById(R.id.btn_connect).setEnabled(false);
                findViewById(R.id.btn_group_channel_list).setEnabled(false);
                break;

            case CONNECTED:
                ((Button) findViewById(R.id.btn_connect)).setText("Disconnect");
                findViewById(R.id.btn_connect).setEnabled(true);
                findViewById(R.id.btn_group_channel_list).setEnabled(true);
                findViewById(R.id.btn_group_channel_list).setBackgroundResource(R.drawable.notpressed);
                break;
        }
    }

    private void connect() {
        SendBird.connect(sUserId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setState(State.DISCONNECTED);
                    return;
                }

                String nickname = mNickname;

                SendBird.updateCurrentUserInfo(nickname, null, new SendBird.UserInfoUpdateHandler() {
                    @Override
                    public void onUpdated(SendBirdException e) {
                        if (e != null) {
                            Toast.makeText(MainActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            setState(State.DISCONNECTED);
                            return;
                        }

                        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                        editor.putString("user_id", sUserId);
                        editor.putString("nickname", mNickname);
                        editor.commit();

                        setState(State.CONNECTED);

                        Intent intent = new Intent(MainActivity.this, SendBirdGroupChannelListActivity.class);
                        startActivity(intent);
                    }
                });

                if (FirebaseInstanceId.getInstance().getToken() == null) return;

                SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), new SendBird.RegisterPushTokenWithStatusHandler() {
                    @Override
                    public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
                        if (e != null) {
                            Toast.makeText(MainActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });

        setState(State.CONNECTING);
    }

    private void disconnect() {
        SendBird.disconnect(new SendBird.DisconnectHandler() {
            @Override
            public void onDisconnected() {
                setState(State.DISCONNECTED);
            }
        });
    }
}
