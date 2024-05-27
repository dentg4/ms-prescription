package com.codigo.clinica.msprescription.domain.aggregates.request;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailMedicinePair {
    private Integer amount;
    private String indications;
    private Long medicineId;
}
