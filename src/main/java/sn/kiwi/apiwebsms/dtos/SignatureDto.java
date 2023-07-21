package sn.kiwi.apiwebsms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignatureDto {

    private long id;
    private String wording;
    private String createdDate;
    private String updatedDate;
    private String reasonrejeted;
    private int activate;

    public SignatureDto() {
    }

    public SignatureDto(long id, String wording, String createdDate, String updatedDate, String reasonrejeted, int activate) {
        this.id = id;
        this.wording = wording;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.reasonrejeted = reasonrejeted;
        this.activate = activate;
    }
}
