package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PromoListDto {

    private long id;
    private String audio;
    private String image;
    private String createdDate;
    private String startDate;
    private String endDate;

    public PromoListDto() {
    }

    public PromoListDto(long id, String audio, String image, String createdDate, String startDate, String endDate) {
        this.id = id;
        this.audio = audio;
        this.image = image;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}