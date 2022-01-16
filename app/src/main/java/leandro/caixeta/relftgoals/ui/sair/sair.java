package leandro.caixeta.relftgoals.ui.sair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.activity.MainActivity;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.Utilities;


public class sair extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);


        //limpando todos os arquivos de  sharedPreferences do usuario

        Utilities utilities = new Utilities();
        utilities.deletarSharedPreferences(getContext());

        Usuario usuario = new Usuario();
        usuario.deslogarUsuario();
        getActivity().finish();
        startActivity(new Intent(getActivity(),MainActivity.class));
        return root;


    }





}
