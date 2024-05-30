package com.codigo.clinica.msprescription.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDetailListRequest {

    @NotBlank(message = "El campo prescripcion es necesario.")
    private Long prescriptionId;

    @NotBlank(message = "El campo de lista es necesario.")
    private List<DetailMedicinePair> list;
}
