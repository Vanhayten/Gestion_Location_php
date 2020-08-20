package com.example.gestionlocationnew;

import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;




public class ClaimsXAxisValueFormatter implements IAxisValueFormatter {

    private Calendar c;
    private LineChart chart;
    private TextView sticky;
    private float lastFormattedValue = 1e9f;
    private int lastMonth = 0;
    private int lastYear = 0;
    private int stickyMonth = -1;
    private int stickyYear = -1;
    private SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM", Locale.getDefault());


    ClaimsXAxisValueFormatter(LineChart chart, TextView sticky) {
        c = new GregorianCalendar();
        this.chart = chart;
        this.sticky = sticky;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        // Sometimes this gets called on values much lower than the visible range
        // Catch that here to prevent messing up the sticky text logic
        if( value < chart.getLowestVisibleX() ) {
            return "";
        }

        // NOTE: I assume for this example that all data is plotted in days
        // since Jan 1, 2018. Update for your scheme accordingly.

        int days = (int)value;

        boolean isFirstValue = value < lastFormattedValue;

        if( isFirstValue ) {
            // starting over formatting sequence
            lastMonth = 50;
            lastYear = 5000;

            c.set(2018,0,1);
            c.add(Calendar.DATE, (int)chart.getLowestVisibleX());

            stickyMonth = c.get(Calendar.MONTH);
            stickyYear = c.get(Calendar.YEAR);

            String stickyText = monthFormatter.format(c.getTime()) + "\n" + stickyYear;
            sticky.setText(stickyText);
        }

        c.set(2018,0,1);
        c.add(Calendar.DATE, days);
        Date d = c.getTime();

        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        String monthStr = monthFormatter.format(d);

        if( (month > stickyMonth || year > stickyYear) && isFirstValue ) {
            stickyMonth = month;
            stickyYear = year;
            String stickyText = monthStr + "\n" + year;
            sticky.setText(stickyText);
        }

        String ret;

        if( (month > lastMonth || year > lastYear) && !isFirstValue ) {
            ret = monthStr;
        }
        else {
            ret = Integer.toString(dayOfMonth);
        }

        lastMonth = month;
        lastYear = year;
        lastFormattedValue = value;

        return ret;
    }
}
