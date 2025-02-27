/*
 * Copyright (C) 2019 The Android Open Source Project
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
package com.android.keyguard.clock;

import android.app.WallpaperManager;
import android.content.res.Resources;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextClock;
import android.graphics.Typeface;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.R;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.plugins.ClockPlugin;
import android.provider.Settings;
import java.util.TimeZone;

import java.util.TimeZone;

import static com.android.systemui.statusbar.phone
        .KeyguardClockPositionAlgorithm.CLOCK_USE_DEFAULT_Y;

/**
 * Plugin for the default clock face used only to provide a preview.
 */
public class SamsungClockController implements ClockPlugin {

    /**
     * Resources used to get title and thumbnail.
     */
    private final Resources mResources;

    /**
     * LayoutInflater used to inflate custom clock views.
     */
    private final LayoutInflater mLayoutInflater;

    /**
     * Extracts accent color from wallpaper.
     */
    private final SysuiColorExtractor mColorExtractor;

    /**
     * Renders preview from clock view.
     */
    private final ViewPreviewer mRenderer = new ViewPreviewer();

    /**
     * Root view of clock.
     */
    private ClockLayout mView;

    /**
     * Text clock in preview view hierarchy.
     */
    private TextClock mClock;


    protected Context mContext;

    /**
     * Create a DefaultClockController instance.
     *
     * @param res Resources contains title and thumbnail.
     * @param inflater Inflater used to inflate custom clock views.
     * @param colorExtractor Extracts accent color from wallpaper.
     */
    public SamsungClockController(Resources res, LayoutInflater inflater,
            SysuiColorExtractor colorExtractor, Context context) {
        mResources = res;
        mLayoutInflater = inflater;
        mColorExtractor = colorExtractor;
        mContext = context;
    }

    private void createViews() {
        mView = (ClockLayout) mLayoutInflater
                .inflate(R.layout.digital_clock_custom, null);
        mClock = mView.findViewById(R.id.clock);
        mClock.setLineSpacing(0, 0.8f);
        mClock.setFormat12Hour("hh\nmm");
        mClock.setFormat24Hour("kk\nmm");
        onTimeTick();
    }


