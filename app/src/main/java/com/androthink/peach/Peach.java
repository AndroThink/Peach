package com.androthink.peach;

import android.os.AsyncTask;

public class Peach {

    private int id;
    private String tag;
    private AsyncTask<String, Void, String> request;
    private RequestMethod.REQUEST_METHOD requestMethod;

    public Peach(){
        this.id = -1;
        this.tag = "";
        this.requestMethod = RequestMethod.REQUEST_METHOD.GET;
    }

    Peach(RequestMethod.REQUEST_METHOD requestMethod){
        this.id = -1;
        this.tag = "";
        this.requestMethod = requestMethod;
    }

    Peach(String tag, AsyncTask<String, Void, String> request, RequestMethod.REQUEST_METHOD requestMethod) {
        this.id = -1;
        this.tag = tag;
        this.request = request;
        this.requestMethod = requestMethod;
    }

    Peach(AsyncTask<String, Void, String> request, RequestMethod.REQUEST_METHOD requestMethod) {
        this.id = -1;
        this.tag = "";
        this.request = request;
        this.requestMethod = requestMethod;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getTag() {
        return tag;
    }

    void setTag(String tag) {
        this.tag = tag;
    }

    AsyncTask<String, Void, String> getRequest() {
        return request;
    }

    void setRequest(AsyncTask<String, Void, String> request) {
        this.request = request;
    }

    RequestMethod.REQUEST_METHOD getRequestMethod() {
        return requestMethod;
    }

    void setRequestMethod(RequestMethod.REQUEST_METHOD requestMethod){this.requestMethod = requestMethod;}
}
