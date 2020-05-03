package com.sg.shopping.exception;

import com.sg.shopping.common.utils.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    JsonResult handleMaxUploadFile(MaxUploadSizeExceededException ex) {
        return JsonResult.errorMsg("File size cannot larger than 500KB");
    }

}

