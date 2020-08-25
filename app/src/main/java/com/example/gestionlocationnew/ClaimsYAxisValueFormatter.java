package com.example.gestionlocationnew;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class ClaimsYAxisValueFormatter extends ValueFormatter {

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return String.format("%d",(int)value)+ " DH";
        //return value + " DH";
    }
}