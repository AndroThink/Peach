package com.androthink.peach;

import android.os.AsyncTask;
import android.util.Log;

import com.androthink.peach.listener.ResponseErrorListener;
import com.androthink.peach.listener.ResponseListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class HttpPostRequest extends AsyncTask<String, Void, String> {

    private int requestId;

    HttpPostRequest setRequestId(int requestId) {
        this.requestId = requestId;return this;
    }

    private String url;
    private boolean success = false;
    private boolean isTimeout = false;

    private Map<String,String> headerParams;
    private Map<String, String> bodyParam;

    private ResponseListener responseListener;
    private ResponseErrorListener responseErrorListener;

    private int responseCode = 200;

    ResponseErrorListener getResponseErrorListener() {
        return responseErrorListener;
    }

    HttpPostRequest(String url, Map<String, String> headerParams, Map<String, String> bodyParam,
                    ResponseListener responseListener, ResponseErrorListener responseErrorListener) {
        this.url = url;
        this.headerParams = headerParams;
        this.bodyParam = bodyParam;
        this.responseListener = responseListener;
        this.responseErrorListener = responseErrorListener;
    }

    @Override
    protected String doInBackground(String... params){
        String result;
        HttpURLConnection connection = null;

        try {
            URL myUrl = new URL(url);

            connection =(HttpURLConnection) myUrl.openConnection();

            connection.setRequestMethod(RequestMethod.POST);
            connection.setReadTimeout(NetworkUtils.READ_TIMEOUT);
            connection.setConnectTimeout(NetworkUtils.CONNECTION_TIMEOUT);

            connection.setDoInput(true);
            connection.setDoOutput(true);

            for (String key : headerParams.keySet()) {
                connection.setRequestProperty(key,headerParams.get(key));
            }

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(bodyParam));
            writer.flush();
            writer.close();
            os.close();

            responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String response;
                while ((response = br.readLine()) != null){
                    stringBuilder.append(response);
                }
                result = stringBuilder.toString();

                br.close();
            }else {
                result = connection.getResponseMessage();
            }
            success = true;
        }
        catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
            isTimeout = true;
        }
        catch(Exception e){
            e.printStackTrace();
            result = e.toString();
            success = false;
        }
        finally {
            if(connection != null) {
                connection.disconnect();
                Log.e("Peach","Peach " + requestId + " Finished .");
            }
        }
        return result;
    }

    protected void onPostExecute(String result){
        if(success){
            responseListener.onResponse(result);
        }else {
            responseErrorListener.onResponseError(new ResponseError(responseCode,result,false,isTimeout));
        }

        PeachTree.popRequestFromQueue(requestId);
    }

    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}