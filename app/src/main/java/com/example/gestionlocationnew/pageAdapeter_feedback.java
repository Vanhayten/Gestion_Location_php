package com.example.gestionlocationnew;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class pageAdapeter_feedback extends ArrayAdapter<liste_feedback> {

    private int mColorResourceId;


    private static final String LOG_TAG = pageAdapeter_feedback.class.getSimpleName();

    public pageAdapeter_feedback(Activity context, ArrayList<liste_feedback> resource) {
        super(context, 0, resource);

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.rows_feedback, parent, false);
        }


        final liste_feedback currentAndroidFlavor = getItem(position);



        RatingBar rt = (RatingBar) listItemView.findViewById(R.id.ratingb);

        rt.setRating(currentAndroidFlavor.getRtin());


        TextView date = (TextView) listItemView.findViewById(R.id.dateFeed);

        date.setText(currentAndroidFlavor.getDate());

        TextView desc = (TextView) listItemView.findViewById(R.id.descFeed);

        desc.setText(currentAndroidFlavor.getDes());

        return listItemView;
    }
}
