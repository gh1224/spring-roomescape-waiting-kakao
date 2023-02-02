package nextstep.exceptions.exception;

import org.springframework.http.HttpStatus;

public class DataAlreadyExistException extends RestAPIException {
    public DataAlreadyExistException(String responseMessage) {
        super(responseMessage);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
