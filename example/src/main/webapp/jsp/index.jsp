<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<parakeet:isAuthenticated>
    Welcome back! <parakeet:usernameTag></parakeet:usernameTag>
</parakeet:isAuthenticated>
<parakeet:anonymousTag>
    <a href="/b/signin">Signin</a>
</parakeet:anonymousTag>


