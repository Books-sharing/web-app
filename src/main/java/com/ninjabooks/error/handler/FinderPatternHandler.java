package com.ninjabooks.error.handler;

import com.ninjabooks.controller.AccountController;
import com.ninjabooks.controller.AuthenticationController;
import com.ninjabooks.json.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ControllerAdvice(basePackageClasses = {AccountController.class, AuthenticationController.class})
public class FinderPatternHandler
{
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> wrongSecurityPattern(HttpServletRequest request, IllegalArgumentException e) throws Exception {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                e,
                request.getRequestURI()));
    }
}
