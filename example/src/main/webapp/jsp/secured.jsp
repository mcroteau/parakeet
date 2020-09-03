<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<parakeet:isAuthenticated>
    Welcome back! <parakeet:usernameTag></parakeet:usernameTag>
    <h1>Secured Jsp</h1>
</parakeet:isAuthenticated>
<parakeet:anonymousTag>
    <h1>There is no way you got here...</h1>
</parakeet:anonymousTag>
