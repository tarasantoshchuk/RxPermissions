package com.tbruyelle.rxpermissions.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private static final String[] ON_CREATE_PERMISSIONS = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO};
    private static final String[] TRIGGER_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxPermissions rxPermissions = RxPermissions.getInstance(this);
        rxPermissions.setLogging(true);

        setContentView(R.layout.act_main);

        mSubscription.add(rxPermissions.request(ON_CREATE_PERMISSIONS).subscribe(onCreatePermissionsSubscriber()));

        mSubscription.add(RxView.clicks(findViewById(R.id.enableCamera))
                // Ask for permissions when button is clicked
                .compose(rxPermissions.ensure(TRIGGER_PERMISSIONS))
                .subscribe(triggerPermissionsSubscriber()));
    }

    @NonNull
    private Subscriber<Boolean> triggerPermissionsSubscriber() {
        return new Subscriber<Boolean>() {
                       @Override
                       public void onCompleted() {

                       }

                       @Override
                       public void onError(Throwable e) {

                       }

                       @Override
                       public void onNext(Boolean aBoolean) {
                           if (aBoolean) {
                               Toast.makeText(MainActivity.this, "camera & storage granted", Toast.LENGTH_LONG).show();
                           }
                       }
                   };
    }

    @NonNull
    private Subscriber<Boolean> onCreatePermissionsSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(MainActivity.this, "contacts & accounts granted", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.clear();
    }
}
