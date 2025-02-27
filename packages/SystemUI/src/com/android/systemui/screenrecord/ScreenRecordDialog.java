/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.screenrecord;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.systemui.R;
import com.android.systemui.shared.system.ActivityManagerWrapper;

import static com.android.systemui.statusbar.phone.StatusBar.SYSTEM_DIALOG_REASON_SCREENSHOT;
import static android.provider.Settings.System.SCREENRECORD_VIDEO_BITRATE;
import static android.provider.Settings.System.SCREENRECORD_AUDIO_OPT;
import static android.provider.Settings.System.SCREENRECORD_SHOW_TAPS;
import static android.provider.Settings.System.SCREENRECORD_STOP_DOT;
import static android.provider.Settings.System.SCREENRECORD_SCREENOFF;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Activity to select screen recording options
 */
public class ScreenRecordDialog extends Activity {
    private static final String TAG = "ScreenRecord";

    private static final int REQUEST_CODE_PERMISSIONS = 201;
    private static final int REQUEST_CODE_PERMISSIONS_AUDIO = 202;

    private static final int REQUEST_CODE_VIDEO = 301;
    private static final int REQUEST_CODE_VIDEO_TAPS = 302;
    private static final int REQUEST_CODE_VIDEO_TAPS_DOT = 303;
    private static final int REQUEST_CODE_VIDEO_DOT = 304;

    private static final int REQUEST_CODE_VIDEO_AUDIO = 401;
    private static final int REQUEST_CODE_VIDEO_AUDIO_TAPS = 402;
    private static final int REQUEST_CODE_VIDEO_AUDIO_DOT = 403;
    private static final int REQUEST_CODE_VIDEO_AUDIO_TAPS_DOT = 404;

