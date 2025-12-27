# Upload e Download de Ficheiros

Guia completo para trabalhar com ficheiros no VeiGest SDK.

---

## Visão Geral

O `FileService` permite:
- Upload de ficheiros (imagens, documentos PDF, etc.)
- Download de ficheiros
- Listagem de ficheiros
- Remoção de ficheiros

---

## Tipos de Ficheiros Suportados

| Extensão | MIME Type | Uso Típico |
|----------|-----------|------------|
| `.jpg`, `.jpeg` | `image/jpeg` | Fotos de veículos, danos |
| `.png` | `image/png` | Imagens com transparência |
| `.pdf` | `application/pdf` | Documentos, contratos |
| `.doc`, `.docx` | `application/msword` | Documentos Word |

---

## Upload de Ficheiros

### Upload Básico

```java
File file = new File("/path/to/document.pdf");

sdk.files().upload(file, new VeiGestCallback<FileInfo>() {
    @Override
    public void onSuccess(@NonNull FileInfo fileInfo) {
        int fileId = fileInfo.getId();
        String fileName = fileInfo.getName();
        String url = fileInfo.getUrl();
        
        Log.d("Upload", "Ficheiro enviado: " + fileName);
        Log.d("Upload", "ID: " + fileId);
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("Upload", "Erro: " + error.getMessage());
    }
});
```

### Upload de Imagem da Câmara

```java
// Capturar imagem da câmara e fazer upload
private void uploadCameraImage(Uri imageUri) {
    // Converter URI para File
    File imageFile = new File(getRealPathFromUri(imageUri));
    
    sdk.files().upload(imageFile, new VeiGestCallback<FileInfo>() {
        @Override
        public void onSuccess(@NonNull FileInfo fileInfo) {
            // Associar ao veículo ou documento
            associarFicheiroAoVeiculo(vehicleId, fileInfo.getId());
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            Toast.makeText(context, "Erro no upload: " + error.getMessage(), 
                Toast.LENGTH_SHORT).show();
        }
    });
}

// Helper para obter path real de URI
private String getRealPathFromUri(Uri uri) {
    String[] projection = {MediaStore.Images.Media.DATA};
    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
    if (cursor == null) return null;
    
    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    String path = cursor.getString(columnIndex);
    cursor.close();
    return path;
}
```

### Upload de Imagem da Galeria

```java
// Activity Result para galeria
ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            Uri selectedImageUri = result.getData().getData();
            uploadImageFromUri(selectedImageUri);
        }
    }
);

// Abrir galeria
private void openGallery() {
    Intent intent = new Intent(Intent.ACTION_PICK, 
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    galleryLauncher.launch(intent);
}

// Fazer upload
private void uploadImageFromUri(Uri uri) {
    try {
        // Criar ficheiro temporário
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("upload_", ".jpg", getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        
        inputStream.close();
        outputStream.close();
        
        // Upload
        sdk.files().upload(tempFile, new VeiGestCallback<FileInfo>() {
            @Override
            public void onSuccess(@NonNull FileInfo fileInfo) {
                tempFile.delete(); // Limpar ficheiro temporário
                processUploadedFile(fileInfo);
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                tempFile.delete();
                showError(error);
            }
        });
        
    } catch (IOException e) {
        Log.e("Upload", "Erro ao processar ficheiro", e);
    }
}
```

### Upload com Progress (Personalizado)

```java
// Nota: O SDK base não inclui progress, mas pode ser implementado
// com um RequestBody personalizado

public class ProgressRequestBody extends RequestBody {
    private final File file;
    private final ProgressListener listener;
    
    public interface ProgressListener {
        void onProgress(long bytesWritten, long totalBytes);
    }
    
    public ProgressRequestBody(File file, ProgressListener listener) {
        this.file = file;
        this.listener = listener;
    }
    
    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }
    
    @Override
    public long contentLength() {
        return file.length();
    }
    
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = Okio.source(file);
        Buffer buffer = new Buffer();
        long totalBytesWritten = 0;
        long readCount;
        
        while ((readCount = source.read(buffer, 2048)) != -1) {
            sink.write(buffer, readCount);
            totalBytesWritten += readCount;
            listener.onProgress(totalBytesWritten, contentLength());
        }
    }
}

// Uso com ProgressBar
private void uploadWithProgress(File file) {
    progressBar.setMax(100);
    progressBar.setProgress(0);
    
    sdk.files().upload(file, new VeiGestCallback<FileInfo>() {
        @Override
        public void onSuccess(@NonNull FileInfo fileInfo) {
            progressBar.setProgress(100);
            Toast.makeText(context, "Upload concluído!", Toast.LENGTH_SHORT).show();
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            progressBar.setProgress(0);
            showError(error);
        }
    });
}
```

---

## Download de Ficheiros

### Download Básico

