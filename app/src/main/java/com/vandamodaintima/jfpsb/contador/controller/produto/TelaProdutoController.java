package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.ContentResolver;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.Excel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProduto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

public class TelaProdutoController {
    private ProdutoModel produtoModel;
    private enum Headers {
        COD_BARRA("Código de Barra"),
        FORNECEDOR("Fornecedor"),
        COD_BARRA_FORNECEDOR("Códigos de Barra de FornecedorModel"),
        DESCRICAO("Descrição"),
        MARCA("MarcaModel"),
        PRECO("Preço");

        public String texto;

        Headers(String texto) {
            this.texto = texto;
        }
    }

    public TelaProdutoController(ConexaoBanco conexaoBanco) {
        produtoModel = new ProdutoModel(conexaoBanco);
    }

    public Boolean importarDeArquivoExcel(TelaProduto.ImportarProdutoAsyncTask importarProdutoAsyncTask, final ContentResolver contentResolver) {
        /*ConexaoBanco conexaoBanco = null;
        ArrayList<ProdutoModel> produtos = new ArrayList<>();
        InputStream inputStream = null;

        importarProdutoAsyncTask.publish("Iniciando Cadastro");

        try {
            produtoModel = new ProdutoModel(conexaoBanco);
            marcaModel = new MarcaModel(conexaoBanco);
            fornecedorModel = new FornecedorModel(conexaoBanco);

            inputStream = Arquivo.getInputStreamFromUri(contentResolver, importarProdutoAsyncTask.getUri());

            if (inputStream == null) {
                throw new Exception("InputStream de arquivo Excel escolhido voltou nula");
            }

            ArquivoExcel arquivoExcel = new ArquivoExcel(inputStream);

            Row headerRow = arquivoExcel.getPlanilha().getRow(0);

            if (headerRow.getPhysicalNumberOfCells() != 6) {
                throw new Exception("Planilha Configurada de Forma Errada");
            }

            if (!headerRow.getCell(Headers.COD_BARRA.ordinal()).getStringCellValue().equals(Headers.COD_BARRA.texto)) {
                throw new Exception("Cabeçalho da Primeira Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.FORNECEDOR.ordinal()).getStringCellValue().equals(Headers.FORNECEDOR.texto)) {
                throw new Exception("Cabeçalho da Segunda Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.COD_BARRA_FORNECEDOR.ordinal()).getStringCellValue().equals(Headers.COD_BARRA_FORNECEDOR.texto)) {
                throw new Exception("Cabeçalho da Terceira Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.DESCRICAO.ordinal()).getStringCellValue().equals(Headers.DESCRICAO.texto)) {
                throw new Exception("Cabeçalho da Quarta Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.MARCA.ordinal()).getStringCellValue().equals(Headers.MARCA.texto)) {
                throw new Exception("Cabeçalho da Quinta Coluna Está Errado");
            }

            if (!headerRow.getCell(Headers.PRECO.ordinal()).getStringCellValue().equals(Headers.PRECO.texto)) {
                throw new Exception("Cabeçalho da Sexta Coluna Está Errado");
            }

            int rows = arquivoExcel.getPlanilha().getPhysicalNumberOfRows();

            importarProdutoAsyncTask.publish(rows + " Produto(s) Encontrado(s)");

            for (int i = 1; i < rows; i++) {
                Row row = arquivoExcel.getPlanilha().getRow(i);

                ProdutoModel produtoModel = new ProdutoModel(conexaoBanco);

                Cell cod_barra = row.getCell(Headers.COD_BARRA.ordinal());
                Cell fornecedor = row.getCell(Headers.FORNECEDOR.ordinal());
                Cell cod_barra_fornecedor = row.getCell(Headers.COD_BARRA_FORNECEDOR.ordinal());
                Cell descricao = row.getCell(Headers.DESCRICAO.ordinal());
                Cell marca = row.getCell(Headers.MARCA.ordinal());
                Cell preco = row.getCell(Headers.PRECO.ordinal());

                if (!isCellEmpty(cod_barra)) {
                    if (cod_barra.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        produtoModel.setCod_barra(String.format(Locale.ENGLISH, "%.0f", cod_barra.getNumericCellValue())); //Retirando o .0
                    } else if (cod_barra.getCellType() == Cell.CELL_TYPE_STRING) {
                        produtoModel.setCod_barra(cod_barra.getStringCellValue());
                    } else {
                        throw new Exception("Formato de Código de Barras Errado. A Célula Precisa Ser do Tipo \"Texto\" ou \"Número\"");
                    }
                } else {
                    throw new Exception("A Célula de Código de Barras Não Pode Estar Vazia");
                }

                if (!isCellEmpty(fornecedor)) {
                    if (fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                        FornecedorModel f = fornecedorModel.listarPorIdOuNome(fornecedor.getStringCellValue());

                        if (f != null) {
                            produtoModel.setFornecedor(f);
                        } else {
                            throw new Exception("Fornecedor " + fornecedor.getStringCellValue() + " Informado Não Encontrado");
                        }
                    } else {
                        throw new Exception("Formato de CNPJ de Fornecedor Está Errado. A Célula Precisa ser do Tipo \"Texto\"");
                    }
                } else {
                    produtoModel.setFornecedor(null);
                }

                if (!isCellEmpty(cod_barra_fornecedor)) {
                    String codigosCell = null;

                    if (cod_barra_fornecedor.getCellType() == Cell.CELL_TYPE_STRING) {
                        codigosCell = cod_barra_fornecedor.getStringCellValue().replace(" ", "");
                    }

                    if (cod_barra_fornecedor.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        codigosCell = String.valueOf(cod_barra_fornecedor.getNumericCellValue()).replace(" ", "");
                    }

                    String[] codigos = codigosCell.split(",");

                    produtoModel.setCod_barra_fornecedor(new ArrayList<>(Arrays.asList(codigos)));
                }

                if (!isCellEmpty(descricao)) {
                    if (descricao.getCellType() == Cell.CELL_TYPE_STRING) {
                        produtoModel.setDescricao(descricao.getStringCellValue());
                    } else {
                        throw new Exception("Formato de Descrição do ProdutoModel Está Errado. A Célula Precisa ser do Tipo \"Texto\"");
                    }
                } else {
                    throw new Exception("A Célula de Descrição Não Pode Estar Vazia");
                }

                if (!isCellEmpty(marca)) {
                    if (marca.getCellType() == Cell.CELL_TYPE_STRING) {
                        MarcaModel m = marcaModel.listarPorNome(marca.getStringCellValue()).get(0);

                        if (m != null) {
                            produtoModel.setMarca(m);
                        } else {
                            throw new Exception("MarcaModel " + marca.getStringCellValue() + " Não Encontrada");
                        }
                    } else {
                        throw new Exception("Formato de MarcaModel do ProdutoModel Está Errado. A Célula Precisa ser do Tipo \"Texto\"");
                    }
                } else {
                    produtoModel.setMarca(null);
                }

                if (!isCellEmpty(preco)) {
                    if (preco.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        produtoModel.setPreco(preco.getNumericCellValue());
                    } else {
                        throw new Exception("Formato de Preço do ProdutoModel Está Errado. A Célula Precisa ser do Tipo \"Número\"");
                    }
                } else {
                    throw new Exception("A Célula de Preço Não Pode Estar Vazia");
                }

                if (produtoModel.getDescricao() != null && produtoModel.getCod_barra() != null && produtoModel.getPreco() != null) {
                    produtos.add(produtoModel);
                }
            }

            Boolean result = produtoModel.importarDeExcel(produtos, importarProdutoAsyncTask);

            if (result) {
                importarProdutoAsyncTask.publish("Produtos Cadastrados com Sucesso");
                return true;
            }
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
            importarProdutoAsyncTask.publish(e.getMessage());
        } finally {
            conexaoBanco.close();

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("Contador", "Ao tentar fechar InputStream: " + e.getMessage(), e);
                }
            }
        }*/

        return false;
    }

