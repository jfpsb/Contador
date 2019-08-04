package com.vandamodaintima.jfpsb.contador.excel;

import android.os.AsyncTask;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by jfpsb on 16/02/2018.
 */

public class ManipulaExcel {
    //private static DAOFornecedor daoFornecedor;
    //private static DAOProduto daoProduto;
    //private static DAOMarca daoMarca;
    private AsyncTask task;
    private ConexaoBanco conexaoBanco;

    private enum Headers {
        COD_BARRA("Código de Barra"),
        FORNECEDOR("FornecedorModel (CNPJ)"),
        COD_BARRA_FORNECEDOR("Códigos de Barra de FornecedorModel"),
        DESCRICAO("Descrição"),
        MARCA("MarcaModel (Nome)"),
        PRECO("Preço");

        public String texto;

        Headers(String texto) {
            this.texto = texto;
        }
    }



    public ManipulaExcel(ConexaoBanco conexao) {
        conexaoBanco = conexao;
    }

    public ManipulaExcel(AsyncTask task, ConexaoBanco conexao) {
        this.task = task;
        conexaoBanco = conexao;
    }

    private boolean isCellEmpty(Cell cell) {
        if(cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }

        if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()) {
            return true;
        }

        return false;
    }

    //TODO: Tentar simplificar. Estilizar planilha
    public boolean ExportaContagem(ContagemModel contagem, String diretorio) {

//        Date dataAtual = new Date();
//        ArquivoExcel arquivoExcel = new ArquivoExcel();
//        int rowIndex = 1; //Inicia após cabeçalho
//        OutputStream outputStream = null;
//
//        setCabecalhoContagem(arquivoExcel);
//
//        CellStyle cellStyle = CellStylePadrao(arquivoExcel);

        //ArrayList<ContagemProdutoModel> contagemproduto = contagemProdutoManager.listarPorContagem(contagem);

//        try {
//            if (contagemproduto.size() == 0)
//                throw new Exception("Não Há Produtos na ContagemModel");
//
//            for (int i = rowIndex; i <= contagemproduto.size(); i++) {
//                Row row = arquivoExcel.getPlanilha().createRow(i);
//
//                ContagemProdutoModel contagemProduto = contagemproduto.get(i - 1);
//
//                Cell cod_barra = row.createCell(0);
//                cod_barra.setCellValue(contagemProduto.getProduto().getCod_barra());
//
//                Cell descricao = row.createCell(1);
//                descricao.setCellValue(contagemProduto.getProduto().getDescricao());
//
//                Cell quant = row.createCell(2);
//                quant.setCellValue(contagemProduto.getQuant());
//
//                cod_barra.setCellStyle(cellStyle);
//                descricao.setCellStyle(cellStyle);
//                quant.setCellStyle(cellStyle);
//            }
//
//            arquivoExcel.getPlanilha().setColumnWidth(0, 23 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(1, 65 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(2, 18 * 256);
//
//            String nomeArquivo = "ContagemModel - " + contagem.getLoja().getNome() + " - " + TrataDisplayData.getDataFormatoBD(dataAtual) + ".xlsx";
//
//            File arquivo = new File(diretorio, nomeArquivo);
//
//            outputStream = new FileOutputStream(arquivo.getAbsolutePath());
//
//            arquivoExcel.getPastaTrabalho().write(outputStream);
//
//            return true;
//        } catch (Exception e) {
//            Log.e("Contador", e.getMessage(), e);
//        } finally {
//            try {
//                if (outputStream != null) {
//                    outputStream.flush();
//                    outputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return false;
    }

    //TODO: Tentar simplificar. Estilizar planilha
    public boolean ExportaFornecedor(String diretorio) {
//        fornecedorManager = new FornecedorManager(conexaoBanco);
//
//        Date dataAtual = new Date();
//        ArquivoExcel arquivoExcel = new ArquivoExcel();
//        int rowIndex = 1; //Inicia após cabeçalho
//        OutputStream outputStream = null;
//
//        setCabecalhoFornecedor(arquivoExcel);
//
//        CellStyle cellStyle = CellStylePadrao(arquivoExcel);
//
//        ArrayList<FornecedorModel> fornecedores = fornecedorManager.listarCursor();
//
//        try {
//            if (fornecedores.size() == 0)
//                throw new Exception("Não Há Fornecedores Cadastrados");
//
//            for (int i = rowIndex; i <= fornecedores.size(); i++) {
//                Row row = arquivoExcel.getPlanilha().createRow(i);
//
//                FornecedorModel fornecedor = fornecedores.get(i - 1);
//
//                Cell cnpj = row.createCell(0);
//                cnpj.setCellValue(fornecedor.getCnpj());
//
//                Cell nome = row.createCell(1);
//                nome.setCellValue(fornecedor.getNome());
//
//                Cell fantasia = row.createCell(2);
//                fantasia.setCellValue(fornecedor.getFantasia());
//
//                cnpj.setCellStyle(cellStyle);
//                nome.setCellStyle(cellStyle);
//                fantasia.setCellStyle(cellStyle);
//            }
//
//            arquivoExcel.getPlanilha().setColumnWidth(0, 25 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(1, 70 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(2, 40 * 256);
//
//            String nomeArquivo = "Fornecedores " + TrataDisplayData.getDataFormatoBD(dataAtual) + ".xlsx";
//
//            File arquivo = new File(diretorio, nomeArquivo);
//
//            outputStream = new FileOutputStream(arquivo.getAbsolutePath());
//
//            arquivoExcel.getPastaTrabalho().write(outputStream);
//
//            return true;
//        } catch (Exception e) {
//            Log.e("Contador", e.getMessage(), e);
//        } finally {
//            try {
//                if (outputStream != null) {
//                    outputStream.flush();
//                    outputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return false;
    }

    //TODO: Tentar simplificar. Estilizar planilha
    public boolean ExportaProduto(String diretorio) {
//        produtoManager = new ProdutoManager(conexaoBanco);
//
//        Date dataAtual = new Date();
//        ArquivoExcel arquivoExcel = new ArquivoExcel();
//        int rowIndex = 1; //Inicia após cabeçalho
//        OutputStream outputStream = null;
//
//        setCabecalhoProduto(arquivoExcel);
//
//        CellStyle cellStyle = CellStylePadrao(arquivoExcel);
//
//        ArrayList<ProdutoModel> produtos = produtoManager.listarCursor();
//
//        try {
//            if (produtos.size() == 0)
//                throw new Exception("Não Há Produtos Cadastrados");
//
//            Row[] rows = new Row[produtos.size()];
//
//            for(int i = rowIndex; i <= rows.length; i++) {
//                Row row = arquivoExcel.getPlanilha().createRow(i);
//
//                for(int j = 0; j < ProdutoModel.getHeaders().length; i++) {
//                    Cell cell = row.createCell(j);
//                    cell.setCellStyle(cellStyle);
//                }
//
//                rows[i - 1] = row;
//            }
//
//            for (int i = 0; i < rows.length; i++) {
//                ProdutoModel produtoModel = produtos.get(i);
//
//                rows[i].getCell(0).setCellValue(produtoModel.getCod_barra());
//                //TODO: lista de codigos
//                rows[i].getCell(2).setCellValue(produtoModel.getFornecedor().getNome());
//                rows[i].getCell(3).setCellValue(produtoModel.getMarca().getNome());
//                rows[i].getCell(4).setCellValue(produtoModel.getDescricao());
//                rows[i].getCell(5).setCellValue(produtoModel.getPreco());
//            }
//
//            arquivoExcel.getPlanilha().setColumnWidth(0, 25 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(1, 70 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(2, 40 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(3, 40 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(4, 40 * 256);
//            arquivoExcel.getPlanilha().setColumnWidth(5, 40 * 256);
//
//            String nomeArquivo = "Produtos " + TrataDisplayData.getDataFormatoBD(dataAtual) + ".xlsx";
//
//            File arquivo = new File(diretorio, nomeArquivo);
//
//            outputStream = new FileOutputStream(arquivo.getAbsolutePath());
//
//            arquivoExcel.getPastaTrabalho().write(outputStream);
//
//            return true;
//        } catch (Exception e) {
//            Log.e("Contador", e.getMessage(), e);
//        } finally {
//            try {
//                if (outputStream != null) {
//                    outputStream.flush();
//                    outputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return false;
    }

    private void setCabecalhoContagem(ArquivoExcel arquivoExcel) {
//        CellStyle cabecalhoStyle = CellStylePadrao(arquivoExcel);
//
//        Font font = arquivoExcel.getPastaTrabalho().createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 16);
//        font.setBold(true);
//
//        cabecalhoStyle.setFont(font);
//        cabecalhoStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
//
//        Row cabecalho = arquivoExcel.getPlanilha().createRow(0);
//
//        Cell cab1 = cabecalho.createCell(0);
//        cab1.setCellValue("Cód. de Barra");
//
//        Cell cab2 = cabecalho.createCell(1);
//        cab2.setCellValue("Descrição");
//
//        Cell cab3 = cabecalho.createCell(2);
//        cab3.setCellValue("Quantidade");
//
//        cab1.setCellStyle(cabecalhoStyle);
//        cab2.setCellStyle(cabecalhoStyle);
//        cab3.setCellStyle(cabecalhoStyle);
    }

    private void setCabecalhoFornecedor(ArquivoExcel arquivoExcel) {
//        CellStyle cabecalhoStyle = CellStylePadrao(arquivoExcel);
//
//        Font font = arquivoExcel.getPastaTrabalho().createFont();
//        font.setFontName("Arial");
//        font.setFontHeightInPoints((short) 16);
//        font.setBold(true);
//
//        cabecalhoStyle.setFont(font);
//        cabecalhoStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
//
//        Row cabecalho = arquivoExcel.getPlanilha().createRow(0);
//
//        Cell cab1 = cabecalho.createCell(0);
//        cab1.setCellValue("CNPJ");
//
//        Cell cab2 = cabecalho.createCell(1);
//        cab2.setCellValue("Nome");
//
//        Cell cab3 = cabecalho.createCell(2);
//        cab3.setCellValue("Nome Fantasia");
//
//        cab1.setCellStyle(cabecalhoStyle);
//        cab2.setCellStyle(cabecalhoStyle);
//        cab3.setCellStyle(cabecalhoStyle);
    }
}