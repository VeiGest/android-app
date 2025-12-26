package com.veigest.sdk.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de resposta base da API.
 * 
 * @param <T> Tipo dos dados contidos na resposta
 */
public class ApiResponse<T> {
    
    @SerializedName("data")
    private T data;
    
    @SerializedName("success")
    private boolean success;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("pagination")
    private Pagination pagination;
    
    @SerializedName("error")
    private ApiError error;
    
    @Nullable
    public T getData() {
        return data;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    @Nullable
    public String getMessage() {
        return message;
    }
    
    @Nullable
    public Pagination getPagination() {
        return pagination;
    }
    
    @Nullable
    public ApiError getError() {
        return error;
    }
    
    /**
     * Informações de paginação.
     */
    public static class Pagination {
        @SerializedName("total")
        private int total;
        
        @SerializedName("page")
        private int page;
        
        @SerializedName("limit")
        private int limit;
        
        @SerializedName("total_pages")
        private int totalPages;
        
        public int getTotal() {
            return total;
        }
        
        public int getPage() {
            return page;
        }
        
        public int getLimit() {
            return limit;
        }
        
        public int getTotalPages() {
            return totalPages;
        }
        
        public boolean hasNextPage() {
            return page < totalPages;
        }
        
        public boolean hasPreviousPage() {
            return page > 1;
        }
    }
    
    /**
     * Estrutura de erro da API.
     */
    public static class ApiError {
        @SerializedName("code")
        private int code;
        
        @SerializedName("message")
        private String message;
        
        @SerializedName("details")
        private Object details;
        
        public int getCode() {
            return code;
        }
        
        public String getMessage() {
            return message;
        }
        
        public Object getDetails() {
            return details;
        }
    }
}
