package com.androthink.peach;

import android.os.AsyncTask;
import android.util.Log;

import com.androthink.peach.listener.ResponseErrorListener;
import com.androthink.peach.listener.ResponseListener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

class HttpPostDataPartRequest extends AsyncTask<String, Void, String> {

    private int requestId;

    HttpPostDataPartRequest setRequestId(int requestId) {
        this.requestId = requestId;return this;
    }

    private String url;

    private Map<String,String> headerParams;
    private Map<String, String> bodyParams;
    private Map<String, File> filesParams;

    private ResponseListener responseListener;
    private ResponseErrorListener responseErrorListener;

    ResponseErrorListener getResponseErrorListener() {
        return responseErrorListener;
    }

    private MultipartUtility utility;

    HttpPostDataPartRequest(String url, Map<String, String> headerParams, Map<String, String> bodyParams,
                            Map<String, File> filesParams, ResponseListener responseListener,
                            ResponseErrorListener responseErrorListener) {
        this.url = url;
        this.headerParams = headerParams;
        this.bodyParams = bodyParams;
        this.filesParams = filesParams;
        this.responseListener = responseListener;
        this.responseErrorListener = responseErrorListener;
    }

    @Override
    protected String doInBackground(String... params){

        try {
            utility = new MultipartUtility(new URL(url),headerParams);

            if(!utility.isSuccess()){
                responseErrorListener.onResponseError(new ResponseError(utility.getResponseCode(),
                        utility.getResult(),false, utility.isTimeout()));
                return null;
            }

            for (String key : bodyParams.keySet()) {
                utility.addFormField(key,bodyParams.get(key));
            }

            if(!utility.isSuccess()){
                responseErrorListener.onResponseError(new ResponseError(utility.getResponseCode(),
                        utility.getResult(),false, utility.isTimeout()));
                return null;
            }

            for (String key : filesParams.keySet()) {
                utility.addFilePart(key,filesParams.get(key));
            }

            if(!utility.isSuccess()){
                responseErrorListener.onResponseError(new ResponseError(utility.getResponseCode(),
                        utility.getResult(),false, utility.isTimeout()));
                return null;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            responseErrorListener.onResponseError(new ResponseError(utility.getResponseCode(),
                    ex.toString(),false,false));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            responseErrorListener.onResponseError(new ResponseError(utility.getResponseCode(),
                    ex.toString(),false,false));
        }
        finally {
            utility.finish();

            for (String key : filesParams.keySet()) {
                filesParams.get(key).deleteOnExit();
                Log.i("Peach", "File : " + key + "Ready To Delete .");
            }

            Log.e("Peach", "Peach " + requestId + " Finished .");
        }

        return null;
    }

    protected void onPostExecute(String result){
        PeachTree.popRequestFromQueue(requestId);

        if(utility.isSuccess()){
            responseListener.onResponse(utility.getResult());
            Log.i("Peach","" + "Success");
        }else {
            responseErrorListener.onResponseError(new ResponseError(utility.getResponseCode(),
                    utility.getResult(),false, utility.isTimeout()));
            Log.i("Peach","" + "Failed");
        }
    }
}