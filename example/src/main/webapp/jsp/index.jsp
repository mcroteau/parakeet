<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<h1>Signin</h1>

<parakeet:anonymous>
    <%if(request.getAttribute("message") != null &&
            !request.getAttribute("message").equals("")){%>
        <p><%=request.getAttribute("message")%></p>
    <%}%>

    <form action="/b/auth" method="post">
        <label>Username</label>
        <input type="text" name="username" placeholder="Username" value="mockyah"/>
        <br/>
        <label>Password</label>
        <input type="text" name="password" placeholder="Password ***" value="birdyah"/>
        <br/>
        <input type="submit" value="Signin"/>
    </form>
</parakeet:anonymous>
<parakeet:isAuthenticated>
    <a href="/b/signout">Signout</a>
</parakeet:isAuthenticated>