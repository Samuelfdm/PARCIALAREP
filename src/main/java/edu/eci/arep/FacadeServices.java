package edu.eci.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class FacadeServices {

    //Necesito la instancia de FacadeServices, singleton puede ser.
    private static FacadeServices facadeServices;

    public static FacadeServices getFacadeServices(){
        if(facadeServices == null){
            return new FacadeServices();
        }else{
            return facadeServices;
        }
    }

    public FacadeServices(){};

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String BACKEND_URL = "http://localhost:45000/compreflex?comando=";

    public String getComando(String comando) throws IOException {
        URL obj = new URL(BACKEND_URL+comando);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine, firstLine;
            StringBuffer response = new StringBuffer(); //
            firstLine = in.readLine();

            System.out.println("PRIMERA LINEA DE RESPUESTA DEL BACK: "+firstLine);

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // DEBO DEVOLVERLE LA RESPUESTA DEL BACK AL FRONT WEB PARA IMPRIMIRLO
            return firstLine;
        } else {
            System.out.println("GET request not worked");
            return "GET request not worked";
        }
    }

}