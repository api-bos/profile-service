package com.bos.profile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private int id_seller;
    private String username;
    private String name;
    private String card_number;
    private String phone;
    private String shop_name;
    private String image_path;
    private int id_kota_kab;
    private String base64StringImage;
    private ArrayList<SelectedCourier> selected_courier;
}
