package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CallBackDto {

    private String participantId;
    private String duration;
    private String terminationDate;
    private String createdDate;
    private String status;
    private String terminationCause;

    public CallBackDto() {
    }

    public CallBackDto(String callDirection, String participantId, String terminationDate,
                       String createdDate, String status, String duration, String terminationCause) {
        this.participantId = participantId;
        this.terminationDate = terminationDate;
        this.createdDate = createdDate;
        this.terminationCause = terminationCause;
        this.status = status;
        this.duration = duration;
    }
}