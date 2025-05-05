package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import database.DBConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ViewTransactionsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get session (Do NOT create a new one if it doesn't exist)
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.html?error=sessionExpired");
            return;
        }

        // Retrieve user ID from session
        Integer userId = (Integer) session.getAttribute("user_id");

        // Set response type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // HTML Structure and Styling
        out.println("<html><head><title>Transaction History</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f4; }");
        out.println("table { width: 80%; margin: auto; border-collapse: collapse; background: white; box-shadow: 2px 2px 10px gray; }");
        out.println("th, td { padding: 10px; border: 1px solid #ddd; }");
        out.println("th { background-color: #6a0dad; color: white; }");
        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
        out.println("a.button { text-decoration: none; color: white; background-color: #6a0dad; padding: 10px 20px; border-radius: 5px; display: inline-block; margin-top: 15px; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h2>Transaction History</h2>");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT title, amount, type, created_at FROM transactions WHERE user_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                    out.println("<p>No transactions found.</p>");
                } else {
                    out.println("<table>");
                    out.println("<tr><th>Title</th><th>Amount</th><th>Type</th><th>Date</th></tr>");
                    while (rs.next()) {
                        out.println("<tr><td>" + rs.getString("title") + "</td><td>₹" + rs.getDouble("amount") + "</td><td>" + rs.getString("type") + "</td><td>" + rs.getTimestamp("created_at") + "</td></tr>");
                    }
                    out.println("</table>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:red;'>Error fetching transactions. Please try again later.</p>");
        }

        // Back to Dashboard Button
        out.println("<br><a href='dashboard.html' class='button'>Go Back to Dashboard</a>");

        out.println("</body></html>");
    }
}
