package com.bos.profile.repository;

import com.bos.profile.dao.SelectedCourierDao;
import com.bos.profile.model.SelectedCourier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.ArrayList;

public interface SelectedCourierRepository extends JpaRepository<SelectedCourier, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE selected_courier SET is_selected = :is_selected WHERE id_seller = :id_seller AND id_courier = :id_courier", nativeQuery = true)
    void updateSelectedCourier(@Param("is_selected") int is_selected, @Param("id_seller") int id_seller, @Param("id_courier") int id_courier);

    @Query(value = "SELECT s.id_selected_courier AS id_selected_courier, " +
            "s.id_courier AS id_courier, c.corier_code AS courier_code, c.courier_name AS courier_name, s.is_selected AS is_selected\n" +
            "FROM selected_courier s\n" +
            "JOIN courier c\n" +
            "ON s.id_courier = c.id_courier\n" +
            "WHERE s.id_seller = :id_seller", nativeQuery = true)
    ArrayList<SelectedCourierDao> getSelectedCourier(@Param("id_seller") int id_seller);
}
