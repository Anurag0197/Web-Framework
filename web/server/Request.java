package web.server;

import http.packet.RequestHttpPacket;
import http.packet.ResponseHttpPacket;
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
    private  RequestHttpPacket requestHttpPacket;
    private  ResponseHttpPacket responseHttpPacket;

    protected Request(Socket s) throws IOException
    {
        this.s = s;
        this.socInput = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.socOutput = new DataOutputStream(s.getOutputStream());
        /** @param  Socket --> Taking socket's class object corresponding to each request */
    }


    public List<String> getPostData()
    {
        return requestHttpPacket.getRequestHttpBody();
        /**  @return --> It returns the Post's request body */
    }

    public String getMethod()
    {
        return requestHttpPacket.getRequestMethod();
        /**  @return --> It returns the HTTP request method */
    }

    private void invokeRouter(String path, String httpMethod) throws Exception
    {
        /**  @param path --> Endpoint to which the request should be mapped
         *   @param httpMethod --> HTTP request method
         *
         * */
        //This method is calling the corresponding method of Router class which is written by the user
        String response = "";
        Object router = RouterObject.getRouter();
        Method[] methods = router.getClass().getDeclaredMethods();

        for(Method method : methods)
        {
            //checking @Route annotation is present or not on a method and if present then checking the endpoint of request
            if (method.isAnnotationPresent(Route.class) && method.getAnnotation(Route.class).value().equals(path))
            {
                //checking the request method is allowed or not
                if(Arrays.asList(method.getAnnotation(Route.class).method()).contains(httpMethod))
                {
                    method.setAccessible(true);
                    response =  method.invoke(router,this).toString();// calling the corresponding router's class method
                }

                else
                {
                    response = "Not Allowed";
                }

                break;
            }
        }

        //This method is initializing the response http packet
        responseHttpPacket = new ResponseHttpPacket(response,ResponseHttpPacket.ContentType.TEXT);
    }

    private void getRequest()
    {
        //This method is initializing the request http packet and calling the invokeRouter method
        try
        {
            requestHttpPacket = new RequestHttpPacket(socInput);
            String path = requestHttpPacket.getRequestHttpHeader().get(0).split(" ")[1];
            invokeRouter(path,requestHttpPacket.getRequestMethod());
        }

        catch (Exception e)
        {
            responseHttpPacket = new ResponseHttpPacket("Error",ResponseHttpPacket.ContentType.TEXT);
        }
    }

    private void sendResponse() throws IOException
    {
        //This method method is sending response http packet to the client
        socOutput.writeBytes(responseHttpPacket.toString());
        socOutput.flush();
        socOutput.close();
        s.close();
    }

    @Override
    public void run()
    {
        //  This method is called for each request and send the response to the client for each corresponding request
        try
        {
            getRequest();
            sendResponse();
        }

        catch (SocketException ignored){}

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
