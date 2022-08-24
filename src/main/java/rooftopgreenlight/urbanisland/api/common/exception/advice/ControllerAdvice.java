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
import rooftopgreenlight.urbanisland.domain.common.exception.*;

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
     * 채팅방을 찾을 수 없을 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse NotFoundChatRoomException(NotFoundChatRoomException e) {
        return APIErrorResponse.of(false, ErrorCode.NOT_FOUND, e);
    }

    /**
     * 프로필 조회 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse notFoundProfileException(NotFoundProfileException e) {
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 권한 오류
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIErrorResponse authorizationException(AuthorizationException e) {
        return APIErrorResponse.of(false, ErrorCode.FORBIDDEN, e);
    }

    /**
     * 그린비 신청 조회 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse notFoundGreenBeeException(NotFoundGreenBeeException e) {
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 옥상지기 신청 조회 오류
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse notFoundOwnerException(NotFoundOwnerException e) {
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 옥상 조회 오류
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse notFoundRooftopException(NotFoundRooftopException e) {
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 리뷰 조회 오류
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse notFoundRooftopReviewException(NotFoundRooftopReviewException e) {
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 중복 객체 오류
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse existObjectException(ExistObjectException e){
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * MemberId 불일치 오류
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse noMatchMemberIdException(NoMatchMemberIdException e) {
        return APIErrorResponse.of(false, ErrorCode.BAD_REQUEST, e);
    }

    /**
     * 서버 접근 오류
     * @param e
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIErrorResponse accessException(AccessException e) {
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
        e.printStackTrace();
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
