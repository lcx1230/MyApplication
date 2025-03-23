package com.example.myapplication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class AdminManageFragment extends Fragment {
    private CardView cardConsultant, cardCommunity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_manage, container, false);

        cardConsultant = view.findViewById(R.id.card_consultant);
        cardCommunity = view.findViewById(R.id.card_community);

        cardConsultant.setOnClickListener(v -> startActivity(new Intent(getActivity(), CounselorManageActivity.class)));
        cardCommunity.setOnClickListener(v -> startActivity(new Intent(getActivity(), CommunityManageActivity.class)));

        return view;
    }
}
