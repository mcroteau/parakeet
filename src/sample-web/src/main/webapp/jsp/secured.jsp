<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<parakeet:isAuthenticated>
    Welcome back! <parakeet:username></parakeet:username>
    <h1>Secured Jsp</h1>
    <a href="/b/signout">Signout</a>
</parakeet:isAuthenticated>
