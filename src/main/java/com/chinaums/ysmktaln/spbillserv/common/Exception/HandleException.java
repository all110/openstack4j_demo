package com.chinaums.ysmktaln.spbillserv.common.Exception;

public class HandleException extends RuntimeException {
   private static final long serialVersionUID = 1L;
   private HandleCode errorCode;
   private String message;

   public HandleException(String code, String errorMsg) {
      this.errorCode = HandleCode.get(code, HandleCode.RSP_9999);
      this.message = errorMsg;
   }

   public HandleException(HandleCode errorCode, String errorMsg) {
      this.errorCode = errorCode;
      this.message = errorMsg;
   }

   public HandleException(HandleCode errorCode) {
      super(errorCode == null ? "" : errorCode.desc());
      this.errorCode = errorCode;
   }

   public String getCode() {
      return this.errorCode.code();
   }

   public String getMessage() {
      return this.message == null ? this.errorCode.desc() : this.message;
   }

   public static HandleException sysError() {
      return new HandleException(HandleCode.RSP_9999);
   }

   public String toString() {
      return String.format("%s - [code:'%s', message:'%s']", this.getClass().getName(), this.errorCode.code(), this.message);
   }
}
