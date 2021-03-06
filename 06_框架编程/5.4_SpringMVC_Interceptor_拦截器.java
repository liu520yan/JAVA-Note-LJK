* SpringMVC中使用Interceptor拦截器
    
    SpringMVCInterceptor 拦截器 HandlerInterceptorWebRequestInterceptor 

    SpringMVC 中的 Interceptor 拦截器也是相当重要和相当有用的，
        它的主要作用是拦截用户的请求并进行相应的处理。
        比如通过它来进行权限验证，或者是来判断用户是否登陆，
        或者是像12306 那样子判断当前时间是否是购票时间。

一、定义Interceptor实现类
    SpringMVC 中的 Interceptor 拦截请求是通过 HandlerInterceptor 来实现的。
    在 SpringMVC 中定义一个 Interceptor 非常简单，主要有两种方式，
        * 第一种 方式是要定义的 Interceptor 类要实现了 Spring 的 HandlerInterceptor 接口，
                或者是这个类继承实现了 HandlerInterceptor 接口的类，
                比如 Spring 已经提供的实现了 HandlerInterceptor 接口的抽象类 
                HandlerInterceptorAdapter ；
        * 第二种 方式是实现 Spring 的 WebRequestInterceptor 接口，
                或者是继承实现了 WebRequestInterceptor的类。

（一）实现 HandlerInterceptor 接口
    
    HandlerInterceptor 接口中定义了三个方法，我们就是通过这三个方法来对用户的请求进行拦截处理的。

   （1）preHandle (HttpServletRequest request, HttpServletResponse response, Object handle) 方法，
        顾名思义，该方法将在请求处理 "之前" 进行调用。
        SpringMVC 中的 Interceptor 是链式的调用的，
        
        在一个应用中或者说是在一个请求中可以同时存在多个 Interceptor。
        每个 Interceptor 的调用会依据它的声明顺序依次执行，
        而且最先执行的都是 Interceptor 中的 preHandle 方法，
        
        所以可以在这个方法中进行一些前置初始化操作或者是对当前请求的一个预处理，
        也可以在这个方法中进行一些判断来决定请求是否要继续进行下去。
        
        该方法的返回值是布尔值 Boolean 类型的，当它返回为 false 时，表示请求结束，
        后续的 Interceptor 和 Controller 都不会再执行；
        
        当返回值为 true 时就会继续调用下一个 Interceptor 的 preHandle 方法，
        如果已经是最后一个 Interceptor 的时候就会是调用当前请求的 Controller 方法。

   （2）postHandle (HttpServletRequest request, HttpServletResponse response, Object handle, ModelAndView modelAndView) 方法，
        由 preHandle 方法的解释我们知道这个方法包括后面要说到的 afterCompletion 方法
        都只能是在当前所属的 Interceptor 的 preHandle 方法的返回值为 true 时才能被调用。

        postHandle 方法，顾名思义就是在当前请求进行处理之后，也就是 Controller 方法调用之后执行，
        但是它会在 DispatcherServlet 进行视图返回渲染之前被调用，
        所以我们可以在这个方法中对 Controller 处理之后的 ModelAndView 对象进行操作。

        postHandle 方法被调用的方向跟 preHandle 是相反的，也就是说
        先声明的 Interceptor 的 postHandle 方法反而会后执行，
        这和 Struts2 里面的 Interceptor 的执行过程有点类型。
        Struts2 里面的 Interceptor 的执行过程也是链式的，
        只是在 Struts2 里面需要手动调用 ActionInvocation 的 invoke 方法来
        触发对下一个 Interceptor 或者是 Action 的调用，
        然后每一个 Interceptor 中在 invoke 方法调用之前的内容都是按照声明顺序执行的，
        而 invoke 方法之后的内容就是反向的。

   （3）afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handle, Exception ex) 方法，
        该方法也是需要当前对应的 Interceptor 的 preHandle 方法的返回值为 true 时才会执行。
        顾名思义，该方法将在整个请求结束之后，也就是在 DispatcherServlet 渲染了对应的视图之后执行。
        这个方法的主要作用是用于进行资源清理工作的。

    * 下面是一个简单的代码说明：
          
        public class SpringMVCInterceptor implements HandlerInterceptor {        
            /** 
             * preHandle 方法是进行处理器拦截用的，顾名思义，该方法将在 Controller 处理之前进行调用，
             * SpringMVC 中的 Interceptor 拦截器是链式的，可以同时存在 
             * 多个 Interceptor，然后 SpringMVC 会根据声明的前后顺序一个接一个的执行，
             * 而且所有的 Interceptor中 的 preHandle 方法都会在 
             * Controller 方法调用之前调用。
             * SpringMVC 的这种 Interceptor 链式结构也是可以进行中断的，
             * 这种中断方式是令preHandle的返 
             * 回值为false，当preHandle的返回值为false的时候整个请求就结束了。 
             */  
            @Override  
            public boolean preHandle(HttpServletRequest request,  
                    HttpServletResponse response, Object handler) throws Exception {  
                // TODO Auto-generated method stub  
                return false;  
            }  
              
            /** 
             * 这个方法只会在当前这个 Interceptor 的 preHandle 方法返回值为 true 的时候才会执行。
             * postHandle 是进行处理器拦截用的，它的执行时间是在处理器进行处理之 
             * 后，也就是在Controller的方法调用之后执行，
             * 但是它会在 DispatcherServlet 进行视图的渲染之前执行，
             * 也就是说在这个方法中你可以对 ModelAndView 进行操作。
             * 
             * 这个方法的链式结构跟正常访问的方向是相反的，
             * 也就是说先声明的 Interceptor 拦截器该方法反而会后调用
             */  
            @Override  
            public void postHandle(HttpServletRequest request,  
                    HttpServletResponse response, Object handler,  
                    ModelAndView modelAndView) throws Exception {  
                // TODO Auto-generated method stub  
                  
            }  
          
            /** 
             * 该方法也是需要当前对应的 Interceptor 的 preHandle 方法的返回值为 true 时才会执行。
             * 该方法将在整个请求完成之后，也就是 DispatcherServlet 渲染了视图执行， 
             * 这个方法的主要作用是用于清理资源的，
             * 当然这个方法也只能在当前这个 Interceptor 的 preHandle 方法的返回值为 true 时才会执行。 
             */  
            @Override  
            public void afterCompletion(HttpServletRequest request,  
                    HttpServletResponse response, Object handler, Exception ex)  
            throws Exception {  
                // TODO Auto-generated method stub  
            }  
        }  

 
