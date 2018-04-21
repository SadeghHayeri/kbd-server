package KBD.migrate;

import KBD.Database;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/fetch-houses")
public class FetchHouses extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Database.fetchHouses();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Fetch House!");
    }
}
