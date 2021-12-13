package com.sharklabs.ams.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 *    Description: The purpose of the class is to build variables with constant values, so the constant values can be used on functions as parameters,
 *    Or for calculation purpose. So these constants have a meaningful name.
 *    Version History: v1
 *
 *    Version        Author            Description                Date
 *    ===============================================================
 *    v1            M.Saad Anjum    Initial Version            24-June-2020
 *    v2            Abdul Rehman    Added SDT Constants        12-October-2020
 *
 */
public class Constant {

    public static String ASC="asc";
    public static String DESC="desc";

    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Failure";

    // ---------------- SDT Constants START

    public static final String TOTAL_ELEMENTS = "totalElements";
    public static final String TOTAL_PAGES = "totalPages";
    public static final String CONTENT = "content";

    public static final String  SDT_FIELD = "fieldName";
    public static final String  SDT_TYPE = "fieldType";
    public static final String  SDT_VALUE1 = "filterValue";
    public static final String  SDT_VALUE2 = "secondValue";
    public static final String  SDT_SEPARATOR = "&"; // Multiple Value separator
    public static final String  SDT_VOID = "!#"; // Default value to set if provided value doesn't exists
    public static final String  SDT_LIKE = "Like";
    public static final String  SDT_EQUALS = "=";
    public static final String  SDT_NOT_EQUALS = "!=";
    public static final String  SDT_BETWEEN = "btw";
    public static final String  SDT_GREATER_THAN = ">";
    public static final String  SDT_LESS_THAN = "<";
    public static final String SDT_LESS_THAN_EQUAL_TO = "<=";
    public static final String SDT_GREATER_THAN_EQUAL_TO = ">=";
    public static final String COMPARISON_TYPE = "comparisonType";

    // ---------------- SDT Constants END

    // Secret key for unprotected calls (alpha-numeric)
    public static  final String SECRET_KEY = "ypFWwA-E8VgkQ9GWn-PzYUEyfEQ-fgeba7H3Q-yEEwHy";

    //Asset Info And Required Fields
    public static final List<String> ASSET_INFO = Arrays.asList("category name","asset name","model #","manufacturer name/id","purchased date","status","warranty unit","warranty","primary usage unit","secondary usage unit","consumption unit","write asset description here");

    public static final List<String> ASSET_REQUIRED_FIELDS = Arrays.asList("category name","asset name","model #","manufacturer name/id","warranty unit","warranty","primary usage unit","secondary usage unit","consumption unit");

    // Statuses For Imports
    public static final String IMPORT_SUCCESS = "Success";

    public static final String IMPORT_FAILURE = "Failure";

    public static final String IMPORT_PARTIAL_SUCCESS = "Partial Success";

    public static final String IMPORT_COMPLETE = "Complete";

    public static final String IMPORT_IN_COMPLETE = "Incomplete";

    //Asset Statuses
    public static final List<String> ASSET_STATUSES = Arrays.asList("active","inactive","outofservice","spare","expired/disposed","sold");

}
