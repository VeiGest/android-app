package com.ipleiria.veigest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.veigest.sdk.models.Maintenance;
import com.veigest.sdk.models.Vehicle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utilitário para geração de relatórios em PDF
 * ADENDA do projeto - 5% da nota
 */
public class PDFGenerator {

    private static final String TAG = "PDFGenerator";
    
    // Dimensões da página A4 em pontos (72 pontos = 1 polegada)
    private static final int PAGE_WIDTH = 595;  // A4 width
    private static final int PAGE_HEIGHT = 842; // A4 height
    private static final int MARGIN = 40;
    private static final int LINE_HEIGHT = 20;
    
    private final Context context;
    private final Paint titlePaint;
    private final Paint headerPaint;
    private final Paint textPaint;
    private final Paint linePaint;

    public PDFGenerator(Context context) {
        this.context = context;
        
        // Configurar pincel para título
        titlePaint = new Paint();
        titlePaint.setColor(Color.parseColor("#11C7A5"));
        titlePaint.setTextSize(24);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setAntiAlias(true);
        
        // Configurar pincel para cabeçalhos
        headerPaint = new Paint();
        headerPaint.setColor(Color.BLACK);
        headerPaint.setTextSize(14);
        headerPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        headerPaint.setAntiAlias(true);
        
        // Configurar pincel para texto normal
        textPaint = new Paint();
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextSize(12);
        textPaint.setAntiAlias(true);
        
        // Configurar pincel para linhas
        linePaint = new Paint();
        linePaint.setColor(Color.LTGRAY);
        linePaint.setStrokeWidth(1);
    }

    /**
     * Gera um relatório PDF com a lista de veículos
     * @param vehicles Lista de veículos
     * @return Arquivo PDF gerado ou null se falhar
     */
    public File generateVehicleReport(List<Vehicle> vehicles) {
        PdfDocument document = new PdfDocument();
        
        try {
            int pageNumber = 1;
            int yPosition = MARGIN;
            
            // Criar primeira página
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            
            // Desenhar cabeçalho
            yPosition = drawHeader(canvas, "Relatório de Veículos", yPosition);
            yPosition += 10;
            
            // Data do relatório
            String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            canvas.drawText("Gerado em: " + dateStr, MARGIN, yPosition, textPaint);
            yPosition += LINE_HEIGHT * 2;
            
            // Desenhar linha separadora
            canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint);
            yPosition += LINE_HEIGHT;
            
            // Desenhar cabeçalhos da tabela
            canvas.drawText("Matrícula", MARGIN, yPosition, headerPaint);
            canvas.drawText("Marca/Modelo", MARGIN + 100, yPosition, headerPaint);
            canvas.drawText("Ano", MARGIN + 280, yPosition, headerPaint);
            canvas.drawText("Status", MARGIN + 340, yPosition, headerPaint);
            yPosition += 5;
            canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint);
            yPosition += LINE_HEIGHT;
            
            // Desenhar dados dos veículos
            for (Vehicle vehicle : vehicles) {
                // Verificar se precisa de nova página
                if (yPosition > PAGE_HEIGHT - MARGIN * 2) {
                    document.finishPage(page);
                    pageNumber++;
                    pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create();
                    page = document.startPage(pageInfo);
                    canvas = page.getCanvas();
                    yPosition = MARGIN + LINE_HEIGHT;
                }
                
                String matricula = vehicle.getLicensePlate() != null ? vehicle.getLicensePlate() : "-";
                String marcaModelo = (vehicle.getBrand() != null ? vehicle.getBrand() : "") + " " +
                        (vehicle.getModel() != null ? vehicle.getModel() : "");
                String ano = vehicle.getYear() > 0 ? String.valueOf(vehicle.getYear()) : "-";
                String status = vehicle.getStatusLabel() != null ? vehicle.getStatusLabel() : 
                        (vehicle.getStatus() != null ? vehicle.getStatus() : "-");
                
                canvas.drawText(matricula, MARGIN, yPosition, textPaint);
                canvas.drawText(marcaModelo.trim(), MARGIN + 100, yPosition, textPaint);
                canvas.drawText(ano, MARGIN + 280, yPosition, textPaint);
                
                // Status com cor
                Paint statusPaint = new Paint(textPaint);
                if ("active".equalsIgnoreCase(vehicle.getStatus()) || "ativo".equalsIgnoreCase(status)) {
                    statusPaint.setColor(Color.parseColor("#4CAF50"));
                } else if ("inactive".equalsIgnoreCase(vehicle.getStatus()) || "inativo".equalsIgnoreCase(status)) {
                    statusPaint.setColor(Color.parseColor("#F44336"));
                } else {
                    statusPaint.setColor(Color.parseColor("#FF9800"));
                }
                canvas.drawText(status, MARGIN + 340, yPosition, statusPaint);
                
                yPosition += LINE_HEIGHT;
            }
            
