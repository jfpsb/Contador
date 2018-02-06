package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.TelaProduto;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarProduto extends Fragment {
    private RadioGroup radioGroup;
    private EditText txtPesquisaProduto;

    public CadastrarProduto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        radioGroup = container.findViewById(R.id.radioGroupOpcao);
        txtPesquisaProduto = container.findViewById(R.id.txtPesquisaProduto);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selecao = radioGroup.getCheckedRadioButtonId();

                switch (selecao) {
                    case (R.id.rbCodBarra):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonCodBarra);
                        break;
                    case (R.id.rbDescricao):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonDescricao);
                        break;
                    case (R.id.rbFornecedor):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonNomeFornecedor);
                        break;
                    default:
                        Toast.makeText(getActivity(), "Aconteceu algo de errado", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cadastrar_produto, container, false);
    }

}