    public void exportarProdutosEmExcel(TelaProduto.ExportarProdutoEmExcel exportarProdutoEmExcel, String diretorio) {
        new Excel(new ProdutoStrategy()).exportar(diretorio, produtoModel.listar());
        /*exportarProdutoEmExcel.publish("Iniciando Exportação. Aguarde");

        Date dataAtual = new Date();
        ArquivoExcel arquivoExcel = new ArquivoExcel();
        int rowIndex = 1; //Inicia após cabeçalho
        OutputStream outputStream = null;

        setCabecalhoProduto(arquivoExcel);

        CellStyle cellStyle = CellStylePadrao(arquivoExcel);

        ArrayList<ProdutoModel> produtos = produtoModel.listar();

        try {
            if (produtos.size() == 0)
                throw new Exception("Não Há Produtos Cadastrados");

            Row[] rows = new Row[produtos.size()];

            for (int i = rowIndex; i <= rows.length; i++) {
                Row row = arquivoExcel.getPlanilha().createRow(i);

                for (int j = 0; j < Headers.values().length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(cellStyle);
                }

                rows[i - 1] = row;
            }

            for (int i = 0; i < rows.length; i++) {
                ProdutoModel produtoModel = produtos.get(i);

                rows[i].getCell(Headers.COD_BARRA.ordinal()).setCellValue(produtoModel.getCod_barra());

                if (produtoModel.getFornecedor() != null) {
                    rows[i].getCell(Headers.FORNECEDOR.ordinal()).setCellValue(produtoModel.getFornecedor().getNome());
                } else {
                    rows[i].getCell(Headers.FORNECEDOR.ordinal()).setCellValue("Não Possui");
                }

                String codbarrafornecedor = "";

                for (int j = 0; j < produtoModel.getCod_barra_fornecedor().size(); j++) {
                    codbarrafornecedor += produtoModel.getCod_barra_fornecedor().get(j);

                    if (j != produtoModel.getCod_barra_fornecedor().size() - 1) {
                        codbarrafornecedor += ", ";
                    }
                }

                rows[i].getCell(Headers.COD_BARRA_FORNECEDOR.ordinal()).setCellValue(codbarrafornecedor);

                rows[i].getCell(Headers.DESCRICAO.ordinal()).setCellValue(produtoModel.getDescricao());

                if (produtoModel.getMarca() != null) {
                    rows[i].getCell(Headers.MARCA.ordinal()).setCellValue(produtoModel.getMarca().getNome());
                } else {
                    rows[i].getCell(Headers.MARCA.ordinal()).setCellValue("Não Possui");
                }


                rows[i].getCell(Headers.PRECO.ordinal()).setCellValue(produtoModel.getPreco());

                if (i % 100 == 0 && i > 99) {
                    exportarProdutoEmExcel.publish(i + " Produtos Já Listados");
                }
            }

            arquivoExcel.getPlanilha().setColumnWidth(Headers.COD_BARRA.ordinal(), 25 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(Headers.COD_BARRA_FORNECEDOR.ordinal(), 70 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(Headers.FORNECEDOR.ordinal(), 45 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(Headers.MARCA.ordinal(), 40 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(Headers.DESCRICAO.ordinal(), 45 * 256);
            arquivoExcel.getPlanilha().setColumnWidth(Headers.PRECO.ordinal(), 25 * 256);

            String nomeArquivo = "Produtos " + new SimpleDateFormat("dd-MM-yyyy HH.mm.ss").format(dataAtual) + ".xlsx";

            File arquivo = new File(diretorio, nomeArquivo);

            outputStream = new FileOutputStream(arquivo.getAbsolutePath());

            exportarProdutoEmExcel.publish("Escrevendo Arquivo Em Memória");

            arquivoExcel.getPastaTrabalho().write(outputStream);

            exportarProdutoEmExcel.publish("Produtos Exportados Com Sucesso");

            return;
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        exportarProdutoEmExcel.publish("Erro ao Exportar Produtos");*/
    }

    private boolean isCellEmpty(Cell cell) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return true;
        }

        if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()) {
            return true;
        }

        return false;
    }

    private void setCabecalhoProduto() {
        /*CellStyle cabecalhoStyle = CellStylePadrao(arquivoExcel);

        Font font = arquivoExcel.getPastaTrabalho().createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);

        cabecalhoStyle.setFont(font);
        cabecalhoStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

        Row cabecalho = arquivoExcel.getPlanilha().createRow(0);

        for (Headers header : Headers.values()) {
            Cell cell = cabecalho.createCell(header.ordinal());

            cell.setCellValue(header.texto);
            cell.setCellStyle(cabecalhoStyle);
        }*/
    }

    private CellStyle CellStylePadrao() {
        /*CellStyle cellStyle = arquivoExcel.getPastaTrabalho().createCellStyle();

        Font font = arquivoExcel.getPastaTrabalho().createFont();
        font.setFontHeightInPoints((short) 12);

        cellStyle.setFont(font);

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        return cellStyle;*/

        return null;
    }
}
