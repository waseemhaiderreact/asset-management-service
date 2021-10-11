package com.sharklabs.ams.util;

public class AccessDeniedException extends Exception {

    public AccessDeniedException() {
        super("Access is Denied.");
    }
}
