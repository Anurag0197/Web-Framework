package web.server;

import java.io.File;
import java.io.IOException;
import java.net.*;



class RouterObject
{
    protected static Object router;

    protected static void setRoute(File file, String className) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        if(router == null)
        {
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            URLClassLoader classLoad = new URLClassLoader(urls);
            Class<?> Router= classLoad.loadClass(className);
            router = Router.newInstance();
        }
    }

    protected static Object getRouter()
    {
        return router;
    }
}


public class WebServer
{
    ServerSocket ss;

    public WebServer(int port , File file, String className)
    {
        try
        {
            RouterObject.setRoute(file,className);
            ss = new ServerSocket(port);
            System.out.println("SERVER HAS STARTED ON PORT " +  port);

            while(true)
            {
                Socket s = ss.accept();

                Request c = new Request(s);

                Thread t = new Thread(c);

                t.start();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
