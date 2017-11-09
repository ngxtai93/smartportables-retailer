<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*" %>
<%@ page import = "tai.entity.Product" %>
<%@ page import = "tai.utils.StringUtilities" %>

<%
	List<Product> listAllProduct = (List<Product>) request.getAttribute("listAllProduct");
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
            ['Product', 'Amount',],
            <% for(Product p: listAllProduct) { %>
                ['<%=stringUtil.filter(p.getName())%>', <%=p.getAmount()%>],
            <% } %>
        ]);
        
        var dataHeight = data.getNumberOfRows() * 30;
        var options = {
            title: 'Product Amount Chart',
            height: dataHeight,
            chartArea: {width: '50%'},
            hAxis: {
              title: 'Amount',
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

        <h4><a href="<%=rootPath%>/account/report/inventory/"><b>Back to menu</b></a></h4>
        <div id="chart_div"></div>
        
    </section>
<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>