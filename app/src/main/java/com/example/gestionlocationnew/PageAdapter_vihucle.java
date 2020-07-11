package com.example.gestionlocationnew;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class PageAdapter_vihucle extends ArrayAdapter<list_vihcule>
{
    private int mColorResourceId;
    public String col;

    private static final String LOG_TAG = PageAdapter_client.class.getSimpleName();

    public PageAdapter_vihucle(Activity context, ArrayList<list_vihcule> resource) {
        super(context, 0, resource);

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.vicucule_rows, parent, false);
        }


        final list_vihcule currentAndroidFlavor = getItem(position);



        TextView matrTextView = (TextView) listItemView.findViewById(R.id.matrV);

        matrTextView.setText(currentAndroidFlavor.getMarque());


        TextView marqueTextView = (TextView) listItemView.findViewById(R.id.marqueV);

        marqueTextView.setText(currentAndroidFlavor.getMatr());

        /*
        col=currentAndroidFlavor.getColor();
        switch (col){
            case "Rouge":
                marqueTextView.setTextColor(Color.RED);
                break;
            case "Vert":
                marqueTextView.setTextColor(Color.GREEN);
                break;
            case "Noir":
                marqueTextView.setTextColor(Color.BLACK);
                break;
            case "Jaune":
                marqueTextView.setTextColor(Color.YELLOW);
                break;
            case "Gris":
                marqueTextView.setTextColor(Color.GRAY);
                break;
            case "Bleu":
                marqueTextView.setTextColor(Color.BLUE);
                break;
            case "Blanc":
                marqueTextView.setTextColor(Color.WHITE);
                break;

        }*/
        mColorResourceId = currentAndroidFlavor.getColor();
        //int color = ContextCompat.getColor(getContext(),mColorResourceId );
        marqueTextView.setTextColor(mColorResourceId);




        return listItemView;
    }


}



