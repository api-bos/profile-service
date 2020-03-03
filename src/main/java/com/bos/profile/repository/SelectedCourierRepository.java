package com.bos.profile.repository;

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

    @Query(value = "SELECT * FROM selected_courier WHERE id_seller = :id_seller", nativeQuery = true)
    ArrayList<SelectedCourier> getSelectedCourier(@Param("id_seller") int id_seller);
}
