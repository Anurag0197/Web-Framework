package sample;

import rendering.template.RenderTemplate;
import routing.Method;
import routing.Route;
import web.server.Request;
import web.server.WebServer;

import java.io.File;
import java.util.List;

public class Router
{
    @Route("/")
    public String home(Request request)
    {
        return "THIS IS A HOMEPAGE!!";
    }

    @Route(value = "/post" , method = Method.POST)
    public List<String> post(Request request)
    {
        List<String> data;

        data= request.getPostData();

        for (String s : data)
            System.out.println(s);

        return data;
    }

    @Route("/get")
    public String get(Request request) throws Exception
    {
        return RenderTemplate.render("index.html");
    }

    @Route(value = "/both", method = {Method.POST , Method.GET})
    public String both(Request request) throws Exception
    {
        if(request.getMethod().equals("POST"))
            return "HI! THIS IS A POST API";

        else
            return "HI! THIS IS A GET API";
    }


    public static void main(String[] args)
    {
        File classPath = new File(Router.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        WebServer webServer = new WebServer(5002, classPath,"Router");
    }
}

