package rooftopgreenlight.urbanisland.api.common.exception.advice;

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
import rooftopgreenlight.urbanisland.api.common.exception.*;
import rooftopgreenlight.urbanisland.api.common.exception.error.ErrorCode;
import rooftopgreenlight.urbanisland.api.controller.dto.APIErrorResponse;
import rooftopgreenlight.urbanisland.domain.exception.NotFoundMemberException;
import rooftopgreenlight.urbanisland.domain.exception.NotFoundProfileException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * 회원을 찾을 수 없을 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse notFoundMemberException(NotFoundMemberException e) {
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 이미 존재하는 회원일 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse duplicatedMemberException(DuplicatedMemberException e) {
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 이메일 전송 오류 발생했을 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse mailSendException(MailSendException e) {
        return APIErrorResponse.of(false, ErrorCode.MAIL_SEND_ERROR, e);
    }

    /**
     * refresh-token이 만료되었을 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse ExpiredRefreshTokenException(ExpiredRefreshTokenException e) {
        return APIErrorResponse.of(false, ErrorCode.JWT_REFRESH_ERROR, e);
    }

    /**
     * DB에 저장된 refresh-token과 일치하지 않을 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse NotMatchedRefreshTokenException(NotMatchedRefreshTokenException e) {
        return APIErrorResponse.of(false, ErrorCode.JWT_REFRESH_ERROR, e);
    }

    /**
     * 회원 Profile 저장 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIErrorResponse fileIOException(FileIOException e) {
        return APIErrorResponse.of(false, ErrorCode.FILE_IO_ERROR, e);
    }

    /**
     * 최상위 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse notFoundProfileException(NotFoundProfileException e) {
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
        return APIErrorResponse.of(false, ErrorCode.INTERNAL_SEVER_ERROR, e);
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

        log.info("ControllerAdvice Exception = {}", ex);

        return super.handleExceptionInternal(ex, APIErrorResponse.of(false, errorCode.getCode(), errorList), headers, status, request);
    }
}
