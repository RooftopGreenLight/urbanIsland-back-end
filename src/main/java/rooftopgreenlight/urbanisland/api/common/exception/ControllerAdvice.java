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

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto validationError(MethodArgumentNotValidException e){
        ErrorDto errorDto = createError("회원 가입 실패!");
        e.getFieldErrors().forEach(
                error -> errorDto.getErrors().add(new ErrorDetailDto(error.getField(), error.getDefaultMessage()))
        );
        return new ResponseDto(errorDto);
    }

    private ErrorDto createError(String errorMessage) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorMessage(errorMessage);
        return errorDto;
    }
}