            // Resumo
            yPosition += LINE_HEIGHT;
            canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint);
            yPosition += LINE_HEIGHT;
            canvas.drawText("Total de veículos: " + vehicles.size(), MARGIN, yPosition, headerPaint);
            
            // Rodapé
            yPosition = PAGE_HEIGHT - MARGIN;
            canvas.drawText("VeiGest - Sistema de Gestão de Veículos", MARGIN, yPosition, textPaint);
            
            document.finishPage(page);
            
            // Salvar arquivo
            return savePdfToFile(document, "veiculos_" + System.currentTimeMillis() + ".pdf");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao gerar PDF de veículos", e);
            return null;
        } finally {
            document.close();
        }
    }

    /**
     * Gera um relatório PDF com a lista de manutenções
     * @param maintenances Lista de manutenções
     * @return Arquivo PDF gerado ou null se falhar
     */
    public File generateMaintenanceReport(List<Maintenance> maintenances) {
        PdfDocument document = new PdfDocument();
        
        try {
            int pageNumber = 1;
            int yPosition = MARGIN;
            
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            
            // Desenhar cabeçalho
            yPosition = drawHeader(canvas, "Relatório de Manutenções", yPosition);
            yPosition += 10;
            
            // Data do relatório
            String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            canvas.drawText("Gerado em: " + dateStr, MARGIN, yPosition, textPaint);
            yPosition += LINE_HEIGHT * 2;
            
            // Linha separadora
            canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint);
            yPosition += LINE_HEIGHT;
            
            // Cabeçalhos da tabela
            canvas.drawText("ID", MARGIN, yPosition, headerPaint);
            canvas.drawText("Tipo", MARGIN + 50, yPosition, headerPaint);
            canvas.drawText("Data", MARGIN + 180, yPosition, headerPaint);
            canvas.drawText("Custo", MARGIN + 280, yPosition, headerPaint);
            canvas.drawText("Status", MARGIN + 360, yPosition, headerPaint);
            yPosition += 5;
            canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint);
            yPosition += LINE_HEIGHT;
            
            double custoTotal = 0;
            
            // Desenhar dados das manutenções
            for (Maintenance maintenance : maintenances) {
                if (yPosition > PAGE_HEIGHT - MARGIN * 2) {
                    document.finishPage(page);
                    pageNumber++;
                    pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create();
                    page = document.startPage(pageInfo);
                    canvas = page.getCanvas();
                    yPosition = MARGIN + LINE_HEIGHT;
                }
                
                String id = String.valueOf(maintenance.getId());
                String tipo = maintenance.getTypeLabel() != null ? maintenance.getTypeLabel() : 
                        (maintenance.getType() != null ? maintenance.getType() : "-");
                String data = maintenance.getDate() != null ? maintenance.getDate() : "-";
                String custo = String.format(Locale.getDefault(), "€%.2f", maintenance.getCost());
                String status = maintenance.getStatusLabel() != null ? maintenance.getStatusLabel() : 
                        (maintenance.getStatus() != null ? maintenance.getStatus() : "-");
                
                custoTotal += maintenance.getCost();
                
                canvas.drawText(id, MARGIN, yPosition, textPaint);
                canvas.drawText(tipo.length() > 15 ? tipo.substring(0, 15) + "..." : tipo, MARGIN + 50, yPosition, textPaint);
                canvas.drawText(data, MARGIN + 180, yPosition, textPaint);
                canvas.drawText(custo, MARGIN + 280, yPosition, textPaint);
                
                Paint statusPaint = new Paint(textPaint);
                if ("completed".equalsIgnoreCase(maintenance.getStatus()) || "concluida".equalsIgnoreCase(status)) {
                    statusPaint.setColor(Color.parseColor("#4CAF50"));
                } else if ("pending".equalsIgnoreCase(maintenance.getStatus()) || "pendente".equalsIgnoreCase(status)) {
                    statusPaint.setColor(Color.parseColor("#FF9800"));
                } else {
                    statusPaint.setColor(Color.parseColor("#2196F3"));
                }
                canvas.drawText(status, MARGIN + 360, yPosition, statusPaint);
                
                yPosition += LINE_HEIGHT;
            }
            
            // Resumo
            yPosition += LINE_HEIGHT;
            canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint);
            yPosition += LINE_HEIGHT;
            canvas.drawText("Total de manutenções: " + maintenances.size(), MARGIN, yPosition, headerPaint);
            yPosition += LINE_HEIGHT;
            canvas.drawText("Custo total: " + String.format(Locale.getDefault(), "€%.2f", custoTotal), MARGIN, yPosition, headerPaint);
            
            // Rodapé
            yPosition = PAGE_HEIGHT - MARGIN;
            canvas.drawText("VeiGest - Sistema de Gestão de Veículos", MARGIN, yPosition, textPaint);
            
            document.finishPage(page);
            
            return savePdfToFile(document, "manutencoes_" + System.currentTimeMillis() + ".pdf");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao gerar PDF de manutenções", e);
            return null;
        } finally {
            document.close();
        }
    }

    /**
     * Desenha o cabeçalho do relatório
     */
    private int drawHeader(Canvas canvas, String title, int yPosition) {
        // Logo/Nome da app
        canvas.drawText("VeiGest", MARGIN, yPosition + 24, titlePaint);
        
        // Título do relatório
        yPosition += 50;
        canvas.drawText(title, MARGIN, yPosition, headerPaint);
        
        return yPosition + LINE_HEIGHT;
    }

    /**
     * Salva o documento PDF num arquivo
     */
    private File savePdfToFile(PdfDocument document, String filename) throws IOException {
        // Criar diretório de documentos
        File documentsDir = new File(context.getFilesDir(), "documents");
        if (!documentsDir.exists()) {
            documentsDir.mkdirs();
        }
        
        File pdfFile = new File(documentsDir, filename);
        
        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            document.writeTo(fos);
        }
        
        Log.d(TAG, "PDF salvo em: " + pdfFile.getAbsolutePath());
        return pdfFile;
    }

    /**
     * Abre o PDF no visualizador padrão
     */
    public void openPdf(File pdfFile) {
        try {
            Uri uri = FileProvider.getUriForFile(context, 
                    context.getPackageName() + ".fileprovider", pdfFile);
            
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            context.startActivity(Intent.createChooser(intent, "Abrir PDF com..."));
        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir PDF", e);
            Toast.makeText(context, "Não foi possível abrir o PDF", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Envia o PDF por email
     */
    public void sendPdfByEmail(File pdfFile, String subject, String body, String... recipients) {
        try {
            Uri uri = FileProvider.getUriForFile(context, 
                    context.getPackageName() + ".fileprovider", pdfFile);
            
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("application/pdf");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            
            if (recipients != null && recipients.length > 0) {
                emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
            }
            
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            context.startActivity(Intent.createChooser(emailIntent, "Enviar relatório por..."));
        } catch (Exception e) {
            Log.e(TAG, "Erro ao enviar email", e);
            Toast.makeText(context, "Não foi possível enviar o email", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Compartilha o PDF
     */
    public void sharePdf(File pdfFile, String title) {
        try {
            Uri uri = FileProvider.getUriForFile(context, 
                    context.getPackageName() + ".fileprovider", pdfFile);
            
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            context.startActivity(Intent.createChooser(shareIntent, "Compartilhar relatório..."));
        } catch (Exception e) {
            Log.e(TAG, "Erro ao compartilhar PDF", e);
            Toast.makeText(context, "Não foi possível compartilhar o PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
