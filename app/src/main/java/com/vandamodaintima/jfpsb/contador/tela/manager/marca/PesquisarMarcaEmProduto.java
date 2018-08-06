package com.vandamodaintima.jfpsb.contador.tela.manager.marca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.entidade.Marca;

/**
 * Usado em TelaMarcaForResult
 * Lista as marcas e permite que o usuário escolha uma ao clicar nela
 * e atribuir ao produto
 * Isso é uma Fragment
 */
public class PesquisarMarcaEmProduto extends PesquisarMarca {
    private AlertDialog.Builder alertaEscolha;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setListOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                Marca marca = new Marca();

                marca.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                setAlertaEscolha(marca);
            }
        });
    }

    private void setAlertaEscolha(final Marca marca) {
        alertaEscolha = new AlertDialog.Builder(getContext());
        alertaEscolha.setTitle("Escolher Marca");
        alertaEscolha.setMessage("Tem Certeza Que Deseja Escolher a Marca " + marca.getNome() + "?");

        alertaEscolha.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((TelaMarcaForResult) getActivity()).setResultado(marca);
            }
        });

        alertaEscolha.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Marca Não Foi Escolhida", Toast.LENGTH_SHORT).show();
            }
        });

        alertaEscolha.show();
    }
}
