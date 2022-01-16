package leandro.caixeta.relftgoals.ui.termos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leandro.caixeta.relftgoals.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class termos extends Fragment {

    public termos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_termos, container, false);
    }
}
