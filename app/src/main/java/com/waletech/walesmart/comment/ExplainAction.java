package com.waletech.walesmart.comment;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.waletech.walesmart.R;

/**
 * Created by KeY on 2016/5/18.
 */
public final class ExplainAction {

    private final static String TAG_RATING_WIDTH = "tag_rating_width";
    private final static String TAG_RATING_MATERIAL = "tag_rating_materail";
    private final static String TAG_RATING_SPORT = "tag_rating_sport";

    private static AppCompatActivity compatActivity;

    private static Handler handler;

    public static void showExplain(Context context, String rating_tag, int rating) {
        compatActivity = (AppCompatActivity) context;
        varInit();

        onRating(rating_tag, rating);
    }

    private static void varInit() {
        handler = new Handler();
    }

    private static void onRating(String rating_tag, int rating) {
        StringBuilder text = new StringBuilder();

        switch (rating_tag) {
            case TAG_RATING_WIDTH:
                text.append(Width.getDescription(rating));
                break;

            case TAG_RATING_MATERIAL:
                text.append(Material.getDescription(rating));
                break;

            case TAG_RATING_SPORT:
                text.append(Sport.getDescription(rating));
                break;
        }

        final TextView com_tv = (TextView) compatActivity.findViewById(R.id.com_tv);
        com_tv.setVisibility(View.VISIBLE);
        com_tv.setText(text);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                com_tv.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    private enum Width {
        TOO_NARROW(1, "太窄"), LITTLE_NARROW(2, "有点窄"), FIT(3, "刚好合适"), LITTLE_WIDER(4, "有点宽"), TOO_WIDER(5, "太宽");

        private int index;
        private String description;

        Width(int index, String description) {
            this.index = index;
            this.description = description;
        }

        public int getIndex() {
            return index;
        }

        public static String getDescription(int i) {
            for (Width w : Width.values()) {
                if (w.getIndex() == i) {
                    return w.description;
                }
            }
            return null;
        }
    }

    private enum Material {
        TOO_UNCOMFORTABLE(1, "很不舒服"), LITTLE_UNCOMFORTABLE(2, "比较不舒服"), FIT(3, "刚好"),
        LITTLE_COMFORTABLE(4, "比较舒服"), TOO_COMFORTABLE(5, "很舒服");

        private int index;
        private String description;

        Material(int index, String description) {
            this.index = index;
            this.description = description;
        }

        public int getIndex() {
            return index;
        }

        public static String getDescription(int i) {
            for (Material m : Material.values()) {
                if (m.getIndex() == i) {
                    return m.description;
                }
            }
            return null;
        }
    }

    private enum Sport {
        TOO_UNCOMFORTABLE(1, "很不舒服"), LITTLE_UNCOMFORTABLE(2, "比较不舒服"), FIT(3, "刚好"),
        LITTLE_COMFORTABLE(4, "比较舒服"), TOO_COMFORTABLE(5, "很舒服");

        private int index;
        private String description;

        Sport(int index, String description) {
            this.index = index;
            this.description = description;
        }

        public int getIndex() {
            return index;
        }

        public static String getDescription(int i) {
            for (Sport s : Sport.values()) {
                if (s.getIndex() == i) {
                    return s.description;
                }
            }
            return null;
        }
    }
}
