package http.packet;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

public class RequestHttpPacket extends HttpPacket
{
    private String requestMethod;
    private int contentLength;

    public RequestHttpPacket(BufferedReader socInput) throws Exception
    {
        initHeader(socInput);
        initBody(socInput);
    }

    private void initHeader(BufferedReader socInput) throws Exception
    {
        String temp = "temp init";

        while (!temp.isEmpty())
        {
            temp = socInput.readLine();
            header.add(temp);

            if(temp.contains("HTTP/1.1"))
                requestMethod = temp.split(" ")[0]; //Getting the HTTP request method


            if (temp.contains("Content-Length"))
            {
                contentLength = Integer.parseInt(temp.split(" ")[1]); // Getting the length of the input
            }
        }

    }

    private void initBody(BufferedReader socInput) throws Exception
    {
        switch (requestMethod) // Reading the post's request body
        {
            case "POST":
                StringBuilder data = new StringBuilder();

                for(int i = 0 ; i < contentLength ; ++i)
                {
                    data.append((char) socInput.read());
                }

                if (data.toString().contains("\n"))
                    body = Arrays.asList(data.toString().split("\n"));

                else
                    body = Arrays.asList(data.toString().split("&"));

                break;
        }
    }

    public String getRequestMethod()
    {
        /**  @return --> HTTP  request method */
        return requestMethod;
    }

    public int getContentLength()
    {
        /**  @return --> HTTP  request method */
        return contentLength;
    }

    public List<String> getRequestHttpHeader()
    {
        /**  @return --> HTTP  header */
        return header;
    }

    public List<String> getRequestHttpBody()
    {
        /**  @return --> HTTP body */
        return body;
    }
}
