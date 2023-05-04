package rooftopgreenlight.urbanisland.exception.advice;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import rooftopgreenlight.urbanisland.exception.JwtException;
import rooftopgreenlight.urbanisland.exception.ErrorCode;
import rooftopgreenlight.urbanisland.dto.APIErrorResponse;
import rooftopgreenlight.urbanisland.exception.ClientException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Clinet 측 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse clientException(ClientException e) {
        log.error("ClientException = ", e);

        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * notFound 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse notFoundException(NotFoundException e) {
        log.error("NotFoundException = ", e);

        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * JWT 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse exception(JwtException e) {
        log.error("JwtException = ", e);

        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 최상위 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIErrorResponse exception(Exception e) {
        log.error("Exception = {}", e);

        return APIErrorResponse.of(false, ErrorCode.INTERNAL_SEVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorCode errorCode = status.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_SEVER_ERROR;

        List<String> errorList = new ArrayList<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException) ex).getFieldErrors().forEach(
                    fieldError -> errorList.add(fieldError.getDefaultMessage())
            );
        } else {
            errorList.add(ex.getMessage());
        }

        log.info("ControllerAdvice Exception = ", ex);
        return super.handleExceptionInternal(ex, APIErrorResponse.of(false, errorCode.getCode(), errorList), headers, status, request);
    }
}
