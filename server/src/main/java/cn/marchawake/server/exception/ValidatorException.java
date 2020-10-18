package cn.marchawake.server.exception;

public class ValidatorException extends RuntimeException{

    public ValidatorException(String message) {
        super(message);
    }

    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
