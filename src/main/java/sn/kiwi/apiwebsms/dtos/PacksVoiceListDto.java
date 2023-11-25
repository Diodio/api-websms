package sn.kiwi.apiwebsms.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PacksVoiceListDto {

    private long id;
    private String name;
    private int duration;
    private double packAmount;
    private int activate;
    private String description;
    private String typeMSG;
    private String voiceNumberOfSecond;

    public PacksVoiceListDto() {
    }

    public PacksVoiceListDto(long id, String name, int duration, double packAmount, int activate, String description, String typeMSG, String voiceNumberOfSecond ) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.packAmount = packAmount;
        this.activate = activate;
        this.description = description;
        this.typeMSG=typeMSG;
        this.voiceNumberOfSecond=voiceNumberOfSecond;
    }
}