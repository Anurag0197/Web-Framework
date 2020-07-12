package http.packet;

public class ResponseHttpPacket extends HttpPacket
{
    public enum ContentType
    {
        TEXT("text/html"), IMAGE("image/jpeg");

        private final String type;

        ContentType(String type) {
            this.type = type;
        }

        public String getType()
        {
            return this.type;
        }
    }

    private String header;
    private String body;

    public ResponseHttpPacket(String response, ContentType type)
    {
        setHeader();
        setBody();
        makePacket(response,type);

    }


    private void setHeader()
    {
        header = "HTTP/1.1 %s\r\n" + "Content-Type: %s\r\n" + "\r\n\r\n";
    }

    private void setBody()
    {
        body = "<html><head><title>%s</title></head><body><h1>%s</h1></body></html>";
    }

    private void makePacket(String response, ContentType type)
    {
        switch (response)
        {
            case "Not Allowed":
                header = String.format(header, HttpStatus.METHOD_NOT_ALLOWED.getStatus(),type.getType());
                body = String.format(body, HttpStatus.METHOD_NOT_ALLOWED.getStatus(), HttpStatus.METHOD_NOT_ALLOWED.getStatus());
                break;

            case "":
                header = String.format(header, HttpStatus.NOT_FOUND.getStatus(),type.getType());
                body = String.format(body, HttpStatus.NOT_FOUND.getStatus(), HttpStatus.NOT_FOUND.getStatus());
                break;

            case "Error":
                header = String.format(header, HttpStatus.INTERNAL_SERVER_ERROR.getStatus(),type.getType());
                body = String.format(body, HttpStatus.INTERNAL_SERVER_ERROR.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.getStatus());
                break;

            default:
                header = String.format(header, HttpStatus.OK.getStatus(),type.getType());
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
