package com.bos.profile.model;

import com.bos.profile.dao.SelectedCourierDao;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.ArrayList;

@Getter
@Setter
public class ProfileDetail {
    @Id
    private int id_seller;
    private String username;
    private String name;
    private String card_number;
    private String phone;
    private String shop_name;
    private String image_path;
    private int id_kab_kota;
    private String base64StringImage;
    private ArrayList<SelectedCourierDao> selected_courier;
}
