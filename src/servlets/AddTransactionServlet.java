package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.DBConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/addTransaction")
public class AddTransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.html?error=sessionExpired");
            return;
        }

        Integer userId = (Integer) session.getAttribute("user_id");
        String title = request.getParameter("title");
        String amountStr = request.getParameter("amount");
        String type = request.getParameter("type");

        if (title == null || title.trim().isEmpty() || amountStr == null || type == null) {
            response.sendRedirect("add_transaction.html?error=empty_fields");
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                response.sendRedirect("add_transaction.html?error=invalid_amount");
                return;
            }
        } catch (NumberFormatException ex) {
            response.sendRedirect("add_transaction.html?error=invalid_amount");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO transactions (user_id, title, amount, type, date) VALUES (?, ?, ?, ?, NOW())";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setBigDecimal(3, amount);
                ps.setString(4, type);

                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    response.sendRedirect("view_transactions.jsp?success=added");
                } else {
                    response.sendRedirect("add_transaction.html?error=insert_failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("add_transaction.html?error=database_error");
        }
    }
}
