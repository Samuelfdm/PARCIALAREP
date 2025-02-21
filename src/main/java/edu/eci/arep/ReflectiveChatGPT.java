package edu.eci.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveChatGPT {
//    //puedo guiarme de FacadeWeb para el server del Backend
//    public static void main(String[] args) throws IOException {
//        ServerSocket serverSocket = null;
//        try {
//            serverSocket = new ServerSocket(45000);
//        } catch (IOException e) {
//            System.err.println("Could not listen on port: 45000.");
//            System.exit(1);
//        }
//
//        Socket clientSocket = null;
//
//        boolean running = true;
//
//        while(running){
//            try {
//                System.out.println("Listo para recibir ...");
//                clientSocket = serverSocket.accept();
//            } catch (IOException e) {
//                System.err.println("Accept failed.");
//                System.exit(1);
//            }
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            String inputLine, outputLine;
//            String firstLine = in.readLine();
//            System.out.println("PRIMERAAAAAAA: "+firstLine);
//            while ((inputLine = in.readLine()) != null) {
//                System.out.println("Recibí: " + inputLine);
//                if (!in.ready()) {
//                    break;
//                }
//            }
//
//            String path = firstLine.split(" ")[1];
//            System.out.println("PATHHHHHHHHHH: "+path);
//
//            if (path.startsWith("/cliente")){
//                outputLine = getWebService();
//            } else if (path.startsWith("/consulta")){
//                outputLine = "HTTP/1.1 200 OK\r\n"
//                        + "Content-Type: text/html\r\n"
//                        + "\r\n"
//                        + "consulta\n";
//                //outputLine = FacadeServices.getFacadeServices().getComando("DD");
//            } else{
//                outputLine = errorMessage();
//            }
//
//            out.println(outputLine);
//            out.close();
//            in.close();
//            clientSocket.close();
//        }
//        serverSocket.close();
//    }
}