```java
int fileId = 123;

sdk.files().download(fileId, new VeiGestCallback<ResponseBody>() {
    @Override
    public void onSuccess(@NonNull ResponseBody body) {
        try {
            // Guardar ficheiro
            File outputFile = new File(getExternalFilesDir(null), "downloaded_file.pdf");
            InputStream inputStream = body.byteStream();
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            inputStream.close();
            outputStream.close();
            
            Log.d("Download", "Ficheiro guardado: " + outputFile.getAbsolutePath());
            
        } catch (IOException e) {
            Log.e("Download", "Erro ao guardar ficheiro", e);
        }
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("Download", "Erro: " + error.getMessage());
    }
});
```

### Download para Cache

```java
private void downloadToCache(int fileId, String fileName, DownloadCallback callback) {
    sdk.files().download(fileId, new VeiGestCallback<ResponseBody>() {
        @Override
        public void onSuccess(@NonNull ResponseBody body) {
            new Thread(() -> {
                try {
                    File cacheFile = new File(getCacheDir(), fileName);
                    saveResponseToFile(body, cacheFile);
                    
                    runOnUiThread(() -> callback.onComplete(cacheFile));
                    
                } catch (IOException e) {
                    runOnUiThread(() -> callback.onError(e));
                }
            }).start();
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            callback.onError(error);
        }
    });
}

interface DownloadCallback {
    void onComplete(File file);
    void onError(Exception error);
}
```

### Abrir Ficheiro Após Download

```java
private void downloadAndOpen(int fileId, String fileName, String mimeType) {
    sdk.files().download(fileId, new VeiGestCallback<ResponseBody>() {
        @Override
        public void onSuccess(@NonNull ResponseBody body) {
            try {
                // Guardar em pasta pública
                File downloadsDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
                File outputFile = new File(downloadsDir, fileName);
                
                saveResponseToFile(body, outputFile);
                
                // Abrir com app apropriada
                openFile(outputFile, mimeType);
                
            } catch (IOException e) {
                showError("Erro ao guardar ficheiro");
            }
        }
        
        @Override
        public void onError(@NonNull VeiGestException error) {
            showError(error.getMessage());
        }
    });
}

private void openFile(File file, String mimeType) {
    Uri uri = FileProvider.getUriForFile(this, 
        getPackageName() + ".fileprovider", file);
    
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(uri, mimeType);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    
    try {
        startActivity(intent);
    } catch (ActivityNotFoundException e) {
        Toast.makeText(this, "Nenhuma app para abrir este ficheiro", 
            Toast.LENGTH_SHORT).show();
    }
}
```

---

## Gestão de Ficheiros

### Listar Ficheiros

```java
sdk.files().list(new VeiGestCallback<List<FileInfo>>() {
    @Override
    public void onSuccess(@NonNull List<FileInfo> files) {
        for (FileInfo file : files) {
            Log.d("Files", String.format(
                "ID: %d, Nome: %s, Tamanho: %d bytes",
                file.getId(),
                file.getName(),
                file.getSize()
            ));
        }
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("Files", "Erro: " + error.getMessage());
    }
});
```

### Obter Detalhes de Ficheiro

```java
sdk.files().get(fileId, new VeiGestCallback<FileInfo>() {
    @Override
    public void onSuccess(@NonNull FileInfo file) {
        Log.d("FileInfo", "Nome: " + file.getName());
        Log.d("FileInfo", "Tipo: " + file.getMimeType());
        Log.d("FileInfo", "Tamanho: " + file.getSize() + " bytes");
        Log.d("FileInfo", "URL: " + file.getUrl());
        Log.d("FileInfo", "Criado: " + file.getCreatedAt());
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("FileInfo", "Erro: " + error.getMessage());
    }
});
```

### Eliminar Ficheiro

```java
sdk.files().delete(fileId, new VeiGestCallback<Void>() {
    @Override
    public void onSuccess(Void result) {
        Log.d("Delete", "Ficheiro eliminado com sucesso");
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        Log.e("Delete", "Erro: " + error.getMessage());
    }
});
```

---

## Fluxos Comuns

### Upload de Documento de Veículo

```java
public class VehicleDocumentUploader {
    private final VeiGestSDK sdk;
    
    public void uploadVehicleDocument(int vehicleId, File document, String tipo, 
            String dataValidade, UploadCompleteCallback callback) {
        
        // Passo 1: Upload do ficheiro
        sdk.files().upload(document, new VeiGestCallback<FileInfo>() {
            @Override
            public void onSuccess(@NonNull FileInfo fileInfo) {
                // Passo 2: Criar documento associado
                createDocument(vehicleId, fileInfo.getId(), tipo, dataValidade, callback);
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                callback.onError("Erro no upload: " + error.getMessage());
            }
        });
    }
    
    private void createDocument(int vehicleId, int fileId, String tipo, 
            String dataValidade, UploadCompleteCallback callback) {
        
        DocumentService.DocumentBuilder builder = new DocumentService.DocumentBuilder()
            .vehicleId(vehicleId)
            .fileId(fileId)
            .tipo(tipo)
            .dataValidade(dataValidade);
        
        sdk.documents().create(builder, new VeiGestCallback<Document>() {
            @Override
            public void onSuccess(@NonNull Document document) {
                callback.onComplete(document);
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                callback.onError("Erro ao criar documento: " + error.getMessage());
            }
        });
    }
    
    interface UploadCompleteCallback {
        void onComplete(Document document);
        void onError(String message);
    }
}
```

