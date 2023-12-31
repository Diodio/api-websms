package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PacksListDto {

    private long id;
    private String name;
    private int numberOfCredits;
    private int duration;
    private double packAmount;
    private int activate;
    private String description;
    private String typeMSG;
    private int voiceNumberOfSecond;


    public PacksListDto() {
    }

    public PacksListDto(long id, String name, int numberOfCredits, int duration, double packAmount, int activate, String description, String typeMSG, int voiceNumberOfSecond ) {
        this.id = id;
        this.name = name;
        this.numberOfCredits = numberOfCredits;
        this.duration = duration;
        this.packAmount = packAmount;
        this.activate = activate;
        this.description = description;
        this.typeMSG=typeMSG;
        this.voiceNumberOfSecond=voiceNumberOfSecond;
    }
}