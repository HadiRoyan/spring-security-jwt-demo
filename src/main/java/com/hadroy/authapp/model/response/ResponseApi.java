package com.hadroy.authapp.model.response;

public class ResponseApi<T> {

    public int statusCode;

    public String status;

    public T data;

    public ResponseApi(int statusCode, String status, T data) {
        this.statusCode = statusCode;
        this.status = status;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
