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

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreeterServer {

    private static final int SERVER_PORT = 50051;

    public static void main(String[] args) throws IOException, InterruptedException {

        // Build server
        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new GreetingServiceImpl())
                .build();

        // Start server
        System.out.println("Starting GreeterServer on port " + SERVER_PORT);
        server.start();

        // Keep it running
        System.out.println("GreeterServer started!");
        server.awaitTermination();
    }
}
