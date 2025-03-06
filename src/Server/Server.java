package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5500);
        System.out.println("Server started on port: " + server.getLocalPort());
        while (true) { //this will always run
            try (Socket socket = server.accept()) { //This will try to set socket to the server obj accepted
                handleClient(socket); //This calls the method that will handle the client
            }
        }
    }

    //This handles the client
    private static void handleClient(Socket socket) throws IOException {
        System.out.println("Client Connected " + socket.toString());

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//This will read the input stream from the socket as a buffer
        //This creates an object of the bufferedReader called in
        //It increase efficiency of reading text from an input stream
        //It does this by using a buffer and reading large chunks at a time rather than one char at a time
        //It minimise the amount of times a file needs to be accessed

        StringBuilder buildRequests = new StringBuilder(); //This will create a string for the request from the client
        String line;
        while (!(line = in.readLine()).isBlank()) {//Runs while there is an input from the socket input
            buildRequests.append(line + "\r\n");//Adds the socket input to the request string
        }

        String request = buildRequests.toString(); //This converts the request to a string
        String[] requestParts = request.split("\r\n"); //This splits the string by the structure of the request/response
        String[] requestPart = requestParts[0].split(" "); //Since in the request the parts we need are in the first line we need to split the first element
        String method = requestPart[0]; //This is what the client is trying to do (GET, POST, ect..)
        String path = requestPart[1]; //The path that they are requesting
        String version = requestPart[2]; //The version of HTTP
        String host = requestPart[1].split(" ")[0]; //The host of the client trying to access from

        List<String> headers = new ArrayList<>(); //Creates String List that will store the header
        for (int i = 2; i < requestParts.length; i++) {//Adds rest of the request to the string Headers
            String header = requestParts[i];
            headers.add(header);
        }
        String accessLog = String.format("Client: %s, Method: %s, Path: %s, Version: %s, Host: %s, Headers: %s", socket.toString(), method, path, version, host, headers.toString());
        System.out.println(accessLog);//This will display all the parts from the request.

        System.out.println("Here1");
        Path filePath = getFilePath(path);
        if (Files.exists(filePath)) {//Checks to see if the file in the filepath exsists
            String contentType = Files.probeContentType(filePath); //Checks to see what the type of the file is
            response(socket, "200 OK", contentType, Files.readAllBytes(filePath));
        } else {
            byte[] notFoundContent = "<h1>Not Found</h1>".getBytes(StandardCharsets.UTF_8);
            response(socket, "404 Not Found", "text/html", notFoundContent);
        }
    }

    private static Path getFilePath(String path) {//This method gets the file path that the client requests
        if ("/".equals(path)) {//This will check if there is a specified path added to the host.
            path = "/login.html"; //Path of the html page for the home page
        }
        else{

        }
        return Paths.get("HTML_FILES/Login", path);
    }

    private static void response(Socket socket, String status, String contentType, byte[] context) throws IOException {
        OutputStream outputToClient = socket.getOutputStream(); //creates an output stream to send to client using the socket
        outputToClient.write(("HTTP/1.1 \r\n" + status).getBytes(StandardCharsets.UTF_8));//Sends an OK response specified to use the ASCII character set
        outputToClient.write(("Content-Type:" + contentType + "\r\n").getBytes(StandardCharsets.UTF_8));//Sends the type of the file that was found at the directory
        outputToClient.write(("\r\n").getBytes(StandardCharsets.UTF_8));//This will send a blank line so the response structure is correct
        outputToClient.write(context);//This will send the content of the file found in the selected directory.
        outputToClient.write("\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        outputToClient.flush();//This sends it all to the user.
    }

}
