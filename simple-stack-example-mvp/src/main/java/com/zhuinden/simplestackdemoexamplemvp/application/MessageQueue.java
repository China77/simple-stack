package com.zhuinden.simplestackdemoexamplemvp.application;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Zhuinden on 2017.01.27..
 */

@Singleton
public class MessageQueue {
    @Inject
    public MessageQueue() {
    }

    public interface Receiver {
        public Key getKey();

        public void receiveMessage(Object message);
    }

    Map<Key, Queue<Object>> messages = new ConcurrentHashMap<>();

    public void pushMessageTo(Key recipient, Object message) {
        Queue<Object> messageQueue = messages.get(recipient);
        if(messageQueue == null) {
            messageQueue = new ConcurrentLinkedQueue<>();
            messages.put(recipient, messageQueue);
        }
        messageQueue.add(message);
    }

    public void requestMessages(Receiver receiver) {
        Queue<Object> messageQueue = messages.get(receiver.getKey());
        if(messageQueue != null) {
            Iterator<Object> messages = messageQueue.iterator();
            while(messages.hasNext()) {
                receiver.receiveMessage(messages.next());
                messages.remove();
            }
        }
    }
}
