package leandro.caixeta.relftgoals.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import leandro.caixeta.relftgoals.R;
import leandro.caixeta.relftgoals.activity.CadaEdit1;
import leandro.caixeta.relftgoals.classes.Usuario;
import leandro.caixeta.relftgoals.classes.usuariosControler;

/**
 * A simple {@link Fragment} subclass.
 */
public class perfil extends Fragment {

    private TextView nome, email,dataNacimento, celular ;
    private Button btnInfoPesoais ;
    public perfil() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        //ligando os componentes as suas respectivas variaveis
        nome =  root.findViewById(R.id.textPerfi_nome);
        email = root.findViewById(R.id.textPerfi_email);

        dataNacimento = root.findViewById(R.id.textPerfi_data_naciment);

        btnInfoPesoais = root.findViewById(R.id.btnPerfilInfoPesoais);

        //recuperando os dados que estao armazenados no arquivo do usuario
        buscardadosbdLocal(root.getContext());


        //fazendo evento de click dos botoes
        btnInfoPesoais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoPessoal = new Intent(getContext(), CadaEdit1.class);
                infoPessoal.putExtra("Acao","editInfoPessoais");
                startActivity(infoPessoal);
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void buscardadosbdLocal(Context context ){

        //pegando o email do usuario que esta logado nece momento na aplicação
        String emailUsu = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        usuariosControler usuariosControler = new usuariosControler(context);

        Usuario usuario = new Usuario();
        usuario = usuariosControler.buscarUsuario(emailUsu);

        if(usuario.getNome() == null){
            Toast.makeText(getContext(),"testando email: "+usuario.getNome(),Toast.LENGTH_LONG).show();
        }else {
            nome.setText(usuario.getNome());
            email.setText(usuario.getEmail());
            dataNacimento.setText(usuario.getData());
        }






    }
}
