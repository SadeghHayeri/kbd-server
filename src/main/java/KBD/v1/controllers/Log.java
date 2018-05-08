package KBD.v1.controllers;

import KBD.models.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/log")
public class Log extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        // calling the DB to run static methods
        /*try {
            Class.forName("KBD.Database");
        } catch (ClassNotFoundException ignored) {}*/


        PrintWriter out = response.getWriter();
        out.println(Logger.getLog());
    }
}
