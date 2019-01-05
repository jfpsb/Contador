package com.vandamodaintima.jfpsb.contador.controller.marca;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarMarcaController {
    private CadastrarView view;
    private Context context;
    private DAOMarca daoMarca;
    private SimpleCursorAdapter marcaAdapter;

    public CadastrarMarcaController(CadastrarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoMarca = new DAOMarca(sqLiteDatabase);
        marcaAdapter = new SimpleCursorAdapter(context, R.layout.item_lista_marca, null, new String[]{"nome"}, new int[]{R.id.labelMarcaNome}, 0);
    }

    public Boolean cadastrar(Marca marca) {
        if(marca.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome da Marca Não Pode Ser Vazio");
            return false;
        }

        Boolean result = daoMarca.inserir(marca);

        if(result) {
            view.mensagemAoUsuario("Marca Cadastrada Com Sucesso");
            view.limparCampos();
            return true;
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Marca");
        }

        return false;
    }
}
