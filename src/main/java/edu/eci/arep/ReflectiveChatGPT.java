package edu.eci.arep;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveChatGPT {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(45000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 45000.");
            System.exit(1);
        }

        boolean running = true;

        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String requestLine = in.readLine();
            if (requestLine == null) {
                clientSocket.close();
                continue;
            }

            System.out.println("Solicitud recibida: " + requestLine);

            // URI de la solicitud
            String requestStringURI = requestLine.split(" ")[1];
            URI requestURI = new URI(requestStringURI);
            String query = requestURI.getQuery(); //SOLO NECESITO LA QUERY DE LA FUNCION PA SABER Q ES

            String response;
            if (query != null && query.startsWith("comando=")) {
                String comando = query.split("=")[1]; //AQUI PARTIMOS LA QUERY EN DOS PARA OBTENER [1] SOLO LA FUNCION
                response = procesarComando(comando);
            } else {
                response = "ERROR EN EL FORMATO DEL COMANDO";
            }

            //respuesta HTTP en formato JSON
            String jsonResponse = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    response;

            // Enviamos la respuesta al cliente
            out.println(jsonResponse);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static String procesarComando(String comando){
        if(comando.contains("Class")){
            return "Class";
        } else if(comando.contains("invoke")){
            return "invoke";
        } else if(comando.contains("unaryInvoke")){
            return "unaryInvoke";
        } else if(comando.contains("binaryInvoke")){
            return "binaryInvoke";
        } else {
            return "ELSE";
        }
    }

}