（二）实现WebRequestInterceptor 接口
        WebRequestInterceptor 中也定义了三个方法，我们也是通过这三个方法来实现拦截的。
        这三个方法都传递了同一个参数 WebRequest ，那么这个 WebRequest 是什么呢？
        这个 WebRequest 是 Spring 定义的一个接口，它里面的方法定义都基本跟 HttpServletRequest 一样，
        在 WebRequestInterceptor 中对 WebRequest 进行的所有操作都将同步到 HttpServletRequest 中，
        然后在当前请求中一直传递。

   （1）preHandle(WebRequest request) 方法。
        该方法将在请求处理之前进行调用，也就是说会在 Controller 方法调用之前被调用。
        这个方法跟 HandlerInterceptor 中的 preHandle 是不同的，

        主要区别在于该方法的返回值是 void ，也就是没有返回值，
        所以我们一般主要用它来进行资源的准备工作，
        比如我们在使用 Hibernate 的时候可以在这个方法中准备一个 Hibernate 的 Session 对象，
        然后利用 WebRequest 的 setAttribute(name, value, scope) 把它放到 WebRequest 的属性中。

        这里可以说说这个 setAttribute 方法的第三个参数 scope ，该参数是一个 Integer 类型的。
        在 WebRequest 的父层接口 RequestAttributes 中对它定义了三个常量：

           SCOPE_REQUEST ：
                    它的值是0 ，代表只有在request 中可以访问。
           SCOPE_SESSION ：
                    它的值是1 ，如果环境允许的话它代表的是一个局部的隔离的 session，
                    否则就代表普通的 session，并且在该 session 范围内可以访问。
           SCOPE_GLOBAL_SESSION ：
                    它的值是2 ，如果环境允许的话，它代表的是一个全局共享的 session，
                    否则就代表普通的 session，并且在该 session 范围内可以访问。

   （2）postHandle(WebRequest request, ModelMap model) 方法。
        该方法将在请求处理之后，也就是在Controller 方法调用之后被调用，但是会在视图返回被渲染之前被调用，所以可以在这个方法里面通过改变数据模型ModelMap 来改变数据的展示。该方法有两个参数，WebRequest 对象是用于传递整个请求数据的，比如在preHandle 中准备的数据都可以通过WebRequest 来传递和访问；ModelMap 就是Controller 处理之后返回的Model 对象，我们可以通过改变它的属性来改变返回的Model 模型。

   （3）afterCompletion(WebRequest request, Exception ex) 方法。
        该方法会在整个请求处理完成，也就是在视图返回并被渲染之后执行。所以在该方法中可以进行资源的释放操作。而WebRequest 参数就可以把我们在preHandle 中准备的资源传递到这里进行释放。Exception 参数表示的是当前请求的异常对象，如果在Controller 中抛出的异常已经被Spring 的异常处理器给处理了的话，那么这个异常对象就是是null 。

        * 下面是一个简单的代码说明：

        import org.springframework.ui.ModelMap;  
        import org.springframework.web.context.request.WebRequest;  
        import org.springframework.web.context.request.WebRequestInterceptor;  
          
        public class AllInterceptor implements WebRequestInterceptor {  
              
            /** 
             * 在请求处理之前执行，该方法主要是用于准备资源数据的，然后可以把它们当做请求属性放到WebRequest中 
             */  
            @Override  
            public void preHandle(WebRequest request) throws Exception {  
                // TODO Auto-generated method stub  
                System.out.println("AllInterceptor...............................");  
                request.setAttribute("request", "request", WebRequest.SCOPE_REQUEST);//这个是放到request范围内的，所以只能在当前请求中的request中获取到  
                request.setAttribute("session", "session", WebRequest.SCOPE_SESSION);//这个是放到session范围内的，如果环境允许的话它只能在局部的隔离的会话中访问，否则就是在普通的当前会话中可以访问  
                request.setAttribute("globalSession", "globalSession", WebRequest.SCOPE_GLOBAL_SESSION);//如果环境允许的话，它能在全局共享的会话中访问，否则就是在普通的当前会话中访问  
            }  
          
            /** 
             * 该方法将在Controller执行之后，返回视图之前执行，ModelMap表示请求Controller处理之后返回的Model对象，所以可以在 
             * 这个方法中修改ModelMap的属性，从而达到改变返回的模型的效果。 
             */  
            @Override  
            public void postHandle(WebRequest request, ModelMap map) throws Exception {  
                // TODO Auto-generated method stub  
                for (String key:map.keySet())  
                    System.out.println(key + "-------------------------");;  
                map.put("name3", "value3");  
                map.put("name1", "name1");  
            }  
          
            /** 
             * 该方法将在整个请求完成之后，也就是说在视图渲染之后进行调用，主要用于进行一些资源的释放 
             */  
            @Override  
            public void afterCompletion(WebRequest request, Exception exception)  
            throws Exception {  
                // TODO Auto-generated method stub  
                System.out.println(exception + "-=-=--=--=-=-=-=-=-=-=-=-==-=--=-=-=-=");  
            }  
              
        }  

