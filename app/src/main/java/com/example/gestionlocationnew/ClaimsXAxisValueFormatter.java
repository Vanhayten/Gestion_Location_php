package com.example.gestionlocationnew;

import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;




public class ClaimsXAxisValueFormatter extends ValueFormatter {

    List<String> datesList;

    public ClaimsXAxisValueFormatter(List<String> arrayOfDates) {
        this.datesList = arrayOfDates;
    }


    @Override
    public String getAxisLabel(float value, AxisBase axis) {
/*
Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
*/
        Integer position = Math.round(value);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");

        if (value > 1 && value < 2) {
            position = 0;
        } else if (value > 2 && value < 3) {
            position = 1;
        } else if (value > 3 && value < 4) {
            position = 2;
        } else if (value > 4 && value <= 5) {
            position = 3;
        }else if (value > 5 && value <= 6) {
            position = 4;
        }
        if (position < datesList.size())
            return sdf.format(new Date((getDateInMilliSeconds(datesList.get(position), "dd/MM/yyyy"))));
        return "";
    }

    public static long getDateInMilliSeconds(String givenDateString, String format) {
        String DATE_TIME_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.FRENCH);
        long timeInMilliseconds = 1;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

}