package com.androthink.peach;

import com.androthink.peach.listener.ResponseErrorListener;
import com.androthink.peach.listener.ResponseListener;

import java.io.File;
import java.util.Map;

public class PeachBranch {

    public static Peach create_GET_Request(String url, Map<String, String> headerParams,
                                           ResponseListener responseListener,
                                           ResponseErrorListener errorListener) {

        return new Peach(new HttpGetRequest(url,headerParams, responseListener, errorListener), RequestMethod.REQUEST_METHOD.GET);
    }

    public static Peach create_POST_Request(String url, final Map<String, String> headerParams,
                                            final Map<String, String> bodyParams,
                                            ResponseListener responseListener,
                                            ResponseErrorListener errorListener) {

        return new Peach(new HttpPostRequest(url,headerParams,bodyParams,responseListener,errorListener), RequestMethod.REQUEST_METHOD.POST);
    }

    public static Peach create_POST_DataPart_Request(String url, final Map<String, String> headerParams,
                                                     final Map<String, String> bodyParams,
                                                     final Map<String, File> filesParams,
                                                     ResponseListener responseListener,
                                                     ResponseErrorListener errorListener) {

        return new Peach(new HttpPostDataPartRequest(url,headerParams,bodyParams,filesParams,responseListener,errorListener), RequestMethod.REQUEST_METHOD.POST_DATA_PART);
    }
}
