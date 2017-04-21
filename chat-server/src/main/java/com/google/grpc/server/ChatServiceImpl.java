/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.grpc.server;

import com.example.grpc.chat.Chat;
import com.example.grpc.chat.ChatServiceGrpc;
//import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;

import java.util.LinkedHashSet;

public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
 
    private static LinkedHashSet<StreamObserver<Chat.ChatMessageFromServer>> 
                observers = new LinkedHashSet<>();

    @Override
    public StreamObserver<Chat.ChatMessage> 
        chat(StreamObserver<Chat.ChatMessageFromServer> responseObserver) {

        // Add response observer to the list
        observers.add(responseObserver);

        // Handler for client messages
        return new StreamObserver<Chat.ChatMessage>() {
 
            @Override
            public void onNext(Chat.ChatMessage value) {

                System.out.println(value);

                // Create a server message from the client message
                Chat.ChatMessageFromServer message = Chat.ChatMessageFromServer
                        .newBuilder()
                        .setMessage(value)
                        //.setTimestamp(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000))
                        .build();

                // Notify all observers
                for (StreamObserver<Chat.ChatMessageFromServer> observer : observers) {
                    observer.onNext(message);
                }
            }

            @Override
            public void onError(Throwable t) {
                // Do something...
            }

            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
            }
        };
    }
}
