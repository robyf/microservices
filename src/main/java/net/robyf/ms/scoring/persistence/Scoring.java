package net.robyf.ms.scoring.persistence;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "scoring")
public class Scoring {

    @Id
    @GeneratedValue
    private UUID id;

    @Column (name = "request", length = 65535, nullable = false)
    private String request;

    @Column (name = "pod", nullable = false)
    private BigDecimal probabilityOfDefault;

    @Tolerate
    public Scoring() {
    }

}
