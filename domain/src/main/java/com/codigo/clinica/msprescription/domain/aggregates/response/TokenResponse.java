package com.codigo.clinica.msprescription.domain.aggregates.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenResponse {
    private boolean isValid;
    private List<String> roles;
    private String username;
    private boolean isTokenExpired;
}
