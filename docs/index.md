# Parakeet is a small security framework that allows for quick securing of J2ee applications.

### Installation

Add dependency:

```
<dependency>
    <groupId>io.github.mcroteau</groupId>
    <artifactId>parakeet</artifactId>
    <version>0.1</version>
</dependency>
```

Update `web.xml`, add CacheFilter declaration:

```
<filter>
    <filter-name>Parakeet</filter-name>
    <filter-class>io.github.mcroteau.resources.filters.CacheFilter</filter-class>
</filter>

<filter-mapping>
    <filter-name>Parakeet</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

Create an Accessor. What is an accessor? It is the class
that will provide data to Parakeet. Data includes, password
`Set<String> roles` and `Set<String> permissions`.

Example:

```
package xyz.ioc.accessor;

import io.github.mcroteau.resources.access.Accessor;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ioc.dao.AccountDao;
import xyz.ioc.model.Account;

import java.util.Set;

public class JdbcAccessor implements Accessor {

    @Autowired
    private AccountDao accountDao;

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

Finally wire it up either by:

`Parakeet parakeet = new Parakeet(new JdcbAccessor);`

or if spring project define your beans as such:

```
<bean id="jdbcAccessor" class="com.project.accessor.JdbcAccessor"/>

<bean id="parakeet" class="io.github.mcroteau.Parakeet" scope="singleton">
    <constructor-arg name="accessor" ref="jdbcAccessor"/>
</bean>
```

### Your project is configured. 

Now you can use:

`parakeet.isAuthenticated()`

`parakeet.hasRole(role)`

`parakeet.hasPermission(permission)`

See, we told you it was cute.