<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<h1>Signin</h1>

<form action="/b/auth" method="post">
    <label>Username</label>
    <input type="text" name="username" placeholder="Username" value="mockyah"/>
    <br/>
    <label>Password</label>
    <input type="text" name="password" placeholder="Password ***" value="birdyah"/>
    <br/>
    <input type="submit" value="Signin"/>
</form>

<parakeet:anonymous>
    Anonymous content
</parakeet:anonymous>