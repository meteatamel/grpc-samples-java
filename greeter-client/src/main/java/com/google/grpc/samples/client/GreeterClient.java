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

package com.google.grpc.samples.client;

import com.example.grpc.GreetingServiceGrpc;
import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;
import com.example.grpc.Sentiment;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreeterClient {

    private static final String HOST = "localhost";
    private static final int PORT = 50051;

    public static void main(String[] args) throws InterruptedException {

        // Create a channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT)
                .usePlaintext(true)
                .build();

        // Create a blocking stub with the channel
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = 
                GreetingServiceGrpc.newBlockingStub(channel);

        // Create a request
        HelloRequest request = HelloRequest.newBuilder()
                .setName("Mete - on Java")
                .setAge(34)
                .setSentiment(Sentiment.HAPPY)
                .build();

        // Send the request using the stub
        System.out.println("GreeterClient sending request");
        HelloResponse helloResponse = stub.greeting(request);

        System.out.println("GreeterClient received response: " + helloResponse.getGreeting());

        //channel.shutdown();
    }
}
