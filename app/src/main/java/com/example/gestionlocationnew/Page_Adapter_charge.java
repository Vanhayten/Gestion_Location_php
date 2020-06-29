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
import java.util.ArrayList;

public class Page_Adapter_charge extends ArrayAdapter<list_charge>
{

    private static final String LOG_TAG = Page_Adapter_charge.class.getSimpleName();

    public Page_Adapter_charge(Activity context, ArrayList<list_charge> resource) {
        super(context, 0, resource);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.charge_rows, parent, false);
        }


        final list_charge currentAndroidFlavor = getItem(position);

        TextView desTextView = (TextView) listItemView.findViewById(R.id.des);

        String dest = currentAndroidFlavor.getDesignation();
        desTextView.setText(dest);

        TextView montantTextView = (TextView) listItemView.findViewById(R.id.mnt);

        String montant = currentAndroidFlavor.getMontant()+" DH";
        montantTextView.setText(montant);

        TextView modepayTextView = (TextView) listItemView.findViewById(R.id.mdpay);

        String modepay = currentAndroidFlavor.getMdpayer();
        modepayTextView.setText(modepay);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.dat);

        String date = currentAndroidFlavor.getDate();
        dateTextView.setText(date);

        TextView idTextView = (TextView) listItemView.findViewById(R.id.Id_charge);

        String charge = currentAndroidFlavor.getIDCharge();
        idTextView.setText(charge);
        idTextView.setVisibility(View.INVISIBLE);


        return listItemView;
    }
}

