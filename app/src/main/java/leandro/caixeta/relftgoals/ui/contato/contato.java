package leandro.caixeta.relftgoals.ui.contato;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.activity.Contato_form;

/**
 * A simple {@link Fragment} subclass.
 */
public class contato extends Fragment {

    private Button btncontato ;

    public contato() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contato, container, false);

        btncontato = root.findViewById(R.id.btncontato);


        btncontato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Contato_form.class));
            }
        });


        return root;
    }
}
