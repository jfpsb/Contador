package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.InserirContagemProdutoController;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.contagem.contagemproduto.AlterarDeletarContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.grade.ListarProdutoGradePorCodigoForResult;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ICadastrarView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IProcessaCodigoBarraLido;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProdutoForContagemForResult;
import com.vandamodaintima.jfpsb.contador.view.codigodebarra.TelaLerCodigoDeBarra;

public class InserirContagemProduto extends ActivityBaseView implements ICadastrarView, IProcessaCodigoBarraLido {
    private ConexaoBanco conexaoBanco;

    private FloatingActionButton btnAbrirCamera;
    private FloatingActionButton btnAdicionarContagem;
    private ListView listViewContagemProduto;
    private InserirContagemProdutoController controller;
    private AlertDialog.Builder alertProdutoGradeNaoEncontrado;

    private static final int TELA_SELECIONAR_PRODUTO = 1;
    private static final int TELA_LER_CODIGO_BARRAS = 2;
    private static final int ALTERAR_DELETAR_CONTAGEM_PRODUTO = 3;
    private static final int VISUALIZAR_PRODUTO_GRADE_CONTAGEM = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conexaoBanco = new ConexaoBanco(this);

        stub.setLayoutResource(R.layout.activity_inserir_contagem_produto);
        stub.inflate();

        btnAbrirCamera = findViewById(R.id.btnAbrirCamera);
        btnAdicionarContagem = findViewById(R.id.btnAdicionarContagem);
        listViewContagemProduto = findViewById(R.id.listViewContagemProduto);

        setAlertProdutoGradeNaoEncontrado();

        btnAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TelaLerCodigoDeBarra.class);
                startActivityForResult(intent, TELA_LER_CODIGO_BARRAS);
            }
        });

        btnAdicionarContagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TelaProdutoForContagemForResult.class);
                startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
            }
        });

        listViewContagemProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContagemProduto contagemProduto = (ContagemProduto) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), AlterarDeletarContagemProduto.class);
                intent.putExtra("contagemproduto", contagemProduto.getId().toString());
                startActivityForResult(intent, ALTERAR_DELETAR_CONTAGEM_PRODUTO);
            }
        });

        String idContagem = getIntent().getStringExtra("contagem");
        controller = new InserirContagemProdutoController(this, conexaoBanco, idContagem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_adicionar_contagem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemHabilitarCampoQuant) {
            item.setChecked(!item.isChecked());
            controller.setCampoQuantHabilitado(item.isChecked());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TELA_LER_CODIGO_BARRAS:
                if(resultCode == ActivityBaseView.RESULT_OK) {
                    if (data != null) {
                        String codigo_lido = data.getStringExtra("codigo_lido");
                        if (codigo_lido != null) {
                            controller.processaCodigoLido(codigo_lido);
                        } else {
                            mensagemAoUsuario("Erro ao retornar código de barras lido. Tenta novamente");
                        }
                    } else {
                        mensagemAoUsuario("Erro ao retornar código de barras lido. Tenta novamente");
                    }
                }
                break;
            case TELA_SELECIONAR_PRODUTO:
                if(resultCode == ActivityBaseView.RESULT_OK) {
                    ProdutoGrade produtoGrade = (ProdutoGrade) data.getSerializableExtra("produtograde");
                    int quantidade = data.getIntExtra("quantidade", 0);
                    controller.carregaProdutoGrade(produtoGrade);
                    controller.cadastrar(quantidade);
                }
                break;
            case ALTERAR_DELETAR_CONTAGEM_PRODUTO:
                if(resultCode == ActivityBaseView.RESULT_OK) {
                    controller.atualizarProdutoGrades();
                }
                break;
            case VISUALIZAR_PRODUTO_GRADE_CONTAGEM:
                if (resultCode == Activity.RESULT_OK) {
                    ProdutoGrade produtoGrade = (ProdutoGrade) data.getSerializableExtra("produto_grade");
                    controller.carregaProdutoGrade(produtoGrade);
                    controller.executaCadastro();
                }
                break;
        }
    }

    public void showAlertaQuantidadeProduto() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.fragment_quantidade_produto_contagem_dialog, null, false);

        final EditText txtQuantidade = v.findViewById(R.id.txtQuantidade);
        txtQuantidade.setSelectAllOnFocus(true);
        txtQuantidade.requestFocus();

        AlertDialog.Builder alertaQuantidadeProduto = new AlertDialog.Builder(v.getContext());
        alertaQuantidadeProduto.setView(v);
        alertaQuantidadeProduto.setTitle("Informe a Quantidade");

        alertaQuantidadeProduto.setPositiveButton("Confirmar", (dialogInterface, i) -> {
            String txtQuant = txtQuantidade.getText().toString();
            int quantidade = 1;

            if (!txtQuant.isEmpty()) {
                quantidade = Integer.parseInt(txtQuant);
                if (quantidade < 1) {
                    mensagemAoUsuario("Informe Uma Quantidade Válida");
                    return;
                }
            }

            controller.cadastrar(quantidade);
        });

        alertaQuantidadeProduto.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            mensagemAoUsuario("A quantidade não foi informada. A contagem de produto não foi adicionada");
        });

        AlertDialog dialog = alertaQuantidadeProduto.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    public void setAlertProdutoGradeNaoEncontrado() {
        alertProdutoGradeNaoEncontrado = new AlertDialog.Builder(this);
        alertProdutoGradeNaoEncontrado.setTitle("Nenhuma grade encontrada para código de barras lido");
        alertProdutoGradeNaoEncontrado.setMessage("Nenhuma grade de produto foi encontrada para o código de barras lido. Deseja pesquisar ou cadastrar o produto manualmente?");
        alertProdutoGradeNaoEncontrado.setPositiveButton("Sim", (dialogInterface, i) -> btnAdicionarContagem.performClick());
        alertProdutoGradeNaoEncontrado.setNegativeButton("Não", ((dialogInterface, i) -> mensagemAoUsuario("Contagem de produto cancelada")));
    }

    public void showAlertaProdutoGradeNaoEncontrado() {
        alertProdutoGradeNaoEncontrado.show();
    }

    @Override
    public void abrirVisualizarProdutoGradeContagem(String codigo) {
        Intent intent = new Intent(getContext(), ListarProdutoGradePorCodigoForResult.class);
        intent.putExtra("codigo", codigo);
        startActivityForResult(intent, VISUALIZAR_PRODUTO_GRADE_CONTAGEM);
    }

    @Override
    protected void onDestroy() {
        if (conexaoBanco != null)
            conexaoBanco.close();
        super.onDestroy();
    }

    @Override
    public void limparCampos() {

    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void aposCadastro(Object... args) {

    }

    @Override
    public void focoEmViewInicial() {

    }

    @Override
    public void setListViewAdapter(ListAdapter adapter) {
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                //Move para o último item da lista sempre que o adapter for modificado
                int lastIndex = adapter.getCount() - 1;
                listViewContagemProduto.smoothScrollToPosition(lastIndex);
            }
        });
        listViewContagemProduto.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
