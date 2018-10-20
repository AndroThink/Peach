package com.androthink.peach;

import android.annotation.SuppressLint;
import android.content.Context;

public class PeachTree {

    @SuppressLint("StaticFieldLeak")
    private static PeachTree peachTree;
    private static PeachQueue requestQueue;
    private Context context;

    public static synchronized PeachTree getInstance(Context context) {
        if (peachTree == null) {
            peachTree = new PeachTree(context);
        }
        return peachTree;
    }

    private PeachTree(Context context) {
        requestQueue = getPeachQueue();
        this.context = context;
    }

    private PeachQueue getPeachQueue() {
        if (requestQueue == null) {
            requestQueue = new PeachQueue();
        }
        return requestQueue;
    }

    public void addToRequestQueue(Peach peach, String tag) {
        if(NetworkUtils.isNetworkAvailable(context)) {
            peach.setTag(tag);
            getPeachQueue().add(peach);
        }else {
            switch (peach.getRequestMethod()) {
                case GET:
                    ((HttpGetRequest) peach.getRequest()).setRequestId(peach.getId())
                            .getResponseErrorListener()
                            .onResponseError(new ResponseError(0, "No Internet Available !", true, false));
                    break;
                case POST:
                    ((HttpPostRequest) peach.getRequest()).setRequestId(peach.getId())
                            .getResponseErrorListener()
                            .onResponseError(new ResponseError(0, "No Internet Available !", true, false));
                    break;
                case POST_DATA_PART:
                    ((HttpPostDataPartRequest) peach.getRequest()).setRequestId(peach.getId())
                            .getResponseErrorListener()
                            .onResponseError(new ResponseError(0, "No Internet Available !", true, false));
                    break;
            }
        }
    }

    public void addToRequestQueue(Peach peach) {
        if(NetworkUtils.isNetworkAvailable(context)) {
            peach.setTag("");
            getPeachQueue().add(peach);
        }else {
            switch (peach.getRequestMethod()) {
                case GET:
                    ((HttpGetRequest) peach.getRequest()).setRequestId(peach.getId())
                            .getResponseErrorListener()
                            .onResponseError(new ResponseError(0, "No Internet Available !", true, false));
                    break;
                case POST:
                    ((HttpPostRequest) peach.getRequest()).setRequestId(peach.getId())
                            .getResponseErrorListener()
                            .onResponseError(new ResponseError(0, "No Internet Available !", true, false));
                    break;
                case POST_DATA_PART:
                    ((HttpPostDataPartRequest) peach.getRequest()).setRequestId(peach.getId())
                            .getResponseErrorListener()
                            .onResponseError(new ResponseError(0, "No Internet Available !", true, false));
                    break;
            }
        }
    }

    public void cancelPendingRequests(String tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    public void cancelAllPendingRequests() {
        if (requestQueue != null) {
            requestQueue.cancelAll();
        }
    }

    static void popRequestFromQueue(int id){
        if (requestQueue != null) {
            requestQueue.popRequestFromQueue(id);
        }
    }

    public void setConnectionTimeout(int millisecond){
        NetworkUtils.setConnectionTimeout(millisecond);
    }

    public boolean isNetworkAvailable(){
        return NetworkUtils.isNetworkAvailable(context);
    }
}
