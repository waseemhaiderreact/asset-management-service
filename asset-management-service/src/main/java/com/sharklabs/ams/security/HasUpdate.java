//package com.sharklabs.ams.security;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
//@PreAuthorize("hasAuthority('ASSETS_UPDATE_WEB') || hasAuthority('ASSETS_UPDATE_MOBILE')")
//public @interface HasUpdate {
//}