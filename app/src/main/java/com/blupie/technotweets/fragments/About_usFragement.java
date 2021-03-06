package com.blupie.technotweets.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blupie.technotweets.R;
import com.blupie.technotweets.models.URLSpanNoUnderline;
import com.blupie.technotweets.adapters.DisplayAdaptor;
import com.blupie.technotweets.models.URLSpanNoUnderline;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class About_usFragement extends Fragment {

    public String sname;
    public String spos;
    public String sdesc;
    static CardView cardView;
    public View v;

    ProgressDialog progressDialog;
    DatabaseReference myref;

    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.email_pro)
    TextView emailPro;
    @BindView(R.id.linkdin_pro)
    TextView linkdinPro;
    @BindView(R.id.mobno_pro)
    TextView mobnoPro;
    Unbinder unbinder;

    public About_usFragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Details");
        // progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        //  progressDialog.show();
        View rootView = inflater.inflate(R.layout.fragment_about_us_fragement, container, false);
        cardView = (CardView) rootView.findViewById(R.id.cardViewdetails);
        cardView.setMaxCardElevation(cardView.getCardElevation()
                * DisplayAdaptor.MAX_ELEVATION_FACTOR);

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        removeUnderlines((Spannable) emailPro.getText());
        removeUnderlines((Spannable) linkdinPro.getText());
        removeUnderlines((Spannable) mobnoPro.getText());

        // loadData();
    }
//
//    private void loadData() {
//        myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons").child(key);
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                spos = (String) dataSnapshot.child("pos").getValue();
//                sname = (String) dataSnapshot.child("name").getValue();
//                sdesc = (String) dataSnapshot.child("description").getValue();
//                name.setText(sname);
//                description.setText(sdesc);
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }

    public static void removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
    }

    public static CardView getCardView() {
        return cardView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
