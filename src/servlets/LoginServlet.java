package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import database.DBConnection;

@WebServlet("/login")  // mapping
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT id FROM users WHERE email=? AND password=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("id");

                        // ✅ create session and save user_id
                        HttpSession session = request.getSession();
                        session.setAttribute("user_id", userId);

                        response.sendRedirect("dashboard.html");  // go to dashboard
                    } else {
                        response.sendRedirect("login.html?error=invalidCredentials");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=serverError");
        }
    }
}