    private int mVideoBitrateOpt;
    private boolean mUseAudio;
    private int mAudioSourceOpt;
    private boolean mShowTaps;
    private boolean mShowDot;
    private boolean mScreenoff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_record_dialog);

        Window window = getWindow();
        assert window != null;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);

        final Spinner bitrateSpinner = findViewById(R.id.spinner_video_bitrate);
        final Spinner audioSourceSpinner = findViewById(R.id.spinner_audio_source);
        final Switch tapsSwitch = findViewById(R.id.switch_taps);
        final Switch dotSwitch = findViewById(R.id.switch_stopdot);
        final Switch screenoff = findViewById(R.id.switch_screenoff);

        ArrayAdapter<CharSequence> bitrateAdapter = ArrayAdapter.createFromResource(this,
            SystemProperties.get("ro.config.low_ram").equals("true") ? R.array.screen_video_quality_go_entries :
                        R.array.screen_video_quality_entries, android.R.layout.simple_spinner_item);
        bitrateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bitrateSpinner.setAdapter(bitrateAdapter);

        // Check if the device supports internal audio recording
        boolean intAudioEnabled = getResources().getBoolean(R.bool.config_recorderInternalAudio);

        ArrayAdapter<CharSequence> audioSourceAdapter = ArrayAdapter.createFromResource(this,
            intAudioEnabled ? R.array.screen_audio_recording_entries : R.array.screen_audio_recording_nointernal_entries,
            android.R.layout.simple_spinner_item);
        audioSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        audioSourceSpinner.setAdapter(audioSourceAdapter);

        initialCheckSpinner(bitrateSpinner, SCREENRECORD_VIDEO_BITRATE, 2 /* average option */);
        initialCheckSpinner(audioSourceSpinner, SCREENRECORD_AUDIO_OPT, 0 /* disabled */);
        initialCheckSwitch(tapsSwitch, SCREENRECORD_SHOW_TAPS);
        initialCheckSwitch(dotSwitch, SCREENRECORD_STOP_DOT);
        initialCheckSwitch(screenoff, SCREENRECORD_SCREENOFF);

        setSwitchListener(tapsSwitch, SCREENRECORD_SHOW_TAPS);
        setSwitchListener(dotSwitch, SCREENRECORD_STOP_DOT);
        setSwitchListener(screenoff, SCREENRECORD_SCREENOFF);
        setSpinnerListener(bitrateSpinner, SCREENRECORD_VIDEO_BITRATE);
        setSpinnerListener(audioSourceSpinner, SCREENRECORD_AUDIO_OPT);

        final Button recordButton = findViewById(R.id.record_button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowTaps = tapsSwitch.isChecked();
                mShowDot = dotSwitch.isChecked();
                mScreenoff = screenoff.isChecked();
                mVideoBitrateOpt = bitrateSpinner.getSelectedItemPosition();
                mAudioSourceOpt = audioSourceSpinner.getSelectedItemPosition();
                Log.d(TAG, "Record button clicked: bitrate " + mVideoBitrateOpt + " audio " + mAudioSourceOpt + ", taps " + mShowTaps + ", dot " + mShowDot  + ", screenoffswitch " + mScreenoff);

                if (mUseAudio && ScreenRecordDialog.this.checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Requesting permission for audio");
                    ScreenRecordDialog.this.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_CODE_PERMISSIONS_AUDIO);
                } else {
                    ScreenRecordDialog.this.requestScreenCapture();
                }
            }
        });
        final Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenRecordDialog.this.finish();
            }
        });

        try {
            ActivityManagerWrapper.getInstance().closeSystemWindows(
                    SYSTEM_DIALOG_REASON_SCREENSHOT).get(
                    3000/*timeout*/, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {}
    }

    private void initialCheckSwitch(Switch sw, String setting) {
        sw.setChecked(
                Settings.System.getIntForUser(this.getContentResolver(),
                        setting, 0, UserHandle.USER_CURRENT) == 1);
    }

    private void initialCheckSpinner(Spinner spin, String setting, int defaultValue) {
        spin.setSelection(
                Settings.System.getIntForUser(this.getContentResolver(),
                        setting, defaultValue, UserHandle.USER_CURRENT));
    }

    private void setSwitchListener(Switch sw, String setting) {
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Settings.System.putIntForUser(this.getContentResolver(),
                    setting, isChecked ? 1 : 0, UserHandle.USER_CURRENT);
        });
    }

    private void setSpinnerListener(Spinner spin, String setting) {
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Settings.System.putIntForUser(getContentResolver(),
                        setting, position, UserHandle.USER_CURRENT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void requestScreenCapture() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
        assert mediaProjectionManager != null;
        Intent permissionIntent = mediaProjectionManager.createScreenCaptureIntent();

        if (mAudioSourceOpt > 0) {
            startActivityForResult(permissionIntent,
                    mShowTaps ? (mShowDot ? REQUEST_CODE_VIDEO_AUDIO_TAPS_DOT : REQUEST_CODE_VIDEO_AUDIO_TAPS)
                            : (mShowDot ? REQUEST_CODE_VIDEO_AUDIO_DOT : REQUEST_CODE_VIDEO_AUDIO));
        } else {
            startActivityForResult(permissionIntent,
                    mShowTaps ? (mShowDot ? REQUEST_CODE_VIDEO_TAPS_DOT : REQUEST_CODE_VIDEO_TAPS)
                            : (mShowDot ? REQUEST_CODE_VIDEO_DOT : REQUEST_CODE_VIDEO));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShowTaps = requestCode == REQUEST_CODE_VIDEO_TAPS
                || requestCode == REQUEST_CODE_VIDEO_AUDIO_TAPS
                || requestCode == REQUEST_CODE_VIDEO_TAPS_DOT
                || requestCode == REQUEST_CODE_VIDEO_AUDIO_TAPS_DOT;
        mShowDot = requestCode == REQUEST_CODE_VIDEO_AUDIO_DOT
                || requestCode == REQUEST_CODE_VIDEO_DOT
                || requestCode == REQUEST_CODE_VIDEO_TAPS_DOT
                || requestCode == REQUEST_CODE_VIDEO_AUDIO_TAPS_DOT;
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                int permission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            getResources().getString(R.string.screenrecord_permission_error),
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    requestScreenCapture();
                }
                break;
            case REQUEST_CODE_PERMISSIONS_AUDIO:
                int videoPermission = checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int audioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                if (videoPermission != PackageManager.PERMISSION_GRANTED
                        || audioPermission != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            getResources().getString(R.string.screenrecord_permission_error),
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    requestScreenCapture();
                }
                break;
            default:
                if (resultCode == RESULT_OK) {
                    startForegroundService(
                            RecordingService.getStartIntent(this, resultCode, data, mAudioSourceOpt,
                                    mShowTaps, mShowDot, mVideoBitrateOpt, mScreenoff));
                } else {
                    Toast.makeText(this,
                            getResources().getString(R.string.screenrecord_permission_error),
                            Toast.LENGTH_SHORT).show();
                }
                finish();
        }
    }
}
