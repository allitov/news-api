package com.allitov.newsapi.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessage {

    public final String USER_DATA_ILLEGAL_ACCESS = "User with id = '%d' cannot get data of user with id = '%d'";

    public final String USER_BY_ID_NOT_FOUND = "User with id = '%d' not found";

    public final String USER_BY_USERNAME_NOT_FOUND = "User with username = '%s' not found";
}
