package team.weacsoft.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.weacsoft.common.exception.BadRequestException;
import team.weacsoft.common.exception.EntityExistException;
import team.weacsoft.common.exception.EntityNotFoundException;
import team.weacsoft.common.utils.ThrowableUtil;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * 全局异常拦截
 * @author GreenHatHG
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ApiResp> badRequestException(BadRequestException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return new ResponseEntity<>(ApiResp.error(e.getStatus(),e.getMessage()), BAD_REQUEST);
    }

    /**
     * 处理 EntityExist
     */
    @ExceptionHandler(value = EntityExistException.class)
    public ResponseEntity<ApiResp> entityExistException(EntityExistException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiResp.error(e.getMessage()));
    }

    /**
     * 处理 EntityNotFound
     */
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiResp> entityNotFoundException(EntityNotFoundException e) {
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiResp.error(NOT_FOUND.value(),e.getMessage()));
    }

//    /**
//     * 处理所有接口数据验证异常
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResp> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
//        // 打印堆栈信息
//        log.error(ThrowableUtil.getStackTrace(e));
//        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
//        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
//        String msg = "不能为空";
//        if(msg.equals(message)){
//            message = str[1] + ":" + message;
//        }
//        return buildResponseEntity(ApiResp.error(message));
//    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ResponseEntity<ApiResp> handle(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getInvalidValue()).append(" ").append(violation.getMessage()).append("\n");
        }
        String result = strBuilder.toString();
        return buildResponseEntity(ApiResp.error(BAD_REQUEST.value(),
                "ConstraintViolation:" + result));
    }

    @ExceptionHandler(value = { BindException.class })
    protected ResponseEntity<ApiResp> handleBindException(BindException ex) {
        return buildResponseEntity(ApiResp.error(BAD_REQUEST.value(),
                "BindException:" + buildMessages(ex.getBindingResult())));
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    protected ResponseEntity<ApiResp> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return buildResponseEntity(ApiResp.error(BAD_REQUEST.value(),
                "MethodArgumentNotValid:" + buildMessages(ex.getBindingResult())));
    }

    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    public ResponseEntity<ApiResp> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        return buildResponseEntity(ApiResp.error(BAD_REQUEST.value(),
                "ParamMissing:" + ex.getMessage()));
    }

    @ExceptionHandler(value = { TypeMismatchException.class })
    protected ResponseEntity<ApiResp> handleTypeMismatch(TypeMismatchException ex) {
        return buildResponseEntity(ApiResp.error(BAD_REQUEST.value(),
                "TypeMissMatch:" + ex.getMessage()));
    }

    private String buildMessages(BindingResult result) {
        StringBuilder resultBuilder = new StringBuilder();

        List<ObjectError> errors = result.getAllErrors();
        if (errors.size() > 0) {
            for (ObjectError error : errors) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    String fieldName = fieldError.getField();
                    String fieldErrMsg = fieldError.getDefaultMessage();
                    resultBuilder.append(fieldName).append(fieldErrMsg).append(";");
                }
            }
        }
        return resultBuilder.toString();
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResp> handleException(Throwable e){
        // 打印堆栈信息
        log.error(ThrowableUtil.getStackTrace(e));
        return buildResponseEntity(ApiResp.error(e.getMessage()));
    }

    /**
     * 统一返回
     */
    private ResponseEntity<ApiResp> buildResponseEntity(ApiResp apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getErrorcode()));
    }
}