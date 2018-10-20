package com.androthink.peach;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.URLConnection.guessContentTypeFromName;

class MultipartUtility {

    private static final String CRLF = "\r\n";
    private static final String CHARSET = "UTF-8";

    private HttpURLConnection connection;
    private OutputStream outputStream;
    private PrintWriter writer;
    private final String boundary;

    private boolean success;
    boolean isSuccess(){return success;}
    private boolean isTimeout = false;
    boolean isTimeout(){return isTimeout;}

    private final URL url;
    private final long start;

    private String result;
    String getResult(){return result;}
    private int responseCode;
    int getResponseCode(){return responseCode;}

    MultipartUtility(final URL url, Map<String, String> headerParams){

        start = currentTimeMillis();
        this.url = url;

        boundary = "---------------------------" + currentTimeMillis();

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(NetworkUtils.CONNECTION_TIMEOUT);
            connection.setReadTimeout(NetworkUtils.READ_TIMEOUT);
            connection.setRequestMethod(RequestMethod.POST);

            for (String key : headerParams.keySet()) {
                connection.setRequestProperty(key,headerParams.get(key));
            }

            connection.setRequestProperty("Accept-Charset", CHARSET);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);

            success = true;
        }catch (java.net.ConnectException e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
            isTimeout = false;
        }
        catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
            isTimeout = true;
        }catch (IOException e){
            e.printStackTrace();
            result = e.toString();
            success = false;
        }
        catch (Exception e){
            e.printStackTrace();
            result = e.toString();
            success = false;
        }
    }

    void addFormField(final String name, final String value) {
        writer.append("--").append(boundary).append(CRLF)
                .append("Content-Disposition: form-data; name=\"").append(name)
                .append("\"").append(CRLF)
                .append("Content-Type: text/plain; charset=").append(CHARSET)
                .append(CRLF).append(CRLF).append(value).append(CRLF);
    }

    void addFilePart(final String fieldName, final File uploadFile) {

        try {
            final String fileName = uploadFile.getName();
            writer.append("--").append(boundary).append(CRLF)
                    .append("Content-Disposition: form-data; name=\"")
                    .append(fieldName).append("\"; filename=\"").append(fileName)
                    .append("\"").append(CRLF).append("Content-Type: ")
                    .append(guessContentTypeFromName(fileName)).append(CRLF)
                    .append("Content-Transfer-Encoding: binary").append(CRLF)
                    .append(CRLF);

            writer.flush();
            outputStream.flush();

            final FileInputStream inputStream = new FileInputStream(uploadFile);
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();

            success = true;
        }catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
            isTimeout = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
        }

        writer.append(CRLF);
    }

    void finish() {

        writer.append(CRLF).append("--").append(boundary).append("--").append(CRLF);
        writer.close();

        try {
            responseCode = connection.getResponseCode();
            if (responseCode != HTTP_OK) {
                result = "" + url + " failed with HTTP status: " + responseCode;
                success = false;
            } else {

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                //Check if the line we are reading is not null
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();

                success = true;
            }
        }catch (java.net.SocketTimeoutException e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
            isTimeout = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
            isTimeout = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            result = e.toString();
            success = false;
            isTimeout = true;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }

            Log.i("PeachBranch", "" + url + " took " + (currentTimeMillis() - start) + " ms");
        }
    }
}
