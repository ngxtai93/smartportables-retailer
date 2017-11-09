<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*, java.text.NumberFormat" %>
<%@ page import = "tai.entity.Product" %>
<%@ page import = "tai.utils.StringUtilities" %>

<%
    Map<Product, Integer> mapProductAmount = (Map<Product, Integer>) request.getAttribute("mapProductAmount");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    StringUtilities stringUtil = StringUtilities.INSTANCE;
%>

<script type="text/javascript">
    // Load the Visualization API and the corechart package.
    google.charts.load('current', {packages: ['corechart', 'bar']});

    // Set a callback to run when the Google Visualization API is loaded.
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {

        // Create the data table.
        var data = google.visualization.arrayToDataTable([
            ['Product', 'Total Sales',],
            <%  for(Map.Entry<Product, Integer> entry: mapProductAmount.entrySet()) {
                Product p = entry.getKey();
                Integer amount = entry.getValue(); %>
                ['<%=stringUtil.filter(p.getName())%>', <%=(p.getPrice() * amount)%>],
            <% } %>
        ]);
        
        var dataHeight = data.getNumberOfRows() * 30;
        var options = {
            title: 'Sales Chart',
            height: dataHeight,
            chartArea: {width: '50%'},
            hAxis: {
              title: 'Sales',
              minValue: 0
            },
            vAxis: {
              title: 'Product'
            }
          };
    
          var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
    
          chart.draw(data, options);
    }
</script>
<div id="body">
    <section class="content">

        <h4><a href="<%=rootPath%>/account/report/sales/"><b>Back to menu</b></a></h4>
        <div id="chart_div"></div>
        
    </section>
<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>