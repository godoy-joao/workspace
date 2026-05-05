package io.github.godoyjoao.workspace.exception;

public class TenantViolationException extends RuntimeException {

    public static String NOT_PARTICIPANT = "User does not have access to the specified workspace.";
    public TenantViolationException(String message) {
        super(message);
    }
}
