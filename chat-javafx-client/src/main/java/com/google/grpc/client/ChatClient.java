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

package com.google.grpc.client;

import com.example.grpc.chat.Chat;
import com.example.grpc.chat.ChatServiceGrpc;
import com.example.grpc.chat.Chat.ChatMessage;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class ChatClient extends Application {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private ObservableList<String> messages = FXCollections.observableArrayList();
    private ListView<String> messagesView = new ListView<>();
    private TextField name = new TextField("name");
    private TextField message = new TextField();
    private Button send = new Button();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
 
        setupAndShowPrimaryStage(primaryStage);

        // Create a channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create an async stub with the channel
        ChatServiceGrpc.ChatServiceStub chatService = ChatServiceGrpc.newStub(channel);
 
        // Open a connection to the server
        StreamObserver<Chat.ChatMessage> chat = chatService.chat(new StreamObserver<Chat.ChatMessageFromServer>() {

            // Handler for messages from the server

            @Override
            public void onNext(Chat.ChatMessageFromServer value) {
                // Display the message
                Platform.runLater(() -> {
                    messages.add(value.getMessage().getFrom() + ": " + value.getMessage().getMessage());
                    messagesView.scrollTo(messages.size());
                });
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Disconnected");
            }

            @Override
            public void onCompleted() {
                System.out.println("Disconnected");
            }
        });

        // Send button handler, create a message and send.
        send.setOnAction(e -> {

            // Create a message
            ChatMessage chatMessage = Chat.ChatMessage.newBuilder()
                    .setFrom(name.getText())
                    .setMessage(message.getText())
                    .build();

            // Send the message
            chat.onNext(chatMessage);

            message.setText("");
        });

        primaryStage.setOnCloseRequest(e -> {
            chat.onCompleted();
            channel.shutdown();
        });
    }

    private void setupAndShowPrimaryStage(Stage primaryStage) {
        messagesView.setItems(messages);

        send.setText("Send");

        BorderPane pane = new BorderPane();
        pane.setLeft(name);
        pane.setCenter(message);
        pane.setRight(send);

        BorderPane root = new BorderPane();
        root.setCenter(messagesView);
        root.setBottom(pane);

        primaryStage.setTitle("gRPC Chat");
        primaryStage.setScene(new Scene(root, 480, 320));

        primaryStage.show();
    }
}
