package http.packet;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

public class RequestHttpPacket extends HttpPacket
{
    private String requestMethod;

    public String getRequestMethod()
    {
        /**  @return --> HTTP  request method */
        return requestMethod;
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

    public void init(BufferedReader socInput) throws Exception
    {
        /** @param socInput --> It would read the data from socket's input */

        //This method is reading input from the socket and storing it in in the request http packet
        boolean isPost = false;
        int contentLength = 0;

        while (true)
        {
            String temp;

            temp = socInput.readLine();

            if(temp.contains("HTTP/1.1"))
                requestMethod = temp.split(" ")[0]; //Getting the HTTP request method


            if (temp.contains("Content-Length"))
            {
                contentLength = Integer.parseInt(temp.split(" ")[1]); // Getting the length of the input

                if(requestMethod.equals("POST"))
                    isPost = true;
            }

            if (temp.isEmpty())
                break;

            header.add(temp);
        }

        if (isPost) // Reading the post's request body
        {
            StringBuilder data = new StringBuilder();
            char temp;

            while (contentLength > 0)
            {
                temp = (char) socInput.read();
                data.append(temp);
                contentLength--;
            }

            if (data.toString().contains("\n"))
                body = Arrays.asList(data.toString().split("\n"));

            else
                body = Arrays.asList(data.toString().split("&"));
        }
    }

}