二、把定义的拦截器类加到SpringMVC的拦截体系中

    1.在SpringMVC的配置文件中加上支持MVC的schema

        xmlns:mvc="http://www.springframework.org/schema/mvc"  
        xsi:schemaLocation=" http://www.springframework.org/schema/mvc  
            http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd"  

    * 下面是我的声明示例：

        <beans xmlns="http://www.springframework.org/schema/beans"  
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"  
            xmlns:mvc="http://www.springframework.org/schema/mvc"  
            xsi:schemaLocation="http://www.springframework.org/schema/beans  
             http://www.springframework.org/schema/beans/spring-beans-3.0.xsd  
             http://www.springframework.org/schema/context  
             http://www.springframework.org/schema/context/spring-context-3.0.xsd  
             http://www.springframework.org/schema/mvc  
             http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">  

        这样在 SpringMVC 的配置文件中就可以使用 mvc 标签了，
        mvc 标签中有一个 mvc:interceptors 是用于声明SpringMVC的拦截器的。
 
（二）使用mvc:interceptors标签来声明需要加入到SpringMVC拦截器链中的拦截器

    <mvc:interceptors>  
        <!-- 使用bean定义一个Interceptor，直接定义在mvc:interceptors根下面的Interceptor将拦截所有的请求 -->  
        <bean class="com.host.app.web.interceptor.AllInterceptor"/>  
        <mvc:interceptor>  
            <mvc:mapping path="/test/number.do"/>  
            <!-- 定义在mvc:interceptor下面的表示是对特定的请求才进行拦截的 -->  
            <bean class="com.host.app.web.interceptor.LoginInterceptor"/>  
        </mvc:interceptor>  
    </mvc:interceptors>  

    由上面的示例可以看出可以利用 mvc:interceptors 标签声明一系列的拦截器，
    然后它们就可以形成一个拦截器链，拦截器的执行顺序是按声明的先后顺序执行的，

    先声明的拦截器中的 preHandle 方法会先执行，
    然而它的 postHandle 方法和 afterCompletion 方法却会后执行。

    在 mvc:interceptors 标签下声明 interceptor 主要有两种方式：
        （1）直接定义一个 Interceptor 实现类的 bean 对象。
            使用这种方式声明的 Interceptor 拦截器将会对所有的请求进行拦截。
        （2）使用 mvc:interceptor 标签进行声明。
            使用这种方式进行声明的 Interceptor 可以通过 mvc:mapping 子标签来定义需要进行拦截的请求路径。

    经过上述两步之后，定义的拦截器就会发生作用对特定的请求进行拦截了。
