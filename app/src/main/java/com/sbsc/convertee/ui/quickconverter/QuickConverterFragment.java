package com.sbsc.convertee.ui.quickconverter;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sbsc.convertee.R;

public class QuickConverterFragment extends Fragment {

    private QuickConverterViewModel mViewModel;

    public static QuickConverterFragment newInstance() {
        return new QuickConverterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quickconverter, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QuickConverterViewModel.class);
        // TODO: Use the ViewModel
    }

}