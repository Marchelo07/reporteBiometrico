package com.marchelo.excel.biome.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoDetailLaunchTime {
    private String id;
    private String nombre;
    private String fecha;
    private String horaOutLaunch;
    private String horaInLaunch;
    private String tiempoLaunch;
}
