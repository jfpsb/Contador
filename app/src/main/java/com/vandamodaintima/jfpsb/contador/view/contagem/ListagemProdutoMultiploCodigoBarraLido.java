package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.produto.ProdutoArrayAdapter;

import java.util.ArrayList;

public class ListagemProdutoMultiploCodigoBarraLido extends ActivityBaseView {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_generic_list_view);
        stub.inflate();

        listView = findViewById(R.id.listView);

        final ArrayList<Produto> items = (ArrayList<Produto>) getIntent().getSerializableExtra("produtos");

        ProdutoArrayAdapter arrayAdapter = new ProdutoArrayAdapter(getApplicationContext(), items);
        listView.setAdapter(arrayAdapter);

        //Se tiver somente 1 item a lista é somente para mostrar o produto que teve a contagem adicionada
        //Senão o usuário poderá escolher qual produto na lista
        if (items.size() > 1) {
            Toast.makeText(getApplicationContext(), "Se Não Encontrar o Produto, Retorne Para a Tela Anterior", Toast.LENGTH_LONG).show();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Produto produto = items.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("produto", produto);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}
