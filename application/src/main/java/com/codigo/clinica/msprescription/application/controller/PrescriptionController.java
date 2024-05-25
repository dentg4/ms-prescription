package com.codigo.clinica.msprescription.application.controller;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionRequest;
import com.codigo.clinica.msprescription.domain.ports.in.PrescriptionServiceIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "API Rest de mantenimiento de Prescripciones.",
        description = "Incluye EndPoints para realizar el mantenimiento de los Prescripciones."
)
@RestController
@RequestMapping("/api/v1/ms-prescription/prescription")
@AllArgsConstructor
public class PrescriptionController {

    private final PrescriptionServiceIn prescriptionServiceIn;

    @Operation(summary = "Crear un Prescripción.",
            description = "Para usar este EndPoint, debes enviar un objeto Prescripción que será guardado en base de datos, previa validacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescripción creado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/create")
    public ResponseEntity<PrescriptionDto> create(@RequestBody PrescriptionRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prescriptionServiceIn.createPrescriptionIn(request));
    }

    @Operation(summary = "Buscar todos los registros de Prescripciones.",
            description = "EndPoint que lista todos los registros de Prescripciones de la base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescripciones encontradas con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Prescripciones no encontradas.", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/all")
    public ResponseEntity<List<PrescriptionDto>> getAll(){
        return ResponseEntity.ok(prescriptionServiceIn.getAllIn());
    }

    @Operation(summary = "Buscar un Prescripción por su Id.",
            description = "Para usar este EndPoint, debes enviar el Id del Prescripción a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id de búsqueda.", required = true, example = "1")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescripción encontrado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Prescripción no encontrada.", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/find/{id}")
    public ResponseEntity<PrescriptionDto> getFindById(@PathVariable Long id){
        return prescriptionServiceIn.findByIdIn(id).map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Actualizar un Prescripción.",
            description = "Para usar este EndPoint, debes enviar un objeto Prescripción (sus cambios) que será guardado en base de datos, previa validacion.",
            parameters = {
                    @Parameter(name = "id", description = "Id de Prescripción.", required = true, example = "1"),
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescripción actualizada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<PrescriptionDto> update(@PathVariable Long id,
                                                                      @RequestBody PrescriptionRequest request){
        return ResponseEntity.ok(prescriptionServiceIn.updateIn(id, request));
    }

    @Operation(summary = "Eliminar un Prescripción por su Id.",
            description = "Para usar este EndPoint, enviar el Id del Prescripción a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id para eliminación.", required = true, example = "1")
            })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescripción eliminado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDto.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PrescriptionDto> delete(@PathVariable Long id){
        return ResponseEntity.ok(prescriptionServiceIn.deleteIn(id));
    }
}
