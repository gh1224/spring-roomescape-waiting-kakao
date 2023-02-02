package nextstep.exceptions.exception;

import org.springframework.http.HttpStatus;

public class DataNotExistException extends RestAPIException {
    public DataNotExistException(String responseMessage) {
        super(responseMessage);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
