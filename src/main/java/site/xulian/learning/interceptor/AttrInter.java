package site.xulian.learning.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import site.xulian.learning.utils.RequestUtil;


public class AttrInter implements Interceptor{
	
	@Override
    public void intercept(Invocation ai) {	
        Controller c = ai.getController();
        c.setAttr("contextPath", RequestUtil.getContextAllPath(c.getRequest()));
        System.out.println(RequestUtil.getContextAllPath(c.getRequest()));
        ai.invoke();
    }
	
}
