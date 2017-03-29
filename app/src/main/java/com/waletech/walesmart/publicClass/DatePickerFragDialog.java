package com.waletech.walesmart.publicClass;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;


import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.user.lockInfo.record.RecordAction;

import java.util.Calendar;

/**
 * Created by KeY on 2016/5/28.
 */
public class DatePickerFragDialog extends AppCompatDialogFragment {

    public final static int DATE_FAVOURITE = 0;
    public final static int DATE_RECORD = 1;

    private static Calendar mCalendar = Calendar.getInstance();
    private String date_str;

    private BaseAction baseAction;
    private int dateType;

    public void setBaseAction(BaseAction baseAction, int dateType) {
        this.baseAction = baseAction;
        this.dateType = dateType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = mCalendar;

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        date_str = year + "-" + onMonthFormat(month) + "-" + onDateFormat(day);
//        date_str = year + "-0" + (month + 1) + "-" + day;

        return new DatePickerDialog(getActivity(), dateListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            mCalendar = calendar;

            TextView date_tv = (TextView) getActivity().findViewById(R.id.filter_tv);
            String date = year + "-" + onMonthFormat(monthOfYear) + "-" + onDateFormat(dayOfMonth);
//            String date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
            date_tv.setText(date);

            // int week = calendar.get(Calendar.DAY_OF_WEEK);
            // date_tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "-" + getWeekDay(week));
        }
    };

    @Override
    public void onStop() {
        TextView date_tv = (TextView) getActivity().findViewById(R.id.filter_tv);

        if (!date_str.equals(date_tv.getText().toString())) {
            date_str = date_tv.getText().toString();

            switch (dateType) {
                case DATE_FAVOURITE:
                    break;

                case DATE_RECORD:
                    ((RecordAction) baseAction).getFilterRecord(date_str, "", "", "");
                    break;

                default:
                    break;
            }
        }

        super.onStop();
    }

    private String onMonthFormat(int month) {
        int real_month = month + 1;
        if (real_month < 10) {
            return "0" + real_month;
        } else {
            return "" + real_month;
        }
    }

    private String onDateFormat(int date) {
        if (date < 10) {
            return "0" + date;
        } else {
            return "" + date;
        }
    }

    //    private String getWeekDay(int week) {
//        switch (week) {
//            case 0:
//                return "星期一";
//            case 1:
//                return "星期二";
//            case 2:
//                return "星期三";
//            case 3:
//                return "星期四";
//            case 4:
//                return "星期五";
//            case 5:
//                return "星期六";
//            case 6:
//                return "星期日";
//            default:
//                return "";
//        }
//    }
}
