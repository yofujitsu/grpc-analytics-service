package com.yofujitsu.grpcanalyticsservice.entity.data;

import com.yofujitsu.grpccommon.GRPCData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Data {

    private Long id;
    private Long heroId;
    private LocalDateTime timestamp;
    private Double value;
    private ValueType valueType;


    public Data(GRPCData grpcData) {
        this.id = grpcData.getId();
        this.heroId = grpcData.getHeroId();
        this.timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(
                        grpcData.getTimestamp().getSeconds(),
                        grpcData.getTimestamp().getNanos()
                ),
                ZoneId.of("UTC")
        );
        this.value = grpcData.getValue();
        this.valueType = ValueType.valueOf(
                grpcData.getValueType().name()
        );
    }
}
