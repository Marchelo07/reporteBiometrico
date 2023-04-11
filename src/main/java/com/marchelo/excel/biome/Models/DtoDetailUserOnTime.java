package com.marchelo.excel.biome.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DtoDetailUserOnTime {
    private String id;
    private String nombre;
    private String fecha;
    private String horaIngreso;
    private String horaOutLaunch;
    private String horaInLaunch;
    private String horaSalida;

    public DtoDetailUserOnTime(String id, String nombre, String fecha, String horaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaIngreso = horaIngreso;
    }

    public DtoDetailUserOnTime(String id, String nombre, String fecha, String horaIngreso, String horaSalida) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
    }

    public DtoDetailUserOnTime(String id, String nombre, String fecha, String horaIngreso, String horaOutLaunch, String horaInLaunch, String horaSalida) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaIngreso = horaIngreso;
        this.horaOutLaunch = horaOutLaunch;
        this.horaInLaunch = horaInLaunch;
        this.horaSalida = horaSalida;
    }
}
