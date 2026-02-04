package javaapplication13;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.net.InetSocketAddress;

public class SimplePdfServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/pdf", new PdfHandler());
        server.start();
        System.out.println("Server started on http://localhost:8080/pdf");
    }

    static class PdfHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File pdfFile = new File("C:\\Users\\CSE\\Desktop\\Marksheet_2.pdf");

            if (!pdfFile.exists()) {
                String notFound = "PDF file not found!";
                exchange.sendResponseHeaders(404, notFound.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound.getBytes());
                }
                return;
            }

            // Set the correct content type
            exchange.getResponseHeaders().set("Content-Type", "application/pdf");
            exchange.sendResponseHeaders(200, pdfFile.length());

            // Stream the PDF file
            try (OutputStream os = exchange.getResponseBody();
                 FileInputStream fis = new FileInputStream(pdfFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
