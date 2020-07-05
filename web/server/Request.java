package web.server;

import http.packet.HttpPacket;
import routing.Route;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

public class Request implements Runnable
{
    private final Socket s;
    private final BufferedReader socInput;
    private final DataOutputStream socOutput;
    private final HttpPacket httpPacket;

    protected Request(Socket s) throws IOException
    {
        this.s = s;
        this.socInput = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.socOutput = new DataOutputStream(s.getOutputStream());
        this.httpPacket = new HttpPacket();
    }

    public List<String> getPostData()
    {
        return this.httpPacket.getRequestHttpBody();    
    }

    public String getMethod()
    {
        return this.httpPacket.getHttpMethod();
    }

    private String invokeRouter(String path, String httpMethod) throws Exception
    {
        String render = "";
        Object router = RouterObject.getRouter();
        Method[] methods = router.getClass().getDeclaredMethods();

        for(Method method : methods)
        {
            if (method.isAnnotationPresent(Route.class) && method.getAnnotation(Route.class).value().substring(1).equals(path))
            {
                if(Arrays.asList(method.getAnnotation(Route.class).method()).contains(httpMethod))
                {
                    method.setAccessible(true);
                    render =  method.invoke(router,this).toString();
                }

                else
                {
                    render = "Not Allowed";
                }

                break;
            }
        }

        return HttpPacket.makeHttpPacket(render);
    }

    @Override
    public void run()
    {
        try
        {
            httpPacket.initHttpPacket(socInput);
            String httpMethod = "GET";


            if(httpPacket.getRequestHttpHeader().get(0).contains("POST"))
                httpMethod = "POST";

            String path = httpPacket.getRequestHttpHeader().get(0).split(" ")[1].substring(1);
            String responseHttpPacket = invokeRouter(path,httpMethod);

            socOutput.writeBytes(responseHttpPacket);
            socOutput.flush();
            socOutput.close();
            s.close();
        }

        catch (Exception e)
        {
            String httpPacket = HttpPacket.makeHttpPacket("Error");

            try
            {
                socOutput.writeBytes(httpPacket);
                socOutput.flush();
                socOutput.close();
                s.close();
            }

            catch (SocketException ignored)
            {}

            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }
}
