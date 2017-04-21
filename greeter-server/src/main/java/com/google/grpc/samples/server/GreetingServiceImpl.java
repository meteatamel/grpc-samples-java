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
package com.google.grpc.samples.server;

import com.example.grpc.GreetingServiceGrpc;
import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;

import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
    
    @Override
    public void greeting(HelloRequest request, 
            StreamObserver<HelloResponse> responseObserver) {

        System.out.println("Received request: " + request);

        String greeting = "Hello there, " + request.getName();

        // Create response
        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();

        // Send and commit
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
