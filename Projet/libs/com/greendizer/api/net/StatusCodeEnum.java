package com.greendizer.api.net;

import java.net.HttpURLConnection;

/**
 * Represents an enumeration of used HTTP status codes.
 */
public enum StatusCodeEnum {

  OK                                (HttpURLConnection.HTTP_OK),
  PARTIAL_CONTENT                   (HttpURLConnection.HTTP_PARTIAL),
  NO_CONTENT                        (HttpURLConnection.HTTP_NO_CONTENT),
  NOT_MODIFIED                      (HttpURLConnection.HTTP_NOT_MODIFIED),
  CREATED                           (HttpURLConnection.HTTP_CREATED),
  FOUND                             (302),
  REQUESTED_RANGE_NOT_SATISFIABLE   (416),
  ACCEPTED                          (HttpURLConnection.HTTP_ACCEPTED),
  BAD_REQUEST                       (HttpURLConnection.HTTP_BAD_REQUEST),
  NOT_FOUND                         (HttpURLConnection.HTTP_NOT_FOUND),
  UNAUTHORIZED                      (HttpURLConnection.HTTP_UNAUTHORIZED),
  INTERNAL_ERROR                    (HttpURLConnection.HTTP_INTERNAL_ERROR)
  ;

  private final int code;

  private StatusCodeEnum(int code) {
    this.code = code;
  }

  /**
   * Returns the status code.
   * @return The status code.
   */
  public int getCode() {
    return code;
  }

  /**
   * Returns true if the code is acceptable (ie. under 500), false otherwise.
   * @return True if the code is acceptable (ie. under 500), false otherwise.
   */
  public boolean isError() {
    return code >= 500;
  }

  /**
   * Creates a {@code StatusCodeEnum} based on an integer status code.
   * @param code Integer status code.
   * @return Newly created {@code StatusCodeEnum}.
   */
  public static StatusCodeEnum parse(int code) {
    for (StatusCodeEnum statusCode : values()) {
      if (statusCode.getCode() == code) {
        return statusCode;
      }
    }
    return null;
  }
}
