package com.androthink.peach;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class PeachQueue {
    private List<Peach> peachQueue;

    PeachQueue(){
        this.peachQueue = new ArrayList<>();
    }

    void add(Peach peach) {
        if (peach.getTag().equals("")) {
            peach.setTag(NetworkUtils.CONNECTION_DEFAULT_TAG);
        }
        if (peach.getId() == -1) {
            peach.setId(((int) (Math.random() * 9000) + 1000));
        }

        switch (peach.getRequestMethod()) {
            case GET:
                peach.setRequest(((HttpGetRequest) peach.getRequest()).setRequestId(peach.getId()));
                break;
            case POST:
                peach.setRequest(((HttpPostRequest) peach.getRequest()).setRequestId(peach.getId()));
                break;
            case POST_DATA_PART:
                peach.setRequest(((HttpPostDataPartRequest) peach.getRequest()).setRequestId(peach.getId()));
                break;
        }

        this.peachQueue.add(peach);
        this.peachQueue.get(this.peachQueue.size() - 1).getRequest().execute();
    }

    void cancelAll(String tag){
        for (Peach peach : peachQueue) {
            if(peach.getTag().equals(tag)){
                if(!peach.getRequest().isCancelled()) {
                    peach.getRequest().cancel(true);
                }
                peachQueue.remove(peach);
            }
        }
    }

    void cancelAll(){
        for (Peach peach : peachQueue) {
            if (!peach.getRequest().isCancelled()) {
                peach.getRequest().cancel(true);
            }
            peachQueue.remove(peach);
        }
    }

    void popRequestFromQueue(int id){
        for (Peach peach : peachQueue) {
            if(peach.getId() == id){
                if(!peach.getRequest().isCancelled()) {
                    peach.getRequest().cancel(true);
                }
                peachQueue.remove(peach);
                Log.e("API_CONNECTION_QUEUE","POP REQUEST : " + id);
                return;
            }
        }
    }
}
