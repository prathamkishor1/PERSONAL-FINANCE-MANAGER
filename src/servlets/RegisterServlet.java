package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.DBConnection;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String name = request.getParameter("name");  // ✅ Added name field
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            // ✅ Updated SQL to include the 'name' column
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name, email, password) VALUES (?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);

            int result = stmt.executeUpdate();

            if (result > 0) {
                response.sendRedirect("login.html");  // Redirect to login page on success
            } else {
                out.println("<h3>Registration Failed</h3>");
            }
        } catch (Exception e) {
            out.println("Database error: " + e.getMessage());
        }
    }
}
