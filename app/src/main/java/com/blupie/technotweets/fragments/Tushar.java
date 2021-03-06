package com.blupie.technotweets.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.blupie.technotweets.adapters.DisplayAdaptor;
import com.blupie.technotweets.models.URLSpanNoUnderline;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Tushar extends Fragment {

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
    @BindView(R.id.linkdin_prot)
    TextView linkdinPro;
    @BindView(R.id.mobno_pro)
    TextView mobnoPro;
    Unbinder unbinder;

    public Tushar() {
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
        View rootView = inflater.inflate(R.layout.fragment_tushar, container, false);
        cardView = (CardView) rootView.findViewById(R.id.tushar);
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
        //removeUnderlines((Spannable) linkdinPro.getText());
        removeUnderlines((Spannable) mobnoPro.getText());

        // loadData();
    }

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

    @OnClick(R.id.linkdin_prot)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/tusharjn/"));
        startActivity(intent);
    }
}
