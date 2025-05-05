<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*, database.DBConnection" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Transactions</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f3f0f9;
            color: #4b0082;
            margin: 0;
            padding: 0;
        }
        h1 {
            text-align: center;
            margin-top: 30px;
            color: #4b0082;
        }
        table {
            width: 80%;
            margin: 30px auto;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0px 0px 10px 2px rgba(75, 0, 130, 0.2);
        }
        th, td {
            padding: 15px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #6a0dad;
            color: white;
        }
        tr:hover {
            background-color: #e0d7f5;
        }
        .button {
            display: block;
            width: 200px;
            margin: 20px auto;
            padding: 10px;
            text-align: center;
            background-color: #6a0dad;
            color: white;
            text-decoration: none;
            border-radius: 8px;
        }
        .button:hover {
            background-color: #4b0082;
        }
    </style>
</head>
<body>

<h1>Your Transactions</h1>

<%
    if (session == null || session.getAttribute("user_id") == null) {
        response.sendRedirect("login.html?error=sessionExpired");
        return;
    } else {
        Integer userId = (Integer) session.getAttribute("user_id");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY date DESC";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>

<table>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Amount</th>
        <th>Type</th>
        <th>Date</th>
    </tr>

<%
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
%>
    <tr>
        <td><%= rs.getInt("id") %></td>
        <td><%= rs.getString("title") %></td>
        <td>₹<%= rs.getBigDecimal("amount") %></td>
        <td><%= rs.getString("type") %></td>
        <td><%= sdf.format(rs.getTimestamp("date")) %></td>
    </tr>
<%
                }
                if (!hasData) {
%>
    <tr>
        <td colspan="5" style="color: gray;">No transactions found.</td>
    </tr>
<%
                }
%>
</table>

<%
            }
        } catch (Exception e) {
            e.printStackTrace();
%>
    <p style="text-align: center; color: red;">Error loading transactions.</p>
<%
        }
    }
%>

<a class="button" href="add_transaction.html">Add Another Transaction</a>
<a class="button" href="dashboard.html">Back to Dashboard</a>

</body>
</html>
