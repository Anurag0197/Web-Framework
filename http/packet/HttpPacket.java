package http.packet;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpPacket
{
    enum HttpStatus
    {
        OK("200 OK"), NOT_FOUND("404 NOT FOUND"), INTERNAL_SERVER_ERROR("500 INTERNAL SERVER ERROR"), METHOD_NOT_ALLOWED("405 METHOD NOT ALLOWED");
        private final String status;

        HttpStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }
    }

    private final List<String> requestHttpHeader = new ArrayList<>();
    private List<String> requestHttpBody;
    private String httpMethod = "GET";

    private final static String responseHttpHeader = "HTTP/1.1 %s\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n";

//    private final static String responseHttpHeader = "HTTP/1.1 %s\r\n" + "Content-Type: image/jpg\r\n" + "\r\n\r\n";

    private final static String responseHttpBody = "<html><head><title>%s</title></head><body><h1>%s</h1></body></html>";

    public void initHttpPacket(BufferedReader socInput) throws Exception
    {
        boolean isPost = false;
        int contentLength = 0;

        while (true)
        {
            String temp;

            temp = socInput.readLine();

            if (temp.contains("Content-Length"))
            {
                contentLength = Integer.parseInt(temp.split(" ")[1]);
                isPost = true;
            }

            if (temp.isEmpty())
                break;

            requestHttpHeader.add(temp);
        }

        if (isPost) {
            httpMethod = "POST";
            StringBuilder data = new StringBuilder();
            char temp;

            while (contentLength > 0) {
                temp = (char) socInput.read();
                data.append(temp);
                contentLength--;
            }

            if (data.toString().contains("\n"))
                requestHttpBody = Arrays.asList(data.toString().split("\n"));

            else
                requestHttpBody = Arrays.asList(data.toString().split("&"));
        }
    }

    public List<String> getRequestHttpHeader()
    {
        return requestHttpHeader;
    }

    public String getHttpMethod()
    {
        return httpMethod;
    }

    public List<String> getRequestHttpBody()
    {
        return requestHttpBody;
    }

    public static String makeHttpPacket(String content)
    {
        switch (content)
        {
            case "Not Allowed":
                return String.format(responseHttpHeader, HttpStatus.METHOD_NOT_ALLOWED.getStatus()) + String.format(responseHttpBody, HttpStatus.METHOD_NOT_ALLOWED.getStatus(), HttpStatus.METHOD_NOT_ALLOWED.getStatus());
            case "":
                return String.format(responseHttpHeader, HttpStatus.NOT_FOUND.getStatus()) + String.format(responseHttpBody, HttpStatus.NOT_FOUND.getStatus(), HttpStatus.NOT_FOUND.getStatus());
            case "Error":
                return String.format(responseHttpHeader, HttpStatus.INTERNAL_SERVER_ERROR.getStatus()) + String.format(responseHttpBody, HttpStatus.INTERNAL_SERVER_ERROR.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.getStatus());
            default:
                return String.format(responseHttpHeader, HttpStatus.OK.getStatus()) + content;
        }
    }
}