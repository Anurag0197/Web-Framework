package web.server;

import java.io.File;
import java.net.*;

class RouterObject
{
    protected static Object router;

    protected static void setRoute(File file, String className) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        /** @param file -->  Path of the directory which contains the Router.class file
         *  @param className --> Name of the class which contains the @Route annotation (EX - Router.class)
         * */

        //It is creating the object of the router's class which is written by the user
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
        /**  @return --> It returns the object of the Router class */
        return router;
    }
}


public class WebServer
{
    public WebServer(int port , File file, String className)  // Web Server is continuously listening on a particular port
    {
        /**
         * @param port --> Port on which Server would start to listen the incoming request
         * @param file -->  Path of the directory which contains the Router.class file
         *  @param className --> Name of the class which contains the @Route annotation (EX - Router.class)
         * */

        try
        {
            RouterObject.setRoute(file,className);
            ServerSocket ss = new ServerSocket(port);
            System.out.println("SERVER HAS STARTED ON PORT " +  port);

            while(true)  // Each request is listened and assigned to a separate thread
            {
                Socket s = ss.accept();

                Request request = new Request(s);

                Thread t = new Thread(request);

                t.start();
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        File classPath = new File("/home/an.kumar1/Desktop/test/out/production/test");
        WebServer webServer = new WebServer(5002, classPath,"Router");
    }

}

