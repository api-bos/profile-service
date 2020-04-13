package com.bos.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seller")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_seller;
    private String username;
    private String password;
    private String name;
    private String card_number;
    private String email;
    private String phone;
    private String shop_name;
    private String image_path;
    private int id_kab_kota;
}
