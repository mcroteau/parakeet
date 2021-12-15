Parakeet 
======

Parakeet is a small J2ee Open Source security framework / plugin that boasts quick easy setup.

#### Installation

Add dependency:

```
<dependency>
    <groupId>xyz.goioc</groupId>
    <artifactId>parakeet</artifactId>
    <version>0.10</version>
</dependency>
```

Update `web.xml`, add ParakeetFilter:

```
<filter>
    <filter-name>Parakeet</filter-name>
    <filter-class>perched.support.filters.ParakeetFilter</filter-class>
</filter>

<filter-mapping>
    <filter-name>Parakeet</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

Create an `Accessor`, the class
which provides data to Parakeet.

Example:

```
package perched.support.accessor;

import Accessor;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.goioc.dao.AccountDao;
import xyz.goioc.model.Account;

import qio.annotate.Inject;

import java.util.Set;


public class JdbcAccessor implements Accessor {

    @Inject
    AccountDao accountDao;

    public String getPassword(String user){
        String password = accountDao.getAccountPassword(user);
        return password;
    }

    public Set<String> getRoles(String user){
        Account account = accountDao.findByUsername(user);
        Set<String> roles = accountDao.getAccountRoles(account.getId());
        return roles;
    }

    public Set<String> getPermissions(String user){
        Account account = accountDao.findByUsername(user);
        Set<String> permissions = accountDao.getAccountPermissions(account.getId());
        return permissions;
    }

}
```

Add Cookie xml declaration just in case the container 
doesn't pick up the cookie on authentication. Add to the **web.xml**

```
<session-config>
    <tracking-mode>COOKIE</tracking-mode>
</session-config>
```

Then somewhere in your project during startup call `.configure()` passing 
in the JdbcAccessor.

```
Parakeet.configure(jdbcAccessor)
```

### Parakeet has your JdbcAccessor and your project is configured. 

Now you can use can authenticate your user via :

`Parakeet.login()`

You'll have access to the following methods:

`Parakeet.isAuthenticated()`

`Parakeet.hasRole(role)`

`Parakeet.hasPermission(permission)`

To log out:

`Parakeet.logout()`

### See, we told you it was short!

Oh, we added something new. Taglibs:

You can now check authentication, and get user info 
within jsp without scriptlets.

**First include tag reference:**

`<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>`

**3 available tags:**

Displays when user is anonymous & not authenticated

`<parakeet:isAnonymous></isAnonymous>`

Displays content only when user is authenticated

`<parakeet:isAuthenticated></isAuthenticated>`

Displays username

`<parakeet:username></username>`

A very basic sample servlet app can be viewed within 
the project under `sample-web`

If you want a more, we recommend Apache Shiro! It's a Bull Dog.

