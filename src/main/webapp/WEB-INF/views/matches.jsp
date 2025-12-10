<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Finished Matches</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">

    <script src="js/app.js"></script>
</head>

<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="/index.html">Home</a>
                <a class="nav-link" href="/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <h1>Matches</h1>
        <%--        <div class="input-container">--%>
        <%--            <input class="input-filter" placeholder="Filter by name" type="text" />--%>
        <%--            <div>--%>
        <%--                <a href="/matches">--%>
        <%--                    <button class="btn-filter">Reset Filter</button>--%>
        <%--                </a>--%>
        <%--            </div>--%>
        <%--        </div>--%>
            <form action="/matches" method="get" class="input-container">
                <input class="input-filter" placeholder="Filter by name" type="text" name="filter_by_player_name" value>
                <button type="submit" class="btn-filter">Apply</button>
                <a href="/matches">
                    <button type="button" class="btn-filter">Reset Filter</button>
                </a>
            </form>
        <table class="table-matches">
            <tr>
                <th>Player One</th>
                <th>Player Two</th>
                <th>Winner</th>
            </tr>

            <c:if test="${size > 0}">
                <c:forEach var="match" items="${matches}">
                    <tr>
                        <td>${match.playerOne.name}</td>
                        <td>${match.playerTwo.name}</td>
                        <td><span class="winner-name-td">${match.winner.name}</span></td>
                    </tr>
                </c:forEach>
            </c:if>

            <c:if test="${size == 0}">
                <tr>
                    <td colspan="3">No matches found</td>
                </tr>
            </c:if>
        </table>

        <%--        <div class="pagination">--%>
        <%--            <a class="prev" href="#"> < </a>--%>
        <%--            <a class="num-page current" href="#">1</a>--%>
        <%--            <a class="num-page" href="#">2</a>--%>
        <%--            <a class="num-page" href="#">3</a>--%>
        <%--            <a class="next" href="#"> > </a>--%>
        <%--        </div>--%>
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a class="prev"
                   href="matches?filter_by_player_name=${filter_by_player_name}&page=${currentPage - 1}">
                    &lt;
                </a>
            </c:if>

            <c:forEach var="i" begin="1" end="${noOfPages}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <a class="num-page current" href="#">
                                ${i}
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a class="num-page"
                           href="matches?filter_by_player_name=${filter_by_player_name}&page=${i}">
                                ${i}
                        </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < noOfPages}">
                <a class="next"
                   href="matches?filter_by_player_name=${filter_by_player_name}&page=${currentPage + 1}">
                    &gt;
                </a>
            </c:if>
        </div>

    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
            roadmap.</p>
    </div>
</footer>
</body>
</html>
