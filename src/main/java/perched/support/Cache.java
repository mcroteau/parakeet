package perched.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cache {

    private static ThreadLocal<HttpServletRequest> req = new InheritableThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> resp = new InheritableThreadLocal<HttpServletResponse>();

    public static void storeRequest(HttpServletRequest request){
        req.set(request);
    }
    public static void storeResponse(HttpServletResponse response){
        resp.set(response);
    }

    public static HttpServletRequest getRequest(){
        return req.get();
    }
    public static HttpServletResponse getResponse(){
        return resp.get();
    }

}
