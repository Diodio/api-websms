package sn.kiwi.apiwebsms.dtos;

public class ApiDtoResponse {
    private Boolean success;
    private String message;
    private int code;

    public ApiDtoResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiDtoResponse(Boolean success, String message, int code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

