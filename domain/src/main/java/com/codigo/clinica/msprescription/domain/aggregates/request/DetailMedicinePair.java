package com.codigo.clinica.msprescription.domain.aggregates.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetailMedicinePair {
    private Integer amount;
    private String indications;
    private Long medicineId;
}
