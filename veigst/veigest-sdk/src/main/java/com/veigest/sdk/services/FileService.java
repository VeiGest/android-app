package com.veigest.sdk.services;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.veigest.sdk.VeiGestCallback;
import com.veigest.sdk.VeiGestException;
import com.veigest.sdk.api.VeiGestApiClient;
import com.veigest.sdk.models.ApiResponse;
import com.veigest.sdk.models.FileInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Serviço de ficheiros do SDK.
 * Gerencia upload, download e listagem de ficheiros.
 */
public class FileService extends BaseService {
    
    public FileService(@NonNull VeiGestApiClient apiClient) {
        super(apiClient);
    }
    
    /**
     * Lista todos os ficheiros.
     * 
     * @param callback Callback com lista de ficheiros
     */
    public void list(@NonNull VeiGestCallback<List<FileInfo>> callback) {
        list(null, null, null, callback);
    }
    
    /**
     * Lista ficheiros com filtros.
     * 
     * @param tipo Tipo MIME do ficheiro (ex: "image/jpeg", "application/pdf")
     * @param page Página
     * @param limit Limite por página
     * @param callback Callback com lista de ficheiros
     */
    public void list(@Nullable String tipo, @Nullable Integer page, @Nullable Integer limit,
                     @NonNull VeiGestCallback<List<FileInfo>> callback) {
        executeCall(apiClient.getApi().getFiles(tipo, page, limit), callback);
    }
    
    /**
     * Lista apenas imagens.
     */
    public void listImages(@NonNull VeiGestCallback<List<FileInfo>> callback) {
        list("image", null, null, callback);
    }
    
    /**
     * Lista apenas PDFs.
     */
    public void listPdfs(@NonNull VeiGestCallback<List<FileInfo>> callback) {
        list("application/pdf", null, null, callback);
    }
    
    /**
     * Obtém informações de um ficheiro.
     * 
     * @param id ID do ficheiro
     * @param callback Callback com informações
     */
    public void get(int id, @NonNull VeiGestCallback<FileInfo> callback) {
        executeCall(apiClient.getApi().getFile(id), callback);
    }
    
    /**
     * Faz upload de um ficheiro.
     * 
     * @param file Ficheiro a enviar
     * @param nome Nome do ficheiro (opcional, usa nome original se null)
     * @param tipo Tipo/categoria do ficheiro (ex: "documento", "foto")
     * @param callback Callback com informações do ficheiro enviado
     */
    public void upload(@NonNull File file, @Nullable String nome, @Nullable String tipo,
                       @NonNull VeiGestCallback<FileInfo> callback) {
        // Detectar MIME type
        String mimeType = getMimeType(file.getName());
        
        // Criar partes do multipart
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        
        RequestBody nomePart = RequestBody.create(
                MediaType.parse("text/plain"), 
                nome != null ? nome : file.getName()
        );
        
        RequestBody tipoPart = RequestBody.create(
                MediaType.parse("text/plain"), 
                tipo != null ? tipo : "documento"
        );
        
        executeCall(apiClient.getApi().uploadFile(filePart, nomePart, tipoPart), callback);
    }
    
    /**
     * Faz upload de um ficheiro a partir de bytes.
     * 
     * @param bytes Dados do ficheiro
     * @param fileName Nome do ficheiro
     * @param mimeType Tipo MIME
     * @param tipo Tipo/categoria
     * @param callback Callback com informações do ficheiro
     */
    public void upload(@NonNull byte[] bytes, @NonNull String fileName, @NonNull String mimeType,
                       @Nullable String tipo, @NonNull VeiGestCallback<FileInfo> callback) {
        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), bytes);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", fileName, requestFile);
        
        RequestBody nomePart = RequestBody.create(MediaType.parse("text/plain"), fileName);
        RequestBody tipoPart = RequestBody.create(
                MediaType.parse("text/plain"), 
                tipo != null ? tipo : "documento"
        );
        
        executeCall(apiClient.getApi().uploadFile(filePart, nomePart, tipoPart), callback);
    }
    
    /**
     * Faz download de um ficheiro.
     * 
     * @param id ID do ficheiro
     * @param destinationFile Ficheiro de destino
     * @param callback Callback com o ficheiro baixado
     */
    public void download(int id, @NonNull File destinationFile, 
                         @NonNull VeiGestCallback<File> callback) {
        apiClient.getApi().downloadFile(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, 
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        writeToFile(response.body(), destinationFile);
                        callback.onSuccess(destinationFile);
                    } catch (IOException e) {
                        callback.onError(new VeiGestException(
                                "Erro ao salvar ficheiro: " + e.getMessage(), e
                        ));
                    }
                } else {
                    callback.onError(new VeiGestException(
                            "Erro no download: " + response.code(),
                            response.code(),
                            null
                    ));
                }
            }
            
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onError(new VeiGestException(
                        "Erro de rede: " + t.getMessage(), t
                ));
            }
        });
    }
    
    /**
     * Faz download de um ficheiro para uma pasta.
     * 
     * @param id ID do ficheiro
     * @param destinationDir Diretório de destino
     * @param fileName Nome do ficheiro
     * @param callback Callback com o ficheiro baixado
     */
    public void downloadTo(int id, @NonNull File destinationDir, @NonNull String fileName,
                           @NonNull VeiGestCallback<File> callback) {
        File destinationFile = new File(destinationDir, fileName);
        download(id, destinationFile, callback);
    }
    
    /**
     * Elimina um ficheiro.
     * 
     * @param id ID do ficheiro
     * @param callback Callback com resultado
     */
    public void delete(int id, @NonNull VeiGestCallback<Void> callback) {
        executeCall(apiClient.getApi().deleteFile(id), callback);
    }
    
    // ==================== UTILITÁRIOS ====================
    
    /**
     * Escreve o ResponseBody para um ficheiro.
     */
    private void writeToFile(ResponseBody body, File file) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        
        try {
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);
            
            byte[] buffer = new byte[4096];
            int read;
            
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            
            outputStream.flush();
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException ignored) {}
            }
            if (outputStream != null) {
                try { outputStream.close(); } catch (IOException ignored) {}
            }
        }
    }
    
    /**
     * Determina o MIME type a partir da extensão do ficheiro.
     */
    private String getMimeType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "txt":
                return "text/plain";
            case "zip":
                return "application/zip";
            default:
                return "application/octet-stream";
        }
    }
    
    /**
     * Obtém a extensão de um ficheiro.
     */
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot + 1);
        }
        return "";
    }
}
