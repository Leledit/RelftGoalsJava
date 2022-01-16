package leandro.caixeta.relftgoals.ui.sobre;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import leandro.caixeta.relftgoals.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class sobre extends Fragment {

    public sobre() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sobre, container, false);
        return root;
    }
}