    private int getLockClockSize() {
        return Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.LOCKCLOCK_FONT_SIZE, 78);
    }

   public void refreshclocksize() {
        int lockClockSize = getLockClockSize();
       if (lockClockSize == 35) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_35));
        } else if (lockClockSize == 36) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_36));
        } else if (lockClockSize == 37) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_37));
        } else if (lockClockSize == 38) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_38));
        } else if (lockClockSize == 39) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_39));
        } else if (lockClockSize == 40) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_40));
        } else if (lockClockSize == 41) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_41));
        } else if (lockClockSize == 42) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_42));
        } else if (lockClockSize == 43) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_43));
        } else if (lockClockSize == 44) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_44));
        } else if (lockClockSize == 45) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_45));
        } else if (lockClockSize == 46) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_46));
        } else if (lockClockSize == 47) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_47));
        } else if (lockClockSize == 48) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_48));
        } else if (lockClockSize == 49) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_49));
        } else if (lockClockSize == 50) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_50));
        } else if (lockClockSize == 51) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_51));
        } else if (lockClockSize == 52) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_52));
        } else if (lockClockSize == 53) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_53));
        } else if (lockClockSize == 54) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_54));
        } else if (lockClockSize == 55) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_55));
        } else if (lockClockSize == 56) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_56));
        } else if (lockClockSize == 57) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_57));
        } else if (lockClockSize == 58) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_58));
        } else if (lockClockSize == 59) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_59));
        } else if (lockClockSize == 60) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_60));
        }  else if (lockClockSize == 61) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_61));
        }  else if (lockClockSize == 62) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_62));
        }  else if (lockClockSize == 63) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_63));
        }  else if (lockClockSize == 64) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_64));
        }else if (lockClockSize == 65) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_65));
        } else if (lockClockSize == 66) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_66));
        } else if (lockClockSize == 66) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_67));
        } else if (lockClockSize == 68) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_68));
        } else if (lockClockSize == 69) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_69));
        } else if (lockClockSize == 70) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_70));
        } else if (lockClockSize == 71) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_71));
        } else if (lockClockSize == 72) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_72));
        } else if (lockClockSize == 73) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_73));
        } else if (lockClockSize == 74) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_74));
        } else if (lockClockSize == 75) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_75));
        } else if (lockClockSize == 76) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_76));
        } else if (lockClockSize == 77) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_77));
        } else if (lockClockSize == 78) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_78));
        } else if (lockClockSize == 79) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_79));
        } else if (lockClockSize == 80) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_80));
        } else if (lockClockSize == 81) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_81));
        } else if (lockClockSize == 82) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_82));
        } else if (lockClockSize == 83) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_83));
        } else if (lockClockSize == 84) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_84));
        }  else if (lockClockSize == 85) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_85));
        } else if (lockClockSize == 86) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_86));
        } else if (lockClockSize == 87) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_87));
        } else if (lockClockSize == 88) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_88));
        } else if (lockClockSize == 89) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_89));
        } else if (lockClockSize == 90) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_90));
        } else if (lockClockSize == 91) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_91));
        } else if (lockClockSize == 92) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_92));
        }  else if (lockClockSize == 93) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_93));
        } else if (lockClockSize == 94) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_94));
        } else if (lockClockSize == 95) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_95));
        } else if (lockClockSize == 96) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_96));
        } else if (lockClockSize == 97) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_97));
        } else if (lockClockSize == 98) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_98));
        } else if (lockClockSize == 99) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_99));
        } else if (lockClockSize == 100) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_100));
        } else if (lockClockSize == 101) {
        mClock.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimensionPixelSize(R.dimen.lock_clock_font_size_101));
        }
    }

    @Override
    public void onDestroyView() {
        mView = null;
        mClock = null;
    }

    @Override
    public String getName() {
        return "samsung";
    }

    @Override
    public String getTitle() {
        return mResources.getString(R.string.clock_title_samsung);
    }

    @Override
    public Bitmap getThumbnail() {
        return BitmapFactory.decodeResource(mResources, R.drawable.default_thumbnail);
    }

    @Override
    public Bitmap getPreview(int width, int height) {

        View previewView = mLayoutInflater.inflate(R.layout.default_clock_preview, null);
        TextClock previewTime = previewView.findViewById(R.id.time);
        previewTime.setLineSpacing(0, 0.8f);
        previewTime.setFormat12Hour("hh\nmm");
        previewTime.setFormat24Hour("kk\nmm");
        TextClock previewDate = previewView.findViewById(R.id.date);

        // Initialize state of plugin before generating preview.
        previewTime.setTextColor(Color.WHITE);
        previewDate.setTextColor(Color.WHITE);
        ColorExtractor.GradientColors colors = mColorExtractor.getColors(
                WallpaperManager.FLAG_LOCK);
        setColorPalette(colors.supportsDarkText(), colors.getColorPalette());

        return mRenderer.createPreview(previewView, width, height);
    }

    @Override
    public View getView() {
        if (mView == null) {
            createViews();
        }
        return mView;
    }

    @Override
    public View getBigClockView() {
        return null;
    }

    @Override
    public int getPreferredY(int totalHeight) {
        return CLOCK_USE_DEFAULT_Y;
    }

    @Override
    public void setStyle(Style style) {}

    @Override
    public void setTextColor(int color) {
        mClock.setTextColor(color);
    }

    @Override
    public void setTypeface(Typeface tf) {
        mClock.setTypeface(tf);
    }

    @Override
    public void setColorPalette(boolean supportsDarkText, int[] colorPalette) {}

    @Override
    public void onTimeTick() {
        if (mView != null && mClock != null) {
            mView.onTimeChanged();
            mClock.refresh();
            refreshclocksize();
        }
    }


    @Override
    public void setDarkAmount(float darkAmount) {
        mView.setDarkAmount(darkAmount);
    }

    @Override
    public void onTimeZoneChanged(TimeZone timeZone) {}

    @Override
    public boolean shouldShowStatusArea() {
        return true;
    }

    @Override
    public boolean shouldShowInBigContainer() {
        return true;
    }
}
