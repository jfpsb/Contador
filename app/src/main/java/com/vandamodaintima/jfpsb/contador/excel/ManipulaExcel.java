package com.vandamodaintima.jfpsb.contador.excel;

import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.TelaProduto;
import com.vandamodaintima.jfpsb.contador.tela.manager.PesquisarProduto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by jfpsb on 16/02/2018.
 */

public class ManipulaExcel {
    private static DAOProduto daoProduto;

    public static void adicionaProdutosDePlanilhaParaBD(final TelaProduto telaProduto, ConexaoBanco conn) {
        Produto[] produtos = null;

        daoProduto = new DAOProduto(conn.conexao());

        try {
            telaProduto.runOnUiThread(TelaProduto.msgTxtProgressStatus("Procurando arquivo 'produtos.xlsx'"));

            File file = new File("/data/data/com.vandamodaintima.jfpsb.contador/files/produtos.xlsx");

            InputStream inputStream = new FileInputStream(file);

            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

            FormulaEvaluator formulaEvaluator = xssfWorkbook.getCreationHelper().createFormulaEvaluator();

            int rows = xssfSheet.getPhysicalNumberOfRows();

            Log.i("Contador", "LINHAS :::: " + rows);

            produtos = new Produto[rows - 1];

            Row auxRow = xssfSheet.getRow(0);

            if(auxRow.getPhysicalNumberOfCells() != 3) {
                throw new Exception("Há um erro na planilha. A quantidade de colunas está errada! Quantidade correta: 3");
            }

            for(int i = 1; i < rows; i++) {
                Row row = xssfSheet.getRow(i);

                int cells = row.getPhysicalNumberOfCells();

                Produto produto = new Produto();

                for(int j = 0; j < cells; j++) {
                    try {
                        Cell cell = row.getCell(j);

                        CellValue cellValue = formulaEvaluator.evaluate(cell);

                        switch (cellValue.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                produto.setPreco(cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                if(cell.getColumnIndex() == 0) {
                                    produto.setCod_barra(cell.getStringCellValue());
                                } else if (cell.getColumnIndex() == 1) {
                                    produto.setDescricao(cell.getStringCellValue());
                                }
                                break;
                        }

                        produto.setFornecedor(null);

                        produtos[i - 1] = produto;
                    } catch (NullPointerException npe) {
                        Toast.makeText(telaProduto.getApplicationContext(), npe.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("Contador", npe.getMessage());
                    }
                }

                telaProduto.runOnUiThread(TelaProduto.msgTxtProgressStatus("Produto " + i + " lido de planilha"));
            }

            telaProduto.runOnUiThread(TelaProduto.msgTxtProgressStatus("Tudo lido"));

            daoProduto.inserirVarios(telaProduto, produtos);

            telaProduto.runOnUiThread(TelaProduto.msgTxtProgressStatus("Produtos Cadastrados"));

            telaProduto.runOnUiThread(toastLocal(telaProduto, "Produtos contidos em planilha de Excel foram cadastrados com sucesso!"));

            telaProduto.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PesquisarProduto.populaListView();
                }
            });

        }catch (FileNotFoundException fnfe) {
            telaProduto.runOnUiThread(toastLocal(telaProduto, "O arquivo não foi encontrado. O arquivo deve estar na pasta 'files' no diretório do app com nome 'produtos.xlsx'"));
            Log.i("Contador", fnfe.getMessage());
        }
        catch (Exception e) {
            telaProduto.runOnUiThread(toastLocal(telaProduto, e.getMessage()));
            Log.i("Contador", e.getMessage());
        }
    }

    private static Runnable toastLocal(final TelaProduto telaProduto, final String msg) {
        return new Runnable() {
            @Override
            public void run() {
                Toast.makeText(telaProduto, msg, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
