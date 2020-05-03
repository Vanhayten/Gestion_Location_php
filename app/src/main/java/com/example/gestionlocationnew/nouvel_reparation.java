package com.example.gestionlocationnew;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nouvel_reparation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nouvel_reparation extends Fragment {
    EditText t1,t2,t3,t4,t5,t6;
    gestion_location dp;

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


         //   = getArguments().getString("matricule");

        String strtext = getActivity().getIntent().getExtras().getString("matricule");


        t1.setText(strtext);
        dp = new gestion_location(getActivity());
        // Inflate the layout for this fragment
        return view ;
    }
    public void enregistre (View view)
    {
        boolean b ;
        try {


            b = dp.insert_reparation(t1.getText().toString(), t2.getText().toString(), t3.getText().toString(), t4.getText().toString(), t5.getText().toString(), Integer.parseInt(t6.getText().toString()));
            Toast.makeText(getActivity(),"l'enregistrement effecuter",Toast.LENGTH_SHORT).show();
        }catch(Exception ex)
        {
            Toast.makeText(getActivity(),"l'enregistrement ne pas effectuer",Toast.LENGTH_SHORT).show();
        }

    }
}
