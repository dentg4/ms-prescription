package com.codigo.clinica.msprescription.application.controller;

import com.codigo.clinica.msprescription.domain.aggregates.dto.PrescriptionDetailDto;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailListRequest;
import com.codigo.clinica.msprescription.domain.aggregates.request.PrescriptionDetailRequest;
import com.codigo.clinica.msprescription.domain.ports.in.PrescriptionDetailServiceIn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "API Rest de mantenimiento de Detalle de prescripcion.",
        description = "Incluye EndPoints para realizar el mantenimiento de los Detalle de prescripcion."
)
@RestController
@RequestMapping("/api/v1/ms-prescription/prescription/detail")
@AllArgsConstructor
public class PrescriptionDetailController {
    private final PrescriptionDetailServiceIn prescriptionDetailServiceIn;

    @Operation(summary = "Crear un Detalle de prescripcion.",
            description = "Para usar este EndPoint, debes enviar un objeto Detalle de prescripcion que será guardado en base de datos, previa validacion.")
    @ApiResponse(responseCode = "200", description = "Detalle de prescripcion creado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDetailDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @PostMapping("/create")
    public ResponseEntity<PrescriptionDetailDto> create(@RequestBody PrescriptionDetailRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prescriptionDetailServiceIn.createDetailIn(request));
    }

    @Operation(summary = "Crear un Lista de Detalle de prescripcion.",
            description = "Para usar este EndPoint, debes enviar un objeto Detalle de prescripcion List que será guardado en base de datos, previa validacion.")
    @ApiResponse(responseCode = "200", description = "Detalle de prescripcion creado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDetailDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @PostMapping("/list/create")
    public ResponseEntity<List<PrescriptionDetailDto>> createList(@RequestBody PrescriptionDetailListRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prescriptionDetailServiceIn.createListDetailIn(request));
    }

    @Operation(summary = "Buscar todos los registros de Detalle de prescripcion.",
            description = "EndPoint que lista todos los registros de Detalle de prescripcion de la base de datos.")
    @ApiResponse(responseCode = "200", description = "Detalle de prescripcion encontradas con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDetailDto.class))})
    @ApiResponse(responseCode = "404", description = "Detalle de prescripcion no encontradas.", content = { @Content(schema = @Schema()) })
    @GetMapping("/all")
    public ResponseEntity<List<PrescriptionDetailDto>> getAll(){
        return ResponseEntity.ok(prescriptionDetailServiceIn.getAllIn());
    }

    @Operation(summary = "Buscar un Detalle de prescripcion por su Id.",
            description = "Para usar este EndPoint, debes enviar el Id del Detalle de prescripcion a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id de búsqueda.", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Detalle de prescripcion encontrado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDetailDto.class))})
    @ApiResponse(responseCode = "404", description = "Detalle de prescripcion no encontrada.", content = { @Content(schema = @Schema()) })
    @GetMapping("/find/{id}")
    public ResponseEntity<PrescriptionDetailDto> getFindById(@PathVariable Long id){
        return prescriptionDetailServiceIn.findByIdIn(id).map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Actualizar un Detalle de prescripcion.",
            description = "Para usar este EndPoint, debes enviar un objeto Detalle de prescripcion (sus cambios) que será guardado en base de datos, previa validacion.",
            parameters = {
                    @Parameter(name = "id", description = "Id de Detalle de prescripcion.", required = true, example = "1"),
            })
    @ApiResponse(responseCode = "200", description = "Detalle de prescripcion actualizada con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDetailDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @PutMapping("/update/{id}")
    public ResponseEntity<PrescriptionDetailDto> update(@PathVariable Long id,
                                              @RequestBody PrescriptionDetailRequest request){
        return ResponseEntity.ok(prescriptionDetailServiceIn.updateIn(id, request));
    }

    @Operation(summary = "Eliminar un Detalle de prescripcion por su Id.",
            description = "Para usar este EndPoint, enviar el Id del Detalle de prescripcion a través de un PathVariable.",
            parameters = {
                    @Parameter(name = "id", description = "Id para eliminación.", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "Detalle de prescripcion eliminado con éxito.", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PrescriptionDetailDto.class))})
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = { @Content(schema = @Schema()) })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PrescriptionDetailDto> delete(@PathVariable Long id){
        return ResponseEntity.ok(prescriptionDetailServiceIn.deleteIn(id));
    }
}
