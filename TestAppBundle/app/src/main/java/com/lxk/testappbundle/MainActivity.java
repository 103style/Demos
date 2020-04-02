package com.lxk.testappbundle;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.splitcompat.SplitCompat;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author https://github.com/103style
 * @date 2020/4/2 11:28
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int CONFIRMATION_REQUEST_CODE = 0x2020;
    private SplitInstallManager splitInstallManager;
    private SplitInstallStateUpdatedListener splitInstallStateUpdatedListener = state -> {
        switch (state.status()) {
            case SplitInstallSessionStatus.DOWNLOADING:
                toast("DOWNLOADING");
                break;
            case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                try {
                    splitInstallManager.startConfirmationDialogForResult(state, MainActivity.this, CONFIRMATION_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case SplitInstallSessionStatus.INSTALLED:
                toast("INSTALLED");
                break;
            case SplitInstallSessionStatus.INSTALLING:
                toast("INSTALLING");
                break;
            case SplitInstallSessionStatus.FAILED:
                toast("INSTALL FAILED");
                break;
        }
    };

    private void toast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        SplitCompat.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_t1).setOnClickListener(this);
        findViewById(R.id.bt_t2).setOnClickListener(this);
        findViewById(R.id.bt_t3).setOnClickListener(this);
        findViewById(R.id.bt_remove_all).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String featureName;
        switch (v.getId()) {
            case R.id.bt_t1:
                featureName = getString(R.string.title_feature1);
                break;
            case R.id.bt_t2:
                featureName = getString(R.string.title_feature2);
                break;
            case R.id.bt_t3:
            default:
                featureName = getString(R.string.title_feature3);
                break;
            case R.id.bt_remove_all:
                List<String> features = new ArrayList<>(getSplitInstallManager().getInstalledModules());
                getSplitInstallManager()
                        .deferredUninstall(features)
                        .addOnFailureListener(e -> {
                            toast("deferredUninstall error = " + e.getMessage());
                        })
                        .addOnSuccessListener(result -> {
                            toast("deferredUninstall result = " + result);
                        });

                return;
        }

        checkSplitLoadState(featureName, () -> {
            toast("do something after " + featureName + " install");
        });

    }


    public SplitInstallManager getSplitInstallManager() {
        if (splitInstallManager == null) {
            splitInstallManager = SplitInstallManagerFactory.create(this);
            splitInstallManager.registerListener(splitInstallStateUpdatedListener);
        }
        return splitInstallManager;
    }

    private void checkSplitLoadState(String featureName, Runnable runnable) {
        if (getSplitInstallManager().getInstalledModules().contains(featureName)) {
            toast(featureName + "is install");
            if (runnable != null) {
                runnable.run();
            }
            return;
        }
        getSplitInstallManager().startInstall(
                SplitInstallRequest.newBuilder()
                        .addModule(featureName)
                        .build()
        );
        toast(featureName + " startInstall");
    }
}