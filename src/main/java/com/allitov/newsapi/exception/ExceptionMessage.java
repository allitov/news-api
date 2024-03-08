package com.allitov.newsapi.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage {

    public final String AUTHENTICATION_FAILURE = "Authentication failure";

    public final String ACCESS_DENIED = "No required authorities";

    public final String USER_DATA_ILLEGAL_ACCESS = "User with id = '%d' cannot get or change data of user with id = '%d'";

    public final String USER_BY_ID_NOT_FOUND = "User with id = '%d' not found";

    public final String USER_BY_USERNAME_NOT_FOUND = "User with username = '%s' not found";

    public final String USER_BLANK_USERNAME = "Username must be specified";

    public final String USER_INVALID_USERNAME = "Username length must be {min} <= length <= {max}";

    public final String USER_BLANK_EMAIL = "Email must be specified";

    public final String USER_INVALID_EMAIL = "Email length must be {min} <= length <= {max}";

    public final String USER_BLANK_PASSWORD = "Password must be specified";

    public final String USER_INVALID_PASSWORD = "Password length must be {min} <= length <= {max}";

    public final String USER_NULL_ROLES = "Roles must be specified";

    public final String USER_INVALID_ROLES = "User roles must be any of ['USER', 'MODERATOR', 'ADMIN']";

    public final String NEWS_CATEGORY_BLANK_NAME = "Name must be specified";

    public final String NEWS_CATEGORY_INVALID_NAME = "News category name length must be {min} <= length <= {max}";

    public final String FILTER_NULL_PAGE_SIZE = "Page size must be specified";

    public final String FILTER_INVALID_PAGE_SIZE = "Page size must be > 0";

    public final String FILTER_NULL_PAGE_NUMBER = "Page number must be specified";

    public final String FILTER_INVALID_PAGE_NUMBER = "Page number must be >= 0";
}
