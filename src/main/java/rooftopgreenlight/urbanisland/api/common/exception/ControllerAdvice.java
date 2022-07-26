package rooftopgreenlight.urbanisland.api.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rooftopgreenlight.urbanisland.api.common.exception.dto.ErrorDetailDto;
import rooftopgreenlight.urbanisland.api.common.exception.dto.ErrorDto;
import rooftopgreenlight.urbanisland.api.controller.dto.ResponseDto;
import rooftopgreenlight.urbanisland.domain.exception.NotFoundMemberException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * 요청 데이터 검증 오류 시
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto validationError(MethodArgumentNotValidException e) {
        ErrorDto errorDto = createError("회원 가입 실패!");
        e.getFieldErrors().forEach(
                error -> errorDto.getErrors().add(new ErrorDetailDto(error.getField(), error.getDefaultMessage()))
        );
        return ResponseDto.of(errorDto);
    }

    /**
     * 회원을 찾을 수 없을 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto notFoundMemberException(NotFoundMemberException e) {
        return ResponseDto.of(createError(e.getMessage()));
    }

    /**
     * 이미 존재하는 회원일 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto duplicatedMemberException(DuplicatedMemberException e) {
        return ResponseDto.of(createError(e.getMessage()));
    }

    /**
     * 이메일 전송 오류 발생했을 때
     * @param e
     * @return 에러 정보 전달
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto mailSendException(MailSendException e) {
        return ResponseDto.of(createError(e.getMessage()));
    }

    private ErrorDto createError(String errorMessage) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorMessage(errorMessage);
        return errorDto;
    }
}
