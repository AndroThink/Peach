package com.androthink.peach;

import android.os.AsyncTask;
import android.util.Log;

import com.androthink.peach.listener.ResponseErrorListener;
import com.androthink.peach.listener.ResponseListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class HttpGetRequest extends AsyncTask<String, Void, String> {

    private int requestId;

    HttpGetRequest setRequestId(int requestId) {
        this.requestId = requestId;
        return this;
    }

    private String url;
    private boolean success = false;
    private boolean isTimeout = false;

    private Map<String,String> headerParams;

    private ResponseListener responseListener;
    private ResponseErrorListener responseErrorListener;

    ResponseErrorListener getResponseErrorListener() {
        return responseErrorListener;
    }

    private int responseCode = 200;

    HttpGetRequest(String url, Map<String, String> headerParams, ResponseListener responseListener, ResponseErrorListener responseErrorListener){
        this.url = url;
        this.headerParams = headerParams;
        this.responseListener = responseListener;
        this.responseErrorListener = responseErrorListener;
    }

    @Override
    protected String doInBackground(String... params){
        String result;

        HttpURLConnection connection = null;
        try {
            URL myUrl = new URL(url);

            connection =(HttpURLConnection)myUrl.openConnection();

            connection.setRequestMethod(RequestMethod.GET);
            connection.setReadTimeout(NetworkUtils.READ_TIMEOUT);
            connection.setConnectTimeout(NetworkUtils.CONNECTION_TIMEOUT);

            for (String key : headerParams.keySet()) {
                connection.setRequestProperty(key,headerParams.get(key));
            }

            connection.connect();
            responseCode = connection.getResponseCode();

            if(responseCode == HttpsURLConnection.HTTP_OK) {
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();

                result = stringBuilder.toString();
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
            responseErrorListener.onResponseError(new ResponseError(responseCode , result ,false,isTimeout));
        }

        PeachTree.popRequestFromQueue(requestId);
    }
}
