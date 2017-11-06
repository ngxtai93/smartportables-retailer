<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*, java.text.NumberFormat, java.time.format.DateTimeFormatter, java.time.LocalDate" %>
<%@ page import = "tai.entity.Product" %>

<%
    Map<LocalDate, Double> mapSaleByDay = (Map<LocalDate, Double>) request.getAttribute("mapSaleByDay");
    int entryCount = 1;
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("MMMM dd, uuuu");
%>

<div id="body">
    <section class="content">

        <h4><a href="<%=rootPath%>/account/report/sales/"><b>Back to menu</b></a></h4>
        <br>
        <table>
            <tr>
                <th>No.</th>
                <th>Date</th>
                <th>Sales</th>
            </tr>
            <% for(Map.Entry<LocalDate, Double> entry: mapSaleByDay.entrySet()) {
                LocalDate d = entry.getKey();
                Double totalSale = entry.getValue(); %>
                <tr>
                    <th><%=entryCount%></th>
                    <th><%=fullFormatter.format(d)%></th>
                    <th><%=currencyFormatter.format(totalSale)%></th>
                </tr>                
                <% entryCount++; %>
            <% } %>
        </table>
    </section>
<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>