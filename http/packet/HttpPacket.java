package http.packet;

import java.util.ArrayList;
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

    protected List<String> header = new ArrayList<>();
    protected List<String> body;
}