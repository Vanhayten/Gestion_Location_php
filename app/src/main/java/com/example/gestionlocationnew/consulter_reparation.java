package com.example.gestionlocationnew;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link consulter_reparation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class consulter_reparation extends Fragment {
    gestion_location db;

    EditText t1, t2;
    ListView ls;
    ArrayList<String> arrayList = new ArrayList<String> ();
    ArrayList<class_reparation> arrayList_reparation;
    Button b1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public consulter_reparation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment consulter_reparation.
     */
    // TODO: Rename and change types and number of parameters
    public static consulter_reparation newInstance(String param1, String param2) {
        consulter_reparation fragment = new consulter_reparation();
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
        // Inflate the layout for this fragment

         View view=inflater.inflate(R.layout.fragment_consulter_reparation, container, false);
        db = new gestion_location ( getActivity () );


        t1 = (EditText) view.findViewById ( R.id.date1 );
        t2 = (EditText) view.findViewById ( R.id.date2 );
        ls = (ListView) view.findViewById ( R.id.liste_reparation );
        //
        String strtext = getActivity().getIntent().getExtras().getString("matricule");
        SQLiteDatabase table = db.getReadableDatabase ();
        String requet = "select * from reparation where imatriculation ='" +strtext+ "' ";
        Cursor c = table.rawQuery ( requet, null );
        ArrayList<class_reparation> arrayList = new ArrayList<> ();
        arrayList.clear ();
        while (c.moveToNext ())
        {
            class_reparation list = new class_reparation ( "date : " + c.getString ( 4 ) + "      Matricule :" + c.getString ( 0 ),   "pièces : " + c.getString ( 1 ) + "         montant : " + c.getString ( 5 ),    "référence facture : " + c.getString ( 3 ) );
            arrayList.add ( list );
        }
        arrayList_reparation = arrayList;
        adapter_reparation listrep = new adapter_reparation ( getActivity (), arrayList_reparation );
        ls.setAdapter ( listrep );
        //
        b1 = (Button) view.findViewById ( R.id.aficher_reparat );
        b1.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {


                try {
                    String d1 = t1.getText ().toString ();
                    String d2 = t2.getText ().toString ();
                    String strtext = getActivity().getIntent().getExtras().getString("matricule");
                    SQLiteDatabase table = db.getReadableDatabase ();
                    String requet = "select * from reparation where imatriculation ='" +strtext+ "' and   date_reparation  between '" + d1 + "' and '" + d2 + "' ";
                    Cursor c = table.rawQuery ( requet, null );
                    ArrayList<class_reparation> arrayList = new ArrayList<> ();
                    arrayList.clear ();
                    while (c.moveToNext ())
                    {
                        class_reparation list = new class_reparation ( "date : " + c.getString ( 4 ) + "      Matricule :" + c.getString ( 0 ),   "pièces : " + c.getString ( 1 ) + "         montant : " + c.getString ( 5 ),    "référence facture : " + c.getString ( 3 ) );
                        arrayList.add ( list );
                    }
                    arrayList_reparation = arrayList;
                    adapter_reparation listrep = new adapter_reparation ( getActivity (), arrayList_reparation );
                    ls.setAdapter ( listrep );
                } catch (Exception ex) {
                }
            }
        } );

        return view;
    }
}