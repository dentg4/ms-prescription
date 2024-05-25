package com.codigo.clinica.msprescription.domain.aggregates.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicineRequest {

    private String name;

    private String description;
}