### Upload de Foto de Dano

```java
public class DamageReporter {
    private final VeiGestSDK sdk;
    
    public void reportDamage(int vehicleId, File photo, String descricao,
            DamageReportCallback callback) {
        
        // Upload foto
        sdk.files().upload(photo, new VeiGestCallback<FileInfo>() {
            @Override
            public void onSuccess(@NonNull FileInfo fileInfo) {
                // Criar ticket com foto
                TicketService.TicketBuilder builder = new TicketService.TicketBuilder()
                    .titulo("Dano reportado")
                    .descricao(descricao)
                    .tipo("tecnico")
                    .prioridade("alta")
                    .vehicleId(vehicleId)
                    .observacoes("Foto anexada: " + fileInfo.getUrl());
                
                sdk.tickets().create(builder, new VeiGestCallback<Ticket>() {
                    @Override
                    public void onSuccess(@NonNull Ticket ticket) {
                        callback.onComplete(ticket, fileInfo);
                    }
                    
                    @Override
                    public void onError(@NonNull VeiGestException error) {
                        callback.onError(error);
                    }
                });
            }
            
            @Override
            public void onError(@NonNull VeiGestException error) {
                callback.onError(error);
            }
        });
    }
    
    interface DamageReportCallback {
        void onComplete(Ticket ticket, FileInfo photo);
        void onError(VeiGestException error);
    }
}
```

---

## Helpers Úteis

### Converter Size para String Legível

```java
public static String formatFileSize(long size) {
    if (size < 1024) return size + " B";
    int exp = (int) (Math.log(size) / Math.log(1024));
    String pre = "KMGTPE".charAt(exp - 1) + "";
    return String.format("%.1f %sB", size / Math.pow(1024, exp), pre);
}

// Uso
FileInfo file = ...;
String sizeText = formatFileSize(file.getSize()); // "1.5 MB"
```

### Obter MIME Type

```java
public static String getMimeType(File file) {
    String extension = MimeTypeMap.getFileExtensionFromUrl(file.getName());
    if (extension != null) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    return "application/octet-stream";
}
```

### Guardar ResponseBody em Ficheiro

```java
private void saveResponseToFile(ResponseBody body, File file) throws IOException {
    InputStream inputStream = null;
    FileOutputStream outputStream = null;
    
    try {
        inputStream = body.byteStream();
        outputStream = new FileOutputStream(file);
        
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
    } finally {
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
    }
}
```

---

## Tratamento de Erros

### Erros Comuns

```java
sdk.files().upload(file, new VeiGestCallback<FileInfo>() {
    @Override
    public void onSuccess(@NonNull FileInfo fileInfo) {
        // Sucesso
    }
    
    @Override
    public void onError(@NonNull VeiGestException error) {
        switch (error.getErrorType()) {
            case VALIDATION_ERROR:
                // Ficheiro inválido (tipo não suportado, muito grande)
                showError("Ficheiro inválido: " + error.getMessage());
                break;
                
            case NETWORK_ERROR:
                // Sem conexão
                showError("Sem conexão. Tente novamente.");
                break;
                
            case UNAUTHORIZED:
                // Sessão expirada
                redirectToLogin();
                break;
                
            case SERVER_ERROR:
                // Erro no servidor
                showError("Erro no servidor. Tente mais tarde.");
                break;
                
            default:
                showError("Erro: " + error.getMessage());
        }
    }
});
```

---

## Configuração do FileProvider

Para partilhar ficheiros, configure o FileProvider no `AndroidManifest.xml`:

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

Crie `res/xml/file_paths.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-files-path name="external_files" path="." />
    <cache-path name="cache" path="." />
    <external-path name="downloads" path="Download/" />
</paths>
```

---

## Permissões Necessárias

```xml
<!-- Para Android < 10 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28" />

<!-- Para aceder à câmara -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- Internet (já incluída pelo SDK) -->
<uses-permission android:name="android.permission.INTERNET" />
```

### Solicitar Permissões em Runtime

```java
private void checkPermissions() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.CAMERA},
            CAMERA_PERMISSION_REQUEST);
    }
}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
    if (requestCode == CAMERA_PERMISSION_REQUEST) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            showError("Permissão de câmara necessária");
        }
    }
}
```

---

## Próximos Passos

- [Referência de Serviços](SERVICES.md)
- [Builders](BUILDERS.md)
- [Boas Práticas](BEST_PRACTICES.md)
