package sn.kiwi.apiwebsms.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessageDetailDto {

    private long id;
    private String subject;
    private String signature;
    private String groups;
    private String content;
    private String createdDate;
    private String updatedDate;
    private Object stats;



    public MessageDetailDto() {
    }

    public MessageDetailDto(long id, String subject, String signature, String groups, String content, String createdDate, String updatedDate, Object stats) {
        this.id = id;
        this.subject = subject;
        this.signature = signature;
        this.groups = groups;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.stats = stats;
    }
}
