package simplehttpserver;

import com.sun.net.httpserver.HttpServer;     
import com.sun.net.httpserver.HttpHandler;    
import com.sun.net.httpserver.HttpExchange;   
import java.io.IOException;                   
import java.io.OutputStream;                  
import java.net.InetSocketAddress;            
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) throws IOException {
       
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
      
        server.createContext("/hello", new HelloHandler());
        server.createContext("/pdf", new PdfHandler());
        server.createContext("/html", new htmlHandler());


       
        server.start();

        System.out.println("Server started on port 8080");
    }
    static class HelloHandler implements HttpHandler {
        @Override
      
        public void handle(HttpExchange exchange) throws IOException {

            exchange.getResponseHeaders().set("Content-Type", "text/plain");

            exchange.sendResponseHeaders(200, 0);

            OutputStream os = exchange.getResponseBody();

            String response = "Hello, this is a simple HTTP server From Parallel Processing Lab!";

            os.write(response.getBytes());

            os.close();
            exchange.close();
        }
    }
    static class PdfHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String filePath = "converted_text.pdf";

        java.io.File file = new java.io.File(filePath);
        if (!file.exists()) {
            String errorMsg = "PDF file not found!";
            exchange.sendResponseHeaders(404, errorMsg.length());
            OutputStream os = exchange.getResponseBody();
            os.write(errorMsg.getBytes());
            os.close();
            return;
        }

        byte[] fileBytes = java.nio.file.Files.readAllBytes(file.toPath());

        exchange.getResponseHeaders().set("Content-Type", "application/pdf");
        exchange.getResponseHeaders().set("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        exchange.sendResponseHeaders(200, fileBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(fileBytes);
        os.close();
    }
}
    
     static class htmlHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");

        String response = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Hello Page</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f5f5f5;
                            color: #333;
                            text-align: center;
                            padding-top: 50px;
                        }
                        h1 {
                            color: #0078D7;
                        }
                    </style>
                </head>
                <body>
                    <h1>Welcome to My Simple HTML Page!</h1>
                    <p>221311006</p>
                </body>
                </html>
                """;

        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(200, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }

        exchange.close();
    }
}


}
