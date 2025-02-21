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
            //Utilizo JsonObject para facilidad de las respuestas
            String response;
            if (query != null && query.startsWith("comando=")) {
                String comando = query.split("=")[1]; //AQUI PARTIMOS LA QUERY EN DOS PARA OBTENER [1] SOLO LA FUNCION
                response = procesarComando(comando);
            } else {
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("error", "Errorrrrr ");
                response = String.valueOf(errorResponse);
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

    private static String procesarComando(String comando) {
        try {
            String reflectiveMethod = comando.split("\\(")[0]; //OBTENEMOS EL NOMBRE DEL METODO GENERAL A EJECUTAR,EJ: Class, invoke, unaryInvoke, binaryInvoke
            String params = comando.split("\\(")[1].split("\\)")[0];//OBTENEMOS LOS PARAMETROS DENTRO DE LOS PARENTESIS (,,,)
            String[] paramsArray = params.split(",");//SEPARAMOS LOS PARAMETROS Y LOS GUARDAMOS EN UN ARREGLO PARA FACILIDAD
            Class<?> clazz = Class.forName(paramsArray[0]); //Obtenemos un objeto de tipo clase que representa la clase a llamar dentro de los parametros generales, EJ: java.lang.Math, java.lang.System
            String methodName = (paramsArray.length > 1) ? paramsArray[1] : "";//Metodo de la clase dentro de los parametros que queremos ejecutar, EJ: max, sum, abs, getenv

            //Verifico segun el metodo la funcion especifica que debo llamar ya conocienco sus parametros
            switch (reflectiveMethod) {
                case "Class":
                    return obtenerTodo(clazz);
                case "invoke":
                    //return "invoke";
                    return invocarMetodo(clazz, methodName);
                case "unaryInvoke":
                    return "invoke";
                    //return invocarMetodoUnario(clazz, methodName, paramsArray[2], paramsArray[3]);
                case "binaryInvoke":
                    return "invoke";
                    //return invocarMetodoBinario(clazz, methodName, paramsArray[2], paramsArray[3], paramsArray[4], paramsArray[5]);
                default:
                    return "invoke";
                    //JsonObject errorResponse = new JsonObject();
                    //errorResponse.addProperty("error", "Comando no reconocido");
                    //return errorResponse;
            }
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Errorrrrr: " + e.getMessage());
            return "errrororororor";
        }
    }
    //Class([class name])
    private static String obtenerTodo(Class<?> clazz) {
        StringBuffer response = new StringBuffer();
        response.append(clazz.getName());

        List<Object> methodsList = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            methodsList.add(method.getName());
        }
        response.append(methodsList.toString());

        List<Object> fieldsList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            fieldsList.add(field.getName());
        }
        response.append(fieldsList.toString());

        return response.toString();
    }
   //invoke([class name],[method name])
    private static String invocarMetodo(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            Object result = method.invoke(null);
            StringBuffer response = new StringBuffer();
            response.append(result.toString());
            return response.toString();
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Error al invocar método " + e.getMessage());
            return "errorResponse";
        }
    }

        //unaryInvoke([class name],[method name],[paramtype],[param value])
    private static JsonObject invocarMetodoUnario(Class<?> clazz, String methodName, String paramType, String paramValue) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, obtenerTipo(paramType));
            Object param = obtenerValor(paramType, paramValue);
            Object result = method.invoke(null, param);
            JsonObject response = new JsonObject();
            response.addProperty("result", result.toString());
            return response;
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Error al invocar método" + e.getMessage());
            return errorResponse;
        }
    }

    /*
   //binaryInvoke([class name],[method name],[paramtype 1],[param value], [paramtype 2],[param value 2])
    private static JsonObject invocarMetodoBinario(Class<?> clazz, String methodName, String paramType1, String paramValue1, String paramType2, String paramValue2) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, obtenerTipo(paramType1), obtenerTipo(paramType2));
            Object param1 = obtenerValor(paramType1, paramValue1);
            Object param2 = obtenerValor(paramType2, paramValue2);
            Object result = method.invoke(null, param1, param2);
            JsonObject response = new JsonObject();
            response.addProperty("result", result.toString());
            return response;
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Error al invocar método " + e.getMessage());
            return errorResponse;
        }
    }

     */

    private static Class<?> obtenerTipo(String type) {
        switch (type) {
            case "int": return int.class;
            case "double": return double.class;
            case "String": return String.class;
            default: return null;
        }
    }

    private static Object obtenerValor(String type, String value) {
        switch (type) {
            case "int": return Integer.parseInt(value);
            case "double": return Double.parseDouble(value);
            case "String": return value;
            default: return null;
        }
    }


}