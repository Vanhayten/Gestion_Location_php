package com.example.gestionlocationnew;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.gestionlocationnew.R;
import com.example.gestionlocationnew.liste_choix_matr;

import java.util.ArrayList;

public class Page_Adapter_choix_matr extends ArrayAdapter<liste_choix_matr> {


    private int mColorResourceId;


    private static final String LOG_TAG = Page_Adapter_choix_matr.class.getSimpleName();

    public Page_Adapter_choix_matr(Activity context, ArrayList<liste_choix_matr> resource) {
        super(context, 0, resource);

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_choix_matr_clients, parent, false);
        }


        final liste_choix_matr currentAndroidFlavor = getItem(position);



        TextView cinTextView = (TextView) listItemView.findViewById(R.id.matr_Choix);

        cinTextView.setText(currentAndroidFlavor.getmatr());




        return listItemView;
    }

}
