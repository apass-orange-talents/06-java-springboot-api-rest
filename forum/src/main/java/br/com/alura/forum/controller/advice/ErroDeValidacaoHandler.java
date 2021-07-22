package br.com.alura.forum.controller.advice;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import br.com.alura.forum.dto.FormValidationErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public List<FormValidationErrorDto> handler(MethodArgumentNotValidException exception) {
        List<FormValidationErrorDto> erros = new ArrayList();

        exception.getBindingResult().getFieldErrors()
                .forEach(e-> {
                    erros.add(new FormValidationErrorDto(e.getField(), this.messageSource.getMessage(e, LocaleContextHolder.getLocale())));
                });

        return erros;
    }
}
