package com.example.gestionlocationnew;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nouvel_reparation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nouvel_reparation extends Fragment {
    EditText t1,t2,t3,t4,t5,t6;
    gestion_location dp;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button b1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public nouvel_reparation() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nouvel_reparation.
     */
    // TODO: Rename and change types and number of parameters
    public static nouvel_reparation newInstance(String param1, String param2) {
        nouvel_reparation fragment = new nouvel_reparation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_nouvel_reparation, container, false);
        t1 = (EditText)view.findViewById(R.id.editText22);
        t2 = (EditText)view.findViewById(R.id.editText23);
        t3 = (EditText)view.findViewById(R.id.editText24);
        t4 = (EditText)view.findViewById(R.id.editText25);
        t5 = (EditText)view.findViewById(R.id.editText26);
        t6 = (EditText)view.findViewById(R.id.editText27);
        b1 = (Button) view.findViewById(R.id.butn);






        //   = getArguments().getString("matricule");

        String strtext = getActivity().getIntent().getExtras().getString("matricule");
        String login = getActivity().getIntent().getExtras().getString("login");

        /**
         * gete date reparation
         */


        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH);
                int Day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogDate = new DatePickerDialog(getContext()
                        ,android.R.style.Theme_Holo_Dialog_MinWidth
                        ,mDateSetListener,Year, Month,Day);

                dialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogDate.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month =month+1;
                String datefin = dayOfMonth+"/"+month+"/"+year;
                t5.setText(datefin);
            }
        };








        t1.setText(strtext);
       dp = new gestion_location(getActivity());
        // Inflate the layout for this fragment
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(t1.getText()) && !TextUtils.isEmpty(t2.getText()) && !TextUtils.isEmpty(t3.getText()) && !TextUtils.isEmpty(t4.getText()) && !TextUtils.isEmpty(t5.getText())  && !TextUtils.isEmpty(t6.getText())){
                boolean b = dp.insert_reparation(t1.getText().toString(), t2.getText().toString(), t3.getText().toString(), t4.getText().toString(), t5.getText().toString(), Integer.parseInt(t6.getText().toString()),login);
                 if(b) {
                     Snackbar snack = Snackbar.make(view,"l'enregistrement effecuter",Snackbar.LENGTH_LONG);
                     View sbView = snack.getView();
                     sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_green));
                     snack.show();
                     //Toast.makeText(getActivity(), "l'enregistrement effecuter", Toast.LENGTH_SHORT).show();

                     //t1.setText("");
                     t2.setText("");
                     t3.setText("");
                     t4.setText("");
                     t5.setText("");
                     t6.setText("");

                 }else{
                    //Toast.makeText(getActivity(),"l'enregistrement ne pas effectuer",Toast.LENGTH_SHORT).show();
                     Snackbar snack = Snackbar.make(view,"l'enregistrement ne pas effectuer",Snackbar.LENGTH_LONG);
                     View sbView = snack.getView();
                     sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_red));
                     snack.show();

                }

            }else{
                    Snackbar snack = Snackbar.make(view,"les champs obligatoire",Snackbar.LENGTH_SHORT);
                    View sbView = snack.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.color_warning_yellow));
                    snack.show();
                }
            }
        });
        return view ;
    }
}


