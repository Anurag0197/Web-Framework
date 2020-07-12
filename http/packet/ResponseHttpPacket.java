package http.packet;

public class ResponseHttpPacket extends HttpPacket
{
    private String header = "HTTP/1.1 %s\r\n" + "Content-Type: text/html\r\n" + "\r\n\r\n";
    private String body = "<html><head><title>%s</title></head><body><h1>%s</h1></body></html>";

    public void makeResponseHttpPacket(String response)
    {
        /**  @param response --> Data which needs to send to the client */

        //This method is initializing the response http packet for each corresponding response
        switch (response)
        {
            case "Not Allowed":
                header = String.format(header, HttpStatus.METHOD_NOT_ALLOWED.getStatus());
                body = String.format(body, HttpStatus.METHOD_NOT_ALLOWED.getStatus(), HttpStatus.METHOD_NOT_ALLOWED.getStatus());
                break;

            case "":
                header = String.format(header, HttpStatus.NOT_FOUND.getStatus());
                body = String.format(body, HttpStatus.NOT_FOUND.getStatus(), HttpStatus.NOT_FOUND.getStatus());
                break;

            case "Error":
                header = String.format(header, HttpStatus.INTERNAL_SERVER_ERROR.getStatus());
                body = String.format(body, HttpStatus.INTERNAL_SERVER_ERROR.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.getStatus());
                break;

            default:
                header = String.format(header, HttpStatus.OK.getStatus());
                body = response;
                break;
        }
    }

    @Override
    public String toString()
    {
        return header + body;
    }
}
