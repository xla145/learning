package site.xulian.learning.interceptor;



import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class LoginInter implements Interceptor{
	
	@Override
    public void intercept(Invocation ai) {	
        Controller c = ai.getController();
        String user = (String) c.getSessionAttr("user");
        if(null == user){
        	c.redirect("/login.html");
        	return;
        }
        ai.invoke();
    }
	
}
