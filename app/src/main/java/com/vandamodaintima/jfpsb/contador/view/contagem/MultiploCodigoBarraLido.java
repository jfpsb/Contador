package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.MultiploCodigoBarraLidoController;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProdutoForContagemForResult;

import java.util.ArrayList;

public class MultiploCodigoBarraLido extends ActivityBaseView {

    private MultiploCodigoBarraLidoController controller;
    private ConexaoBanco conexaoBanco;
    private ListView listView;
    private AlertDialog.Builder produtoNaoSelecionado;
    private ItemCodigoBarraLido clicado;
    private ItemCodigoBarraLidoArrayAdapter arrayAdapter;

    private final int SELECIONAR_PRODUTO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_generic_list_view);
        stub.inflate();

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new MultiploCodigoBarraLidoController(conexaoBanco);

        listView = findViewById(R.id.listView);

        Bundle bundle = getIntent().getExtras();

        String loja = bundle.getString("loja");
        String data = bundle.getString("data");
        controller.carregaContagem(loja, data);

        SparseArray<Barcode> barcodeSparseArray = bundle.getSparseParcelableArray("barcodes");
        final ArrayList<ItemCodigoBarraLido> codigos = new ArrayList<>();

        for (int i = 0; i < barcodeSparseArray.size(); i++) {
            String codigo = barcodeSparseArray.valueAt(i).rawValue;

            ItemCodigoBarraLido item = new ItemCodigoBarraLido();

            item.setCodigo(codigo);
            ArrayList<Produto> produtos = controller.pesquisaProduto(codigo);

            item.setProdutos(produtos);

            if (produtos.size() == 0) {
                item.setStatus(ItemCodigoBarraLido.Status.NAO_ENCONTRADO);
            } else if (produtos.size() == 1) {
                item.setStatus(ItemCodigoBarraLido.Status.ENCONTRADO);
                controller.carregaProduto(produtos.get(0));
                controller.cadastrar();
            } else {
                item.setStatus(ItemCodigoBarraLido.Status.VARIOS_ENCONTRADOS);
            }

            codigos.add(item);
        }

        arrayAdapter = new ItemCodigoBarraLidoArrayAdapter(getApplicationContext(), codigos);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clicado = codigos.get(position);

                Intent intent;

                if (clicado.getStatus() == ItemCodigoBarraLido.Status.ENCONTRADO) {
                    intent = new Intent(getApplicationContext(), ListagemProdutoMultiploCodigoBarraLido.class);
                    intent.putExtra("produtos", clicado.getProdutos());
                    startActivity(intent);
                } else if (clicado.getStatus() == ItemCodigoBarraLido.Status.VARIOS_ENCONTRADOS) {
                    intent = new Intent(getApplicationContext(), ListagemProdutoMultiploCodigoBarraLido.class);
                    intent.putExtra("produtos", clicado.getProdutos());
                    startActivityForResult(intent, SELECIONAR_PRODUTO);
                } else {
                    intent = new Intent(getApplicationContext(), TelaProdutoForContagemForResult.class);
                    intent.putExtra("codigo", clicado.getCodigo());
                    startActivityForResult(intent, SELECIONAR_PRODUTO);
                }
            }
        });

        setProdutoNaoSelecionado();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECIONAR_PRODUTO) {
            if (resultCode == Activity.RESULT_OK) {
                Produto produto = (Produto) data.getSerializableExtra("produto");
                int quantidade = data.getIntExtra("quantidade", 0);
                controller.carregaProduto(produto);

                if(quantidade == 0) {
                    if (controller.cadastrar()) {
                        clicado.setStatus(ItemCodigoBarraLido.Status.ENCONTRADO);
                        arrayAdapter.notifyDataSetChanged();
                    }
                } else {
                    if(controller.cadastrar(quantidade)) {
                        clicado.setStatus(ItemCodigoBarraLido.Status.ENCONTRADO);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                produtoNaoSelecionado.show();
            }
        }
    }

    private void setProdutoNaoSelecionado() {
        produtoNaoSelecionado = new AlertDialog.Builder(MultiploCodigoBarraLido.this);
        produtoNaoSelecionado.setTitle("Produto Não Selecionado");
        produtoNaoSelecionado.setMessage("Produto Não Selecionado. Talvez Você Não Tenha o Encontrado na Lista. Deseja Abrir a Tela de Pesquisar/Cadastrar Produto?");

        produtoNaoSelecionado.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), TelaProdutoForContagemForResult.class);
                startActivityForResult(intent, SELECIONAR_PRODUTO);
            }
        });

        produtoNaoSelecionado.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Produto Não Selecionado. Contagem Não Adicionada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
