package com.example.computacaomovel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    // TODO: Rename and change types of parameters
    private TextView detailText1;
    private TextView detailText2;
    private TextView detailText3;
    private TextView detailText3SubText1;
    private TextView detailText3SubText2;

    public DialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DialogFragment newInstance(SpannableStringBuilder text1, SpannableStringBuilder text2, SpannableStringBuilder text3, String text4, String text5) {
        DialogFragment fragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putCharSequence("text1", text1);
        args.putCharSequence("text2", text2);
        args.putCharSequence("text3", text3);
        args.putCharSequence("text4", text4);
        args.putCharSequence("text5", text5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailText1 = view.findViewById(R.id.detail_text_1);
        detailText2 = view.findViewById(R.id.detail_text_2);
        detailText3 = view.findViewById(R.id.detail_text_3);
        detailText3SubText1 = view.findViewById(R.id.detail_text_3_sub_text_1);
        detailText3SubText2 = view.findViewById(R.id.detail_text_3_sub_text_2);

        detailText1.setText(getArguments().getCharSequence("text1"));
        detailText2.setText(getArguments().getCharSequence("text2"));

        SpannableStringBuilder aux;
        if (!getArguments().getCharSequence("text4").toString().isEmpty()) {
            aux = new SpannableStringBuilder("Extrato CAPES");
            aux.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("Extrato CAPES").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            detailText3.setText(aux);
            aux = new SpannableStringBuilder("Computação: ");
            aux.append(getArguments().getCharSequence("text3"));
            aux.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, ("Computação: ").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            detailText3SubText1.setText(aux);
            aux = new SpannableStringBuilder(getArguments().getCharSequence("text5"));
            aux.append(": ").append(getArguments().getCharSequence("text4"));
            aux.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, getArguments().getCharSequence("text5").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            detailText3SubText2.setText(aux);
            detailText3SubText2.setVisibility(View.VISIBLE);
        } else {
            aux = new SpannableStringBuilder(getArguments().getCharSequence("text3"));
            detailText3.setText(aux);
            detailText3SubText1.setText("");
            detailText3SubText2.setVisibility(View.GONE);
        }

//        if (getArguments().getString("text4").isEmpty() && getArguments().getString("text5").isEmpty()) {
//
//        }
    }
}