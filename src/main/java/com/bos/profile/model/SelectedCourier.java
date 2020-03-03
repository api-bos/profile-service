package com.bos.profile.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "selected_courier")
public class SelectedCourier {
    @Id
    private int id_selected_courier;
    private int id_seller;
    private int id_courier;
    private int is_selected;
}